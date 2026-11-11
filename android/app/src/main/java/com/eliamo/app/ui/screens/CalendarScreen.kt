package com.eliamo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.CheckInRules
import com.eliamo.app.data.DayInfo
import com.eliamo.app.data.RecordType
import com.eliamo.app.data.PoopRecord
import com.eliamo.app.data.dayInfoFor
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.*
import java.time.LocalDate

@Composable
fun CalendarScreen(
    todayBig: Int,
    todaySmall: Int,
    checkedIn: Set<String>,
    onCheckIn: () -> Unit,
    onNotice: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    var year by remember { mutableIntStateOf(today.year) }
    var month by remember { mutableIntStateOf(today.monthValue) }
    var selected by remember { mutableStateOf(today) }

    val first = CheckInRules.firstWeekday(year, month)
    val days = CheckInRules.daysInMonth(year, month)
    val rows = (first + days + 6) / 7

    fun shiftMonth(delta: Int) {
        var y = year
        var m = month + delta
        while (m < 1) { m += 12; y-- }
        while (m > 12) { m -= 12; y++ }
        year = y
        month = m
    }

    val monthInfo: List<DayInfo> = (1..days).map {
        dayInfoFor(LocalDate.of(year, month, it), today, todayBig, todaySmall, checkedIn)
    }
    val monthBig = monthInfo.sumOf { it.big }
    val monthSmall = monthInfo.sumOf { it.small }
    val monthChecked = monthInfo.count { it.checked }

    val sel = dayInfoFor(selected, today, todayBig, todaySmall, checkedIn)
    val isFuture = CheckInRules.isFuture(selected, today)
    val isPast = CheckInRules.isPast(selected, today)
    val isToday = CheckInRules.isToday(selected, today)
    val canCheck = CheckInRules.canCheckIn(selected, today, checkedIn)

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S2)
    ) {
        Spacer(Modifier.height(10.dp))
        Text("出货打卡日历", color = InkFaint, fontSize = 12.sp)
        Text("过去封存，未来锁上", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text("只有今天能盖章，翻月随便翻", color = InkSoft, fontSize = 13.sp)

        Spacer(Modifier.height(D.S4))
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(D.Touch)
                        .paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                        .clickable { shiftMonth(-1) },
                    contentAlignment = Alignment.Center
                ) { DoodleIconView(DoodleIcon.CHEVRON, size = 20.dp) }

                Column(
                    Modifier.weight(1f).clickable {
                        year = today.year
                        month = today.monthValue
                        selected = today
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${year}年 ${month}月", color = Ink, fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold)
                    Text("点标题回到今天", color = InkFaint, fontSize = 10.sp)
                }

                Box(
                    Modifier.size(D.Touch)
                        .paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                        .clickable { shiftMonth(1) },
                    contentAlignment = Alignment.Center
                ) {
                    DoodleIconView(
                        DoodleIcon.CHEVRON, size = 20.dp,
                        modifier = Modifier.graphicsLayer { rotationZ = 180f }
                    )
                }
            }

            Spacer(Modifier.height(D.S3))
            Row(Modifier.fillMaxWidth()) {
                listOf("日", "一", "二", "三", "四", "五", "六").forEach {
                    Text(it, color = InkFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(6.dp))

            for (w in 0 until rows) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    for (c in 0 until 7) {
                        val d = w * 7 + c - first + 1
                        if (d < 1 || d > days) {
                            Spacer(Modifier.weight(1f).aspectRatio(1f))
                        } else {
                            val date = LocalDate.of(year, month, d)
                            val info = dayInfoFor(date, today, todayBig, todaySmall, checkedIn)
                            val future = CheckInRules.isFuture(date, today)
                            val active = date == selected
                            val bg = when {
                                future -> PaperDeep
                                info.checked -> GrassSoft
                                info.total == 0 -> PaperCard
                                else -> PoopBrown.copy(
                                    alpha = (0.15f + info.total * 0.11f).coerceAtMost(0.9f)
                                )
                            }
                            Box(
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .paperFrame(
                                        background = bg,
                                        border = when {
                                            date == today -> Ink
                                            active -> StampRed
                                            else -> Line
                                        },
                                        borderWidth = if (date == today || active) 2.dp else 1.5.dp,
                                        radius = 10.dp
                                    )
                                    .clickable {
                                        selected = date
                                        when {
                                            future -> onNotice(
                                                "🔒", "还没到日子",
                                                "未来日期不能提前打卡，等它变成今天再来。"
                                            )
                                            CheckInRules.isPast(date, today) -> onNotice(
                                                "📦", "已封存",
                                                "这是历史记录，只读，不能修改。"
                                            )
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "$d", color = if (future) InkFaint else Ink, fontSize = 12.sp,
                                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Normal
                                    )
                                    when {
                                        future -> Text("🔒", fontSize = 9.sp)
                                        info.checked -> Text("✓", color = GrassGreen,
                                            fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                        info.total > 0 -> Text("${info.total} 次", color = InkSoft, fontSize = 9.sp)
                                    }
                                    if (date == today) Text("今", color = StampDeep, fontSize = 8.sp)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End) {
                Text("少 ", color = InkFaint, fontSize = 11.sp)
                listOf(PaperCard, GrassSoft, StampSoft, PoopBrown).forEach {
                    Box(Modifier.size(12.dp).background(it, RoundedCornerShape(4.dp)))
                    Spacer(Modifier.width(4.dp))
                }
                Text(" 多　🔒 未来", color = InkFaint, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(D.S3))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            listOf("$monthBig" to "本月开大", "$monthSmall" to "本月开小", "$monthChecked" to "打卡天数")
                .forEach { (n, l) ->
                    DoodleCard(modifier = Modifier.weight(1f)) {
                        Text(n, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text(l, color = InkFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    }
                }
        }

        SectionTitle(
            "${selected.monthValue}月${selected.dayOfMonth}日 · " +
                    if (isFuture) "未来" else if (isPast) "历史归档" else "今天",
            "共 ${sel.total} 次"
        )
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    when {
                        isFuture -> "状态：未开放"
                        isPast -> "状态：只读封存"
                        sel.checked -> "状态：今日已打卡"
                        else -> "状态：等待今日打卡"
                    },
                    color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                when {
                    isFuture -> StampChip("未开放")
                    isPast -> StampChip("已封存")
                    sel.checked -> StampChip("已盖章")
                }
            }
            Spacer(Modifier.height(D.S2))
            when {
                isFuture -> {
                    EmptyState("🔒", "还没到日子，\n未来不能提前打卡。")
                    Text("规则：只有「今天」可以打卡，未来日期一律锁定。",
                        color = InkFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
                isPast -> {
                    if (sel.total == 0) {
                        EmptyState("🍃", "这天很安静，\n什么也没发生。")
                    } else {
                        val rowsForDay = listOf(
                            Triple("08:1${selected.dayOfMonth % 9}", "💩", "开大 · 香蕉船（4 型）"),
                            Triple("12:4${selected.dayOfMonth % 6}", "💧", "开小 · 常规"),
                            Triple("20:0${selected.dayOfMonth % 9}", "💧", "开小 · 有点多")
                        )
                        repeat(minOf(sel.total, 4)) { i ->
                            val r = rowsForDay[i % 3]
                            LogRow(
                                PoopRecord(
                                    i.toLong(), r.first, RecordType.SMALL, r.third,
                                    "历史归档 · 只读", r.second
                                )
                            )
                        }
                    }
                    Text("历史记录只读，不能修改、补卡或删除。",
                        color = InkFaint, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
                else -> {
                    Text("今天已记录 ${sel.total} 次（开大 ${sel.big} / 开小 ${sel.small}）",
                        color = InkSoft, fontSize = 13.sp)
                    Spacer(Modifier.height(D.S3))
                    if (canCheck) {
                        SmallButton("✅ 今日打卡 · 盖个章", onCheckIn, variant = BtnVariant.SHADOW)
                        Spacer(Modifier.height(D.S2))
                        Text("一天只能盖一次章；过去封存，未来锁定。",
                            color = InkFaint, fontSize = 11.sp)
                    } else {
                        Text("✓ 今日已打卡，明天再来。", color = GrassGreen,
                            fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(Modifier.height(D.S6))
    }
}
