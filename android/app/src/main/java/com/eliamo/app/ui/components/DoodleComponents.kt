package com.eliamo.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.PoopRecord
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.D
import com.eliamo.app.ui.theme.GrassGreen
import com.eliamo.app.ui.theme.GrassSoft
import com.eliamo.app.ui.theme.Ink
import com.eliamo.app.ui.theme.InkFaint
import com.eliamo.app.ui.theme.InkSoft
import com.eliamo.app.ui.theme.Line
import com.eliamo.app.ui.theme.LineSolid
import com.eliamo.app.ui.theme.Mint
import com.eliamo.app.ui.theme.Paper
import com.eliamo.app.ui.theme.PaperCard
import com.eliamo.app.ui.theme.PaperDeep
import com.eliamo.app.ui.theme.PaperSunk
import com.eliamo.app.ui.theme.PeeGold
import com.eliamo.app.ui.theme.PoopBrown
import com.eliamo.app.ui.theme.StampRed
import com.eliamo.app.ui.theme.StampSoft
import com.eliamo.app.ui.theme.StampDeep
import com.eliamo.app.ui.theme.TapeSoft
import com.eliamo.app.ui.theme.TapeYellow
import androidx.compose.material3.Text
import kotlin.math.roundToInt

// 手账贴纸框：实色硬阴影 + 纸面 + 虚线/实线描边
fun Modifier.paperFrame(
    background: Color = PaperCard,
    border: Color = Line,
    dashed: Boolean = true,
    borderWidth: Dp = 1.5.dp,
    radius: Dp = D.Card,
    hardShadow: Boolean = false
): Modifier = this.drawBehind {
    val r = radius.toPx()
    val bw = borderWidth.toPx()
    if (hardShadow) {
        val ox = 4.dp.toPx()
        val oy = 5.dp.toPx()
        drawRoundRect(
            Ink,
            topLeft = Offset(ox, oy),
            size = Size(size.width - ox, size.height - oy),
            cornerRadius = CornerRadius(r, r)
        )
    }
    drawRoundRect(
        background,
        topLeft = Offset.Zero,
        size = Size(size.width, size.height),
        cornerRadius = CornerRadius(r, r)
    )
    drawRoundRect(
        border,
        topLeft = Offset.Zero,
        size = Size(size.width, size.height),
        cornerRadius = CornerRadius(r, r),
        style = Stroke(
            width = bw,
            pathEffect = if (dashed) PathEffect.dashPathEffect(
                floatArrayOf(9.dp.toPx(), 7.dp.toPx())
            ) else null
        )
    )
}

@Composable
fun PaperBackground() {
    Canvas(Modifier.fillMaxSize()) {
        val step = 34.dp.toPx()
        var y = 0f
        while (y < size.height) {
            drawLine(Ink.copy(alpha = 0.05f), Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
            y += step
        }
    }
}

@Composable
fun Tape(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(width = 66.dp, height = 19.dp)
            .graphicsLayer { rotationZ = -7f }
            .drawBehind {
                drawRoundRect(
                    TapeYellow.copy(alpha = 0.62f),
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
            }
    )
}

@Composable
fun DoodleCard(
    modifier: Modifier = Modifier,
    tilt: Float = 0f,
    background: Color = PaperCard,
    withTape: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier
            .graphicsLayer { rotationZ = tilt }
            .paperFrame(background = background)
    ) {
        if (withTape) {
            Tape(Modifier.align(Alignment.TopEnd).offset(x = (-24).dp, y = (-10).dp))
        }
        Column(Modifier.padding(D.S4), content = content)
    }
}

