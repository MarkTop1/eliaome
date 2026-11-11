package com.eliamo.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = StampRed,
    onPrimary = PaperCard,
    secondary = GrassGreen,
    background = Paper,
    onBackground = Ink,
    surface = PaperCard,
    onSurface = Ink,
    outline = Line,
    error = StampDeep
)

private val EliaoShapes = Shapes(
    small = RoundedCornerShape(D.Sm),
    medium = RoundedCornerShape(D.Button),
    large = RoundedCornerShape(D.Card)
)

@Composable
fun EliaoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightScheme,
        typography = EliaoTypography,
        shapes = EliaoShapes,
        content = content
    )
}
