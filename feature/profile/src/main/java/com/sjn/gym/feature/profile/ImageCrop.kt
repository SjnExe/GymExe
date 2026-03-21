package com.sjn.gym.feature.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCropScreen(
    uri: Uri,
    onCropSuccess: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                bitmap =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            decoder.isMutableRequired = true
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (bitmap == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    val cropSize = 300.dp
    val density = context.resources.displayMetrics.density
    val cropDiameterPx = cropSize.value * density

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Crop Photo") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val croppedBitmap =
                                cropImage(
                                    bitmap = bitmap!!,
                                    containerSize = size,
                                    scale = scale,
                                    offset = offset,
                                    cropDiameter = cropDiameterPx,
                                )
                            val file =
                                File(
                                    context.filesDir,
                                    "profile_pic_${System.currentTimeMillis()}.jpg",
                                )
                            FileOutputStream(file).use { out ->
                                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                            }
                            onCropSuccess(file.absolutePath)
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Done")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.Black)
                    .onSizeChanged { size = it }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.1f, 5f)
                            offset += pan
                        }
                    },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier =
                    Modifier.fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y,
                        ),
                contentScale = ContentScale.Fit,
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val circleRadius = cropDiameterPx / 2f
                val circleCenter = Offset(size.width / 2f, size.height / 2f)

                val overlayPath =
                    Path().apply {
                        addRect(Rect(0f, 0f, size.width.toFloat(), size.height.toFloat()))
                    }

                val circlePath =
                    Path().apply {
                        addOval(
                            Rect(
                                left = circleCenter.x - circleRadius,
                                top = circleCenter.y - circleRadius,
                                right = circleCenter.x + circleRadius,
                                bottom = circleCenter.y + circleRadius,
                            )
                        )
                    }

                val combinedPath =
                    Path().apply { op(overlayPath, circlePath, PathOperation.Difference) }

                drawPath(path = combinedPath, color = Color.Black.copy(alpha = 0.6f))
            }
        }
    }
}

private fun cropImage(
    bitmap: Bitmap,
    containerSize: IntSize,
    scale: Float,
    offset: Offset,
    cropDiameter: Float,
): Bitmap {
    val bitmapAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
    val containerAspectRatio = containerSize.width.toFloat() / containerSize.height.toFloat()

    val displayWidth: Float
    val displayHeight: Float

    if (bitmapAspectRatio > containerAspectRatio) {
        displayWidth = containerSize.width.toFloat()
        displayHeight = displayWidth / bitmapAspectRatio
    } else {
        displayHeight = containerSize.height.toFloat()
        displayWidth = displayHeight * bitmapAspectRatio
    }

    val scaledDisplayWidth = displayWidth * scale
    val scaledDisplayHeight = displayHeight * scale

    val circleRadius = cropDiameter / 2f
    val containerCenterX = containerSize.width / 2f
    val containerCenterY = containerSize.height / 2f

    val imageLeft = containerCenterX - (scaledDisplayWidth / 2f) + offset.x
    val imageTop = containerCenterY - (scaledDisplayHeight / 2f) + offset.y

    val cropLeftOnDisplay = containerCenterX - circleRadius
    val cropTopOnDisplay = containerCenterY - circleRadius

    val startX = ((cropLeftOnDisplay - imageLeft) / scaledDisplayWidth * bitmap.width).toInt()
    val startY = ((cropTopOnDisplay - imageTop) / scaledDisplayHeight * bitmap.height).toInt()

    val cropWidth = (cropDiameter / scaledDisplayWidth * bitmap.width).toInt()
    val cropHeight = (cropDiameter / scaledDisplayHeight * bitmap.height).toInt()

    val safeStartX = startX.coerceIn(0, bitmap.width - 1)
    val safeStartY = startY.coerceIn(0, bitmap.height - 1)

    // Check if x + width goes out of bounds
    val safeWidth =
        if (safeStartX + cropWidth > bitmap.width) {
            bitmap.width - safeStartX
        } else {
            cropWidth
        }

    val safeHeight =
        if (safeStartY + cropHeight > bitmap.height) {
            bitmap.height - safeStartY
        } else {
            cropHeight
        }

    return if (safeWidth > 0 && safeHeight > 0) {
        Bitmap.createBitmap(bitmap, safeStartX, safeStartY, safeWidth, safeHeight)
    } else {
        bitmap
    }
}