@Composable
fun StampChip(text: String) {
    Box(
        Modifier
            .graphicsLayer { rotationZ = -6f }
            .paperFrame(
                background = Color.Transparent,
                border = StampRed,
                dashed = false,
                borderWidth = 2.dp,
                radius = 10.dp
            )
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(text, color = StampDeep, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
    }
}

// HeroUI 的按钮变体骨架：solid / faded / bordered / light / shadow
enum class BtnVariant { SOLID, FADED, BORDERED, LIGHT, SHADOW }

@Composable
fun DoodleButton(
    title: String,
    sub: String,
    emoji: String,
    variant: BtnVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: DoodleIcon = DoodleIcon.CHEVRON
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, tween(160), label = "btn-scale")

    val bg = when (variant) {
        BtnVariant.SOLID -> StampRed
        BtnVariant.FADED -> PaperCard
        BtnVariant.BORDERED -> Color.Transparent
        BtnVariant.LIGHT -> TapeSoft
        BtnVariant.SHADOW -> GrassGreen
    }
    val border = when (variant) {
        BtnVariant.SOLID -> Ink
        BtnVariant.FADED -> InkSoft
        BtnVariant.BORDERED -> Ink
        BtnVariant.LIGHT -> Color.Transparent
        BtnVariant.SHADOW -> Ink
    }
    val fg = if (variant == BtnVariant.SOLID || variant == BtnVariant.SHADOW) PaperCard else Ink

    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .paperFrame(
                background = bg,
                border = border,
                dashed = variant == BtnVariant.FADED,
                borderWidth = 2.dp,
                radius = D.Button,
                hardShadow = variant == BtnVariant.SOLID || variant == BtnVariant.SHADOW
            )
            .clickable(interactionSource = interaction, indication = null) { onClick() }
            .padding(horizontal = D.S4, vertical = D.S3)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.width(D.S3))
            Column(Modifier.weight(1f)) {
                Text(
                    title, color = fg, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    sub, color = fg.copy(alpha = 0.8f), fontSize = 12.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            DoodleIconView(trailing, tint = fg, size = 20.dp)
        }
    }
}

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BtnVariant = BtnVariant.LIGHT,
    icon: DoodleIcon? = null
) {
    val bg = when (variant) {
        BtnVariant.SOLID -> StampRed
        BtnVariant.LIGHT -> TapeSoft
        BtnVariant.BORDERED -> Color.Transparent
        else -> PaperCard
    }
    val fg = if (variant == BtnVariant.SOLID) PaperCard else Ink
    Box(
        modifier
            .fillMaxWidth()
            .heightIn(min = D.Touch)
            .paperFrame(background = bg, border = InkSoft, dashed = variant == BtnVariant.FADED,
                borderWidth = 2.dp, radius = D.Sm)
            .clickable { onClick() }
            .padding(horizontal = D.S4, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                DoodleIconView(icon, tint = fg, size = 18.dp)
                Spacer(Modifier.width(D.S2))
            }
            Text(text, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DoodleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String? = null,
    vertical: Boolean = false
) {
    val fg = if (selected) PaperCard else Ink
    Box(
        modifier
            .heightIn(min = D.Touch)
            .paperFrame(
                background = if (selected) StampRed else PaperCard,
                border = if (selected) Ink else LineSolid,
                dashed = !selected,
                borderWidth = if (selected) 2.dp else 1.5.dp,
                radius = if (vertical) 14.dp else 999.dp
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (vertical) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (emoji != null) Text(emoji, fontSize = 24.sp)
                Text(text, color = fg, fontSize = 10.sp, maxLines = 1)
            }
        } else {
            Text(
                (if (emoji != null) "$emoji " else "") + text,
                color = fg, fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DoodleSwitch(on: Boolean, onToggle: () -> Unit) {
    val x by animateDpAsState(if (on) 20.dp else 0.dp, tween(200), label = "switch")
    Box(
        Modifier
            .size(width = 50.dp, height = D.Touch)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(width = 50.dp, height = 30.dp)
                .paperFrame(
                    background = if (on) GrassGreen else PaperSunk,
                    border = if (on) Ink else InkSoft,
                    dashed = false,
                    borderWidth = 2.dp,
                    radius = 999.dp
                )
        ) {
            Box(
                Modifier
                    .offset(x = 2.dp + x, y = 2.dp)
                    .size(22.dp)
                    .paperFrame(
                        background = PaperCard,
                        border = InkSoft,
                        dashed = false,
                        borderWidth = 1.5.dp,
                        radius = 999.dp
                    )
            )
        }
    }
}

// Bencho · Slide to confirm
@Composable
fun SlideToConfirm(
    label: String,
    state: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val thumbPx = with(density) { 58.dp.toPx() }
    var trackW by remember { mutableFloatStateOf(0f) }
    var x by remember { mutableFloatStateOf(0f) }
    val maxX = (trackW - thumbPx).coerceAtLeast(0f)

    LaunchedEffect(state) {
        if (state == "err" || state == "idle") x = 0f
    }

    Box(
        modifier
            .fillMaxWidth()
            .height(62.dp)
            .onSizeChanged { trackW = it.width.toFloat() }
            .paperFrame(
                background = if (state == "ok") GrassSoft else PaperDeep,
                border = if (state == "err") StampRed else InkSoft,
                dashed = true,
                borderWidth = 2.dp,
                radius = 999.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = InkSoft, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(x.roundToInt(), 0) }
                .size(50.dp)
                .paperFrame(
                    background = StampRed, border = Ink, dashed = false,
                    borderWidth = 2.dp, radius = 999.dp
                )
                .pointerInput(trackW, state) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (maxX > 0f && x > maxX * 0.6f) {
                                x = maxX
                                onConfirm()
                            } else {
                                x = 0f
                            }
                        },
                        onHorizontalDrag = { _, delta ->
                            x = (x + delta).coerceIn(0f, maxX)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            DoodleIconView(DoodleIcon.CHEVRON, tint = PaperCard, size = 22.dp)
        }
    }
}

// Bencho · Particles 彩纸
@Composable
fun ConfettiBurst(trigger: Int, onDone: () -> Unit) {
    if (trigger <= 0) return
    val anim = remember(trigger) { Animatable(0f) }
    LaunchedEffect(trigger) {
        anim.animateTo(1f, tween(900))
        onDone()
    }
    val p = anim.value
    val colors = listOf(StampRed, TapeYellow, GrassGreen, PoopBrown, Mint, PeeGold)
    Canvas(Modifier.fillMaxSize()) {
        for (i in 0 until 42) {
            val vx = ((i % 9) - 4) * 7f
            val vy = -((i % 7) * 2f + 4f)
            val px = size.width * 0.5f + vx * 8f * p
            val py = size.height * 0.42f + vy * 9f * p + 150f * p * p
            drawRect(
                colors[i % colors.size],
                Offset(px, py),
                Size(4f + (i % 5) * 2f, 7f + (i % 4) * 2f)
            )
        }
    }
}

@Composable
fun ToastCard(message: Pair<String, String>?, emoji: String) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(200)),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(tween(200))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .paperFrame(
                    background = PaperCard, border = Ink, dashed = false,
                    borderWidth = 2.dp, radius = 18.dp, hardShadow = true
                )
                .padding(D.S4)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(emoji, fontSize = 26.sp)
                Spacer(Modifier.width(D.S3))
                Column {
                    Text(message?.first ?: "", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        message?.second ?: "", color = InkSoft, fontSize = 12.sp, lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(top = D.S5, bottom = D.S2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
        if (action != null) {
            Box(Modifier.heightIn(min = D.Touch).clickable { onAction?.invoke() }, contentAlignment = Alignment.Center) {
                Text(action, color = InkFaint, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun LogRow(record: PoopRecord) {
    Row(Modifier.fillMaxWidth().padding(vertical = D.S3), verticalAlignment = Alignment.Top) {
        Text(record.time, color = InkFaint, fontSize = 12.sp, modifier = Modifier.width(44.dp))
        Box(
            Modifier
                .size(34.dp)
                .paperFrame(background = PaperDeep, border = Line, radius = 10.dp),
            contentAlignment = Alignment.Center
        ) { Text(record.emoji, fontSize = 18.sp) }
        Spacer(Modifier.width(D.S3))
        Column(Modifier.weight(1f)) {
            Text(record.title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(record.desc, color = InkSoft, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
fun EmptyState(emoji: String, text: String) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 44.sp)
        Spacer(Modifier.height(10.dp))
        Text(text, color = InkFaint, fontSize = 13.sp, lineHeight = 21.sp)
    }
}

@Composable
fun SkeletonBlock(height: Dp = 22.dp, widthFraction: Float = 1f) {
    Box(
        Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .paperFrame(background = PaperSunk, border = Line, radius = D.Sm)
    )
}

@Composable
fun ErrorCard(title: String, desc: String, onRetry: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .paperFrame(background = StampSoft, border = StampRed, borderWidth = 1.5.dp, radius = D.Sm)
            .padding(D.S4)
    ) {
        Column {
            Text(title, color = StampDeep, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = InkSoft, fontSize = 11.sp, lineHeight = 17.sp)
            Spacer(Modifier.height(D.S2))
            SmallButton("再试一次", onRetry)
        }
    }
}
