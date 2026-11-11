package com.eliamo.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.AmmoList
import com.eliamo.app.data.HitQuips
import com.eliamo.app.data.NameValidator
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val DEFAULT_NAME = "讨厌的周一"

private data class Splat(val emoji: String, val x: Float, val y: Float, val rot: Float)

private fun trajectory(kind: String, t: Float): Pair<Float, Float> {
    val x0 = 45f
    val y0 = 190f
    val x1 = 250f
    val y1 = 92f
    val lift = when (kind) {
        "heavy" -> 10f
        "fast" -> 130f
        "wobble" -> 70f
        "straight" -> 145f
        else -> 55f
    }
    val cx = (x0 + x1) / 2f
    val u = 1f - t
    var x = u * u * x0 + 2f * u * t * cx + t * t * x1
    val y = u * u * y0 + 2f * u * t * lift + t * t * y1
    if (kind == "wobble") x += sin(t * 18f) * 10f
    return x to y
}

@Composable
private fun DollCanvas(hit: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        drawLine(Ink, Offset(cx - 13f, h * 0.72f), Offset(cx - 18f, h * 0.94f), 6f, StrokeCap.Round)
        drawLine(Ink, Offset(cx + 13f, h * 0.72f), Offset(cx + 18f, h * 0.94f), 6f, StrokeCap.Round)
        drawLine(Ink, Offset(cx - 28f, h * 0.49f), Offset(cx - 50f, h * 0.63f), 6f, StrokeCap.Round)
        drawLine(Ink, Offset(cx + 28f, h * 0.49f), Offset(cx + 50f, h * 0.63f), 6f, StrokeCap.Round)

        drawRoundRect(
            TapeYellow,
            topLeft = Offset(cx - 30f, h * 0.42f),
            size = Size(60f, h * 0.32f),
            cornerRadius = CornerRadius(14f, 14f)
        )
        drawRoundRect(
            Ink,
            topLeft = Offset(cx - 30f, h * 0.42f),
            size = Size(60f, h * 0.32f),
            cornerRadius = CornerRadius(14f, 14f),
            style = Stroke(3f)
        )

        drawCircle(Color(0xFFFFE0B2.toInt()), h * 0.19f, Offset(cx, h * 0.26f))
        drawCircle(Ink, h * 0.19f, Offset(cx, h * 0.26f), style = Stroke(3f))

        if (hit) {
            val e = 4f
            drawLine(Ink, Offset(cx - 14f - e, h * 0.23f - e), Offset(cx - 14f + e, h * 0.23f + e), 3f, StrokeCap.Round)
            drawLine(Ink, Offset(cx - 14f + e, h * 0.23f - e), Offset(cx - 14f - e, h * 0.23f + e), 3f, StrokeCap.Round)
            drawLine(Ink, Offset(cx + 14f - e, h * 0.23f - e), Offset(cx + 14f + e, h * 0.23f + e), 3f, StrokeCap.Round)
            drawLine(Ink, Offset(cx + 14f + e, h * 0.23f - e), Offset(cx + 14f - e, h * 0.23f + e), 3f, StrokeCap.Round)
            val mouth = Path().apply {
                moveTo(cx - 13f, h * 0.32f)
                quadraticTo(cx - 6f, h * 0.37f, cx, h * 0.32f)
                quadraticTo(cx + 6f, h * 0.27f, cx + 13f, h * 0.32f)
            }
            drawPath(mouth, Ink, style = Stroke(3f, cap = StrokeCap.Round))
        } else {
            drawCircle(Ink, 3.5f, Offset(cx - 12f, h * 0.23f))
            drawCircle(Ink, 3.5f, Offset(cx + 12f, h * 0.23f))
            drawArc(
                Ink, 20f, 140f, false,
                topLeft = Offset(cx - 13f, h * 0.27f),
                size = Size(26f, 18f),
                style = Stroke(3f, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun HitParticles(trigger: Int, color: Color) {
    if (trigger <= 0) return
    val anim = remember(trigger) { Animatable(0f) }
    LaunchedEffect(trigger) {
        anim.animateTo(1f, tween(520))
    }
    val p = anim.value
    if (p >= 1f) return
    Canvas(Modifier.fillMaxSize()) {
        val cx = size.width * 0.72f
        val cy = size.height * 0.38f
        for (i in 0 until 16) {
            val angle = i * 137.5f * (Math.PI.toFloat() / 180f)
            val r = 92f * p
            drawCircle(
                color.copy(alpha = (1f - p).coerceAtLeast(0f)),
                3f + (i % 4) * 2f,
                Offset(cx + cos(angle) * r, cy + sin(angle) * r)
            )
        }
    }
}

@Composable
fun GameScreen(
    onBack: () -> Unit,
    onFire: (Int) -> Unit,
    onNotice: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(DEFAULT_NAME) }
    var nameInput by remember { mutableStateOf(DEFAULT_NAME) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var ammoId by remember { mutableIntStateOf(4) }
    var shots by remember { mutableIntStateOf(0) }
    var hits by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var lastQuip by remember { mutableStateOf("") }
    var flying by remember { mutableStateOf(false) }
    var hitKey by remember { mutableIntStateOf(0) }
    val splats = remember { mutableStateListOf<Splat>() }
    val flight = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val ammo = AmmoList.first { it.id == ammoId }

    fun applyName() {
        val err = NameValidator.validate(nameInput)
        if (err != null) {
            nameError = err
            onNotice("⚠️", "名字不合格", err)
            return
        }
        name = nameInput.trim()
        nameError = null
        onNotice("🪧", "人偶已就位", "「$name」站好了，准备接招。")
    }

    fun fire() {
        if (flying) return
        val firedAmmo = ammo
        shots++
        scope.launch {
            flying = true
            flight.snapTo(0f)
            flight.animateTo(1f, tween(520, easing = LinearEasing))
            flying = false
            hits++
            combo++
            score += firedAmmo.power * 10 + combo * 2
            lastQuip = HitQuips.random()
            splats.add(
                Splat(
                    firedAmmo.emoji,
                    kotlin.random.Random.Default.nextInt(8, 78).toFloat(),
                    kotlin.random.Random.Default.nextInt(12, 102).toFloat(),
                    kotlin.random.Random.Default.nextInt(-30, 31).toFloat()
                )
            )
            if (splats.size > 8) splats.removeAt(0)
            hitKey++
            onFire(firedAmmo.id)
            onNotice("💥", "命中！", lastQuip)
        }
    }

    fun reset() {
        name = DEFAULT_NAME
        nameInput = DEFAULT_NAME
        nameError = null
        ammoId = 4
        shots = 0
        hits = 0
        combo = 0
        score = 0
        lastQuip = ""
        splats.clear()
        flying = false
        scope.launch { flight.snapTo(0f) }
        onNotice("🔄", "对局已重置", "人偶擦干净了，累计战绩仍保留给荣誉墙。")
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S2)
    ) {
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("战斗模式 · 第 5 号入口", color = InkFaint, fontSize = 12.sp)
                Text("屙了么超人", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("把不开心糊到人偶脸上", color = InkSoft, fontSize = 13.sp)
            }
            Box(
                Modifier.size(D.Touch)
                    .paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { DoodleIconView(DoodleIcon.CHEVRON, size = 22.dp) }
        }

        Spacer(Modifier.height(D.S4))
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            Text("人偶名称 · 1–12 字", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(D.S2))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it; nameError = null },
                    singleLine = true,
                    textStyle = TextStyle(color = Ink, fontSize = 14.sp),
                    modifier = Modifier
                        .weight(1f)
                        .height(D.Touch)
                        .paperFrame(background = PaperCard, border = InkSoft, radius = D.Sm)
                        .padding(horizontal = D.S3, vertical = 10.dp)
                )
                Spacer(Modifier.width(D.S2))
                Box(
                    Modifier.height(D.Touch)
                        .paperFrame(
                            background = TapeSoft, border = Ink, dashed = false,
                            borderWidth = 2.dp, radius = D.Sm
                        )
                        .clickable { applyName() }
                        .padding(horizontal = D.S4),
                    contentAlignment = Alignment.Center
                ) {
                    Text("立人偶", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (nameError != null) {
                Spacer(Modifier.height(D.S2))
                Text("⚠️ $nameError", color = StampDeep, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(D.S2))
            Text("当前人偶：$name", color = InkSoft, fontSize = 12.sp)
        }

        Spacer(Modifier.height(D.S3))
        Box(
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .paperFrame(
                    background = GrassSoft, border = Ink, dashed = false,
                    borderWidth = 2.dp, radius = D.Card
                )
        ) {
            Canvas(Modifier.matchParentSize()) {
                drawLine(
                    Ink.copy(alpha = 0.18f),
                    Offset(0f, size.height * 0.82f),
                    Offset(size.width, size.height * 0.82f),
                    2f
                )
                drawCircle(Color.White.copy(alpha = 0.65f), 22f, Offset(60f, 40f))
                drawCircle(Color.White.copy(alpha = 0.65f), 16f, Offset(88f, 36f))
            }

            Column(
                Modifier.align(Alignment.CenterEnd).padding(end = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.size(120.dp, 150.dp)) {
                    DollCanvas(hit = hitKey > 0 && !flying, modifier = Modifier.matchParentSize())
                    splats.forEach { s ->
                        Text(
                            s.emoji, fontSize = 22.sp,
                            modifier = Modifier
                                .offset(x = s.x.dp, y = s.y.dp)
                                .graphicsLayer { rotationZ = s.rot }
                        )
                    }
                }
                Box(
                    Modifier
                        .paperFrame(
                            background = PaperCard, border = Ink, dashed = false,
                            borderWidth = 1.5.dp, radius = D.Sm
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(name, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        maxLines = 1)
                }
            }

            Box(
                Modifier.align(Alignment.BottomStart).padding(start = 20.dp, bottom = 14.dp)
            ) {
                Box(
                    Modifier.size(52.dp)
                        .paperFrame(
                            background = StampSoft, border = Ink, dashed = false,
                            borderWidth = 2.dp, radius = 999.dp
                        ),
                    contentAlignment = Alignment.Center
                ) { Text("🦸", fontSize = 24.sp) }
            }

            if (flying) {
                val p = flight.value
                val (x, y) = trajectory(ammo.flight, p)
                Text(
                    ammo.emoji, fontSize = 30.sp,
                    modifier = Modifier
                        .offset(x = x.dp, y = y.dp)
                        .graphicsLayer { rotationZ = p * 720f }
                )
            }

            if (!flying && hitKey > 0) {
                Text(
                    "啪叽！", color = StampDeep, fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                        .graphicsLayer { rotationZ = -8f }
                )
            }

            if (hitKey > 0) HitParticles(hitKey, Color(ammo.tint.toInt()))
        }

        Spacer(Modifier.height(D.S3))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            listOf("$shots" to "发射", "$hits" to "命中", "$combo" to "连击", "$score" to "得分")
                .forEach { (n, l) ->
                    DoodleCard(modifier = Modifier.weight(1f)) {
                        Text(n, color = Ink, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text(l, color = InkFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    }
                }
        }

        Spacer(Modifier.height(D.S3))
        Text("选择弹体 · 威力 " + "★".repeat(ammo.power), color = Ink, fontSize = 13.sp,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(D.S2))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            AmmoList.forEach { a ->
                DoodleChip(a.name, ammoId == a.id, { ammoId = a.id }, emoji = a.emoji,
                    vertical = true)
            }
        }
        Spacer(Modifier.height(D.S2))
        Text(ammo.quip, color = InkSoft, fontSize = 12.sp)

        Spacer(Modifier.height(D.S3))
        DoodleButton(
            if (flying) "飞行中…" else "发射！",
            if (flying) "这一坨正在赶路" else "把「${ammo.name}」糊到人偶脸上",
            ammo.emoji,
            BtnVariant.SOLID,
            { fire() },
            trailing = DoodleIcon.SUPERMAN
        )
        Spacer(Modifier.height(D.S2))
        SmallButton("🔄 重置对局", { reset() }, icon = DoodleIcon.REFRESH,
            variant = BtnVariant.BORDERED)
        Spacer(Modifier.height(D.S2))
        SmallButton("收工返回原页面", onBack, icon = DoodleIcon.CHEVRON,
            variant = BtnVariant.LIGHT)

        if (lastQuip.isNotEmpty()) {
            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth(), background = TapeSoft, tilt = 0.5f) {
                Text("现场播报", color = InkFaint, fontSize = 11.sp)
                Spacer(Modifier.height(D.S2))
                Text(lastQuip, color = Ink, fontSize = 15.sp, lineHeight = 23.sp)
            }
        }
        Spacer(Modifier.height(D.S4))
        Text("规则：名称需 1–12 字、不含换行与尖括号；可重复发射，重置只清本局；日历里的未来与历史记录不受影响。",
            color = InkFaint, fontSize = 11.sp, lineHeight = 17.sp)
        Spacer(Modifier.height(D.S6))
    }
}
