package com.eliamo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.Amounts
import com.eliamo.app.data.Minutes
import com.eliamo.app.data.Moods
import com.eliamo.app.data.PoopRecord
import com.eliamo.app.data.RecordType
import com.eliamo.app.data.Shapes
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun RecordSheet(
    initialType: RecordType,
    onClose: () -> Unit,
    onSubmit: (RecordType, PoopRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var type by remember { mutableStateOf(initialType) }
    var shape by remember { mutableIntStateOf(4) }
    var amount by remember { mutableIntStateOf(3) }
    var minute by remember { mutableIntStateOf(1) }
    var mood by remember { mutableIntStateOf(0) }
    var note by remember { mutableStateOf("") }
    var slideState by remember { mutableStateOf("idle") }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(slideState) {
        if (slideState == "loading") {
            delay(520)
            val s = Shapes.getOrNull(shape - 1)
            val desc = Minutes[minute] + " · " + Moods[mood].name +
                    if (note.isBlank()) "" else " · $note"
            val rec = if (type == RecordType.BIG) {
                PoopRecord(
                    System.currentTimeMillis(), "09:41", RecordType.BIG,
                    "开大 · ${s?.name ?: "未分型"}（$shape 型）", desc, "💩"
                )
            } else {
                PoopRecord(
                    System.currentTimeMillis(), "09:41", RecordType.SMALL,
                    "开小 · ${Amounts[amount - 1]}", desc, "💧"
                )
            }
            onSubmit(type, rec)
            onClose()
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .heightIn(max = 720.dp)
            .paperFrame(
                background = Paper, border = LineSolid, dashed = true,
                borderWidth = 2.dp, radius = 28.dp
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S3)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(Modifier.size(width = 44.dp, height = 5.dp)
                .background(InkFaint, RoundedCornerShape(4.dp)))
        }
        Spacer(Modifier.height(D.S3))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (type == RecordType.BIG) "记一笔 · 开大" else "记一笔 · 开小",
                color = Ink, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
            Box(
                Modifier.size(D.Touch).paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                    .clickable { onClose() }, contentAlignment = Alignment.Center
            ) { DoodleIconView(DoodleIcon.CLOSE, size = 22.dp) }
        }

        FieldLabel("类型", "选错了还能改")
        Row(horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            DoodleChip("💩 开大", type == RecordType.BIG, {
                type = RecordType.BIG
                shape = 4
                slideState = "idle"
                error = false
            })
            DoodleChip("💧 开小", type == RecordType.SMALL, {
                type = RecordType.SMALL
                shape = 0
                slideState = "idle"
                error = false
            })
        }

        if (type == RecordType.BIG) {
            FieldLabel("形态 · 布里斯托分型", if (shape > 0) "第 $shape 型 · ${Shapes[shape - 1].name}" else "还没选形态")
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(D.S2)) {
                Shapes.forEach { s ->
                    DoodleChip(s.name, shape == s.id, {
                        shape = if (shape == s.id) 0 else s.id
                        error = false
                        slideState = "idle"
                    }, emoji = s.emoji, vertical = true)
                }
            }

            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("份量", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f))
                    Text(Amounts[amount - 1], color = Ink, fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold)
                }
                Slider(
                    value = amount.toFloat(),
                    onValueChange = { amount = it.roundToInt().coerceIn(1, 5) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = TapeYellow,
                        activeTrackColor = PoopBrown,
                        inactiveTrackColor = PaperSunk
                    )
                )
                Row(Modifier.fillMaxWidth()) {
                    Text("一点点", color = InkFaint, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    Text("惊艳全场", color = InkFaint, fontSize = 11.sp)
                }
            }
        }

        FieldLabel("耗时", "诚实一点")
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            Minutes.forEachIndexed { i, m ->
                DoodleChip(m, minute == i, { minute = i })
            }
        }

        FieldLabel("此刻心情", "Bencho · emoji reactions")
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            Moods.forEachIndexed { i, m ->
                DoodleChip(m.name, mood == i, { mood = i }, emoji = m.emoji)
            }
        }

        FieldLabel("备注", "可不填")
        BasicTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .paperFrame(background = PaperCard, border = InkSoft, radius = D.Sm)
                .padding(D.S4),
            textStyle = TextStyle(color = Ink, fontSize = 14.sp),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.TopStart) {
                    if (note.isEmpty()) {
                        Text("今天想说点啥…比如：这坨值得纪念", color = InkFaint, fontSize = 14.sp)
                    }
                    inner()
                }
            }
        )

        Spacer(Modifier.height(D.S4))
        SlideToConfirm(
            label = when (slideState) {
                "loading" -> "正在盖章…"
                "ok" -> "已出货 ✓"
                else -> "向右滑动 → 盖章出货"
            },
            state = slideState,
            onConfirm = {
                if (type == RecordType.BIG && shape <= 0) {
                    error = true
                    slideState = "err"
                } else {
                    error = false
                    slideState = "loading"
                }
            }
        )
        if (error) {
            Spacer(Modifier.height(D.S2))
            Text("先选个形态嘛，不然没法盖章 😅", color = StampDeep, fontSize = 12.sp,
                fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(D.S2))
        Text("Bencho · Slide to confirm / Watermelon · Morphing button",
            color = InkFaint, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(D.S4))
    }
}

@Composable
private fun FieldLabel(text: String, hint: String) {
    Row(Modifier.fillMaxWidth().padding(top = D.S4, bottom = D.S2),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f))
        Text(hint, color = InkFaint, fontSize = 11.sp)
    }
}
