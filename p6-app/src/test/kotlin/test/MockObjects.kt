package test

import androidx.compose.ui.unit.Density

/**
 * Contain globally avaiable mock objects.
 */
object MockObjects{
    val mockDensity = object : Density {
        override val density: Float
            get() = 1f
        override val fontScale: Float
            get() = 1f
    }
}