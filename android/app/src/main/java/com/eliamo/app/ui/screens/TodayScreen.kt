package com.eliamo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.PoopRecord
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.*
import java.util.Locale

@Composable
private fun CounterCard(label: String, value: Int, unit: String, note: String, modifier: Modifier = Modifier) {
    DoodleCard(modifier = modifier) {
        Text(label, color = InkSoft, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$value", color = Ink, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Text(unit, color = InkSoft, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
        }
        Box(
            Modifier
                .padding(top = 6.dp)
                .background(GrassSoft, CircleShape)
                .padding(horizontal = 7.dp, vertical = 2.dp)
        ) { Text(note, color = Color(0xFF4F6B42), fontSize = 11.sp) }
    }
}

@Composable
fun TodayScreen(
    big: Int,
    small: Int,
    records: List<PoopRecord>,
    onRecordBig: () -> Unit,
    onRecordSmall: () -> Unit,
    onSettings: () -> Unit,
    onShake: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S2)
    ) {
        Row(Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("11月11日 · 周三 · 立冬后第 4 天", color = InkFaint, fontSize = 12.sp)
                Spacer(Modifier.height(2.dp))
                Text("早上好，掌柜的", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("今天也要顺顺畅畅的呀", color = InkSoft, fontSize = 13.sp)
            }
            Box(
                Modifier
                    .size(D.Touch)
                    .paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                    .clickable { onSettings() },
                contentAlignment = Alignment.Center
            ) { DoodleIconView(DoodleIcon.SETTINGS, size = 24.dp) }
        }

        Spacer(Modifier.height(D.S4))
        DoodleCard(modifier = Modifier.fillMaxWidth(), tilt = -0.6f, withTape = true) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("今日手账 · 第 6 页", color = InkFaint, fontSize = 12.sp, modifier = Modifier.weight(1f))
                StampChip("已卸货")
            }
            Spacer(Modifier.height(10.dp))
            val quote = if (big == 0 && small == 0) {
                "今天还没开张，\n肠胃在摸鱼。"
            } else {
                "今日已卸货 $big 坨，\n地球轻了约 ${String.format(Locale.US, "%.1f", big * 0.2)}kg。"
            }
            Text(quote, color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold, lineHeight = 31.sp)
            Text(
                "—— 屙了么 · 自动记录", color = InkFaint, fontSize = 12.sp,
                textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(D.S3))
        Row(Modifier.fillMaxWidth()) {
            CounterCard("💩 开大", big, "坨", "比昨天 +1", Modifier.weight(1f))
            Spacer(Modifier.width(D.S3))
            CounterCard("💧 开小", small, "次", "补水充足", Modifier.weight(1f))
        }

        Spacer(Modifier.height(D.S4))
        DoodleButton(
            "开大 · 记一坨", "选形态、盖章、领一句毒鸡汤", "💩",
            BtnVariant.SOLID, onRecordBig, trailing = DoodleIcon.CHEVRON
        )
        Spacer(Modifier.height(D.S3))
        DoodleButton(
            "开小 · 放个水", "3 秒搞定，不耽误事", "💧",
            BtnVariant.FADED, onRecordSmall, trailing = DoodleIcon.PLUS
        )

        Spacer(Modifier.height(D.S3))
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("连续 6 天有出货", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("再坚持 1 天解锁「七日不断更」", color = InkFaint, fontSize = 11.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    repeat(7) { i ->
                        Box(
                            Modifier
                                .size(14.dp)
                                .background(if (i < 6) StampRed else Color.Transparent, CircleShape)
                                .border(1.5.dp, Ink, CircleShape)
                        )
                    }
                }
            }
        }

        SectionTitle("今日流水", "清空演示", onClear)
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            if (records.isEmpty()) {
                EmptyState("📄", "今天还是一张白纸，\n等你落笔。")
            } else {
                records.forEach { rec ->
                    LogRow(rec)
                    if (rec !== records.last()) {
                        Box(Modifier.fillMaxWidth().height(1.dp).background(Line))
                    }
                }
            }
        }

        Spacer(Modifier.height(D.S4))
        SmallButton("🫨 摇一摇手机，听个响", onShake, variant = BtnVariant.LIGHT)
        Spacer(Modifier.height(D.S6))
    }
}
