package com.sjn.gym.core.model

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class ArchitectureKonsistTest {

    @Test
    fun `viewmodels should reside in their respective feature packages and end with ViewModel`() {
        Konsist.scopeFromProject().classes().withNameEndingWith("ViewModel").assertTrue {
            it.resideInPackage("..feature..") || it.resideInPackage("com.sjn.gym")
        }
    }
}
