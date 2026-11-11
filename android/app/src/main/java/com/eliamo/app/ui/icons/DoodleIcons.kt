package com.eliamo.app.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eliamo.app.ui.theme.Ink

// 与 prototype/index.html 的内联 SVG symbol 使用同一套 24x24 手绘线条几何
enum class DoodleIcon {
    TODAY, CALENDAR, REPORT, BADGE, SUPERMAN, SETTINGS, CLOSE, CHECK, CHEVRON,
    DROP, CLOCK, PLUS, SHARE, REFRESH, LOCK
}

@Composable
fun DoodleIconView(
    icon: DoodleIcon,
    modifier: Modifier = Modifier,
    tint: Color = Ink,
    size: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(size)) {
        drawDoodle(icon, tint, this.size.width / 24f)
    }
}

private fun DrawScope.drawDoodle(icon: DoodleIcon, c: Color, s: Float) {
    val w = 2f * s

    fun line(x1: Float, y1: Float, x2: Float, y2: Float, width: Float = w) =
        drawLine(c, Offset(x1 * s, y1 * s), Offset(x2 * s, y2 * s), width, StrokeCap.Round)

    fun circle(x: Float, y: Float, r: Float, fill: Boolean = false) =
        drawCircle(c, r * s, Offset(x * s, y * s), style = if (fill) Fill else Stroke(w))

    fun box(x: Float, y: Float, bw: Float, bh: Float, r: Float = 0f) =
        drawRoundRect(
            c,
            topLeft = Offset(x * s, y * s),
            size = Size(bw * s, bh * s),
            cornerRadius = CornerRadius(r * s, r * s),
            style = Stroke(w)
        )

    fun strokePath(build: Path.() -> Unit) {
        val p = Path()
        p.build()
        drawPath(p, c, style = Stroke(w, cap = StrokeCap.Round))
    }

    when (icon) {
        DoodleIcon.TODAY -> {
            circle(12f, 12f, 8.5f)
            circle(9.2f, 10f, 0.9f, true)
            circle(14.8f, 10f, 0.9f, true)
            strokePath {
                moveTo(8.6f * s, 14.4f * s)
                quadraticTo(12f * s, 18f * s, 15.4f * s, 14.4f * s)
            }
        }
        DoodleIcon.CALENDAR -> {
            box(3f, 5f, 18f, 16f, 2.5f)
            line(8f, 3f, 8f, 7f)
            line(16f, 3f, 16f, 7f)
            line(3f, 10f, 21f, 10f)
            circle(8.5f, 14f, 1f, true)
            circle(12f, 14f, 1f, true)
            circle(15.5f, 14f, 1f, true)
        }
        DoodleIcon.REPORT -> {
            line(3f, 20f, 21f, 20f)
            line(6f, 20f, 6f, 14f, 3f * s)
            line(11f, 20f, 11f, 7f, 3f * s)
            line(16f, 20f, 16f, 11f, 3f * s)
            line(20f, 20f, 20f, 16f, 3f * s)
        }
        DoodleIcon.BADGE -> {
            circle(12f, 9f, 5.5f)
            line(8.5f, 13.5f, 7f, 22f)
            line(7f, 22f, 12f, 19.4f)
            line(12f, 19.4f, 17f, 22f)
            line(17f, 22f, 15.5f, 13.5f)
            line(12f, 6.5f, 12f, 10.5f)
            line(10f, 8.5f, 14f, 8.5f)
        }
        DoodleIcon.SETTINGS -> {
            line(4f, 7f, 20f, 7f)
            line(4f, 12f, 20f, 12f)
            line(4f, 17f, 20f, 17f)
            circle(9f, 7f, 2f)
            circle(15f, 12f, 2f)
            circle(8f, 17f, 2f)
        }
        DoodleIcon.CLOSE -> {
            line(6f, 6f, 18f, 18f)
            line(18f, 6f, 6f, 18f)
        }
        DoodleIcon.CHECK -> {
            line(5f, 12f, 9.5f, 16.5f)
            line(9.5f, 16.5f, 19f, 7f)
        }
        DoodleIcon.CHEVRON -> {
            line(14f, 6f, 8f, 12f)
            line(8f, 12f, 14f, 18f)
        }
        DoodleIcon.DROP -> strokePath {
            moveTo(12f * s, 3f * s)
            cubicTo(15f * s, 7.5f * s, 18f * s, 10.6f * s, 18f * s, 14f * s)
            quadraticTo(18f * s, 20f * s, 12f * s, 20f * s)
            quadraticTo(6f * s, 20f * s, 6f * s, 14f * s)
            cubicTo(6f * s, 10.6f * s, 9f * s, 7.5f * s, 12f * s, 3f * s)
            close()
        }
        DoodleIcon.CLOCK -> {
            circle(12f, 12f, 8.5f)
            line(12f, 7.5f, 12f, 12f)
            line(12f, 12f, 15f, 14f)
        }
        DoodleIcon.PLUS -> {
            line(12f, 5f, 12f, 19f)
            line(5f, 12f, 19f, 12f)
        }
        DoodleIcon.SHARE -> {
            line(12f, 16f, 12f, 4f)
            line(8f, 8f, 12f, 4f)
            line(12f, 4f, 16f, 8f)
            line(5f, 14f, 5f, 19f)
            line(5f, 19f, 19f, 19f)
            line(19f, 19f, 19f, 14f)
        }
        DoodleIcon.REFRESH -> {
            drawArc(
                color = c,
                startAngle = -60f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(4f * s, 4f * s),
                size = Size(16f * s, 16f * s),
                style = Stroke(w, cap = StrokeCap.Round)
            )
            line(20f, 4f, 20f, 10f)
            line(20f, 10f, 14f, 10f)
        }
        DoodleIcon.LOCK -> {
            box(5f, 10f, 14f, 10f, 2f)
            strokePath {
                moveTo(8f * s, 10f * s)
                quadraticTo(8f * s, 3f * s, 12f * s, 3f * s)
                quadraticTo(16f * s, 3f * s, 16f * s, 10f * s)
            }
        }
        DoodleIcon.SUPERMAN -> strokePath {
            moveTo(13f * s, 2.5f * s)
            lineTo(6f * s, 13f * s)
            lineTo(11f * s, 13f * s)
            lineTo(9f * s, 21.5f * s)
            lineTo(18f * s, 10f * s)
            lineTo(13f * s, 10f * s)
            close()
        }
    }
}
