package com.sjn.gymexe.ui.screens.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.local.dao.SessionDao
import com.sjn.gymexe.data.local.entity.SessionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val sessionDao: SessionDao
) : ViewModel() {
    val sessions = sessionDao.getAllSessions()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Workout History", style = MaterialTheme.typography.headlineMedium)

            if (sessions.isNotEmpty()) {
                HistoryChart(sessions = sessions)
            } else {
                 Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                     Text("No history yet. Go lift something!")
                 }
            }
        }
    }
}

@Composable
fun HistoryChart(sessions: List<SessionEntity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Recent Activity (Duration)", style = MaterialTheme.typography.titleSmall)

            Canvas(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                if (sessions.isEmpty()) return@Canvas

                // Normalize data (Duration in minutes)
                // Filter out null endTimes
                val dataPoints = sessions.filter { it.endTime != null }.take(10).map {
                    val end = it.endTime ?: it.startTime
                    ((end - it.startTime) / 60000).toFloat()
                }.reversed() // Show oldest to newest left to right

                if (dataPoints.isEmpty()) return@Canvas

                val maxVal = dataPoints.maxOrNull() ?: 1f
                val width = size.width
                val height = size.height
                val stepX = width / (dataPoints.size - 1).coerceAtLeast(1)

                val path = Path().apply {
                    dataPoints.forEachIndexed { index, value ->
                        val x = index * stepX
                        val y = height - (value / maxVal * height)
                        if (index == 0) moveTo(x, y) else lineTo(x, y)
                    }
                }

                drawPath(
                    path = path,
                    color = Color.Blue, // Should use Theme color
                    style = Stroke(width = 4.dp.toPx())
                )

                // Draw dots
                dataPoints.forEachIndexed { index, value ->
                    val x = index * stepX
                    val y = height - (value / maxVal * height)
                    drawCircle(
                        color = Color.Red,
                        radius = 6.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}
