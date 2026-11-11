package com.eliamo.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.Badge
import com.eliamo.app.data.GameStats
import com.eliamo.app.data.PoopRecord
import com.eliamo.app.data.Quips
import com.eliamo.app.data.RecordStore
import com.eliamo.app.data.RecordType
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.screens.AchievementsScreen
import com.eliamo.app.ui.screens.CalendarScreen
import com.eliamo.app.ui.screens.GameScreen
import com.eliamo.app.ui.screens.RecordSheet
import com.eliamo.app.ui.screens.ReportScreen
import com.eliamo.app.ui.screens.SettingsScreen
import com.eliamo.app.ui.screens.TodayScreen
import com.eliamo.app.ui.theme.*
import kotlinx.coroutines.delay
import java.time.LocalDate

enum class Tab { TODAY, CALENDAR, REPORT, BADGE, SUPERMAN }

@Composable
private fun TabBar(current: Tab, onSelect: (Tab) -> Unit) {
    val items = listOf(
        Triple(Tab.TODAY, "今日", DoodleIcon.TODAY),
        Triple(Tab.CALENDAR, "日历", DoodleIcon.CALENDAR),
        Triple(Tab.REPORT, "战报", DoodleIcon.REPORT),
        Triple(Tab.BADGE, "荣誉", DoodleIcon.BADGE),
        Triple(Tab.SUPERMAN, "超人", DoodleIcon.SUPERMAN)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .height(D.TabBar)
            .background(PaperDeep)
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        items.forEach { (t, label, icon) ->
            val active = t == current
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .graphicsLayer { rotationZ = if (active) -2f else 0f }
                    .paperFrame(
                        background = if (active) {
                            if (t == Tab.SUPERMAN) StampSoft else TapeSoft
                        } else Color.Transparent,
                        border = Color.Transparent,
                        dashed = false,
                        borderWidth = 0.dp,
                        radius = 14.dp
                    )
                    .clickable { onSelect(t) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DoodleIconView(icon, tint = if (active) Ink else InkFaint, size = 21.dp)
                Spacer(Modifier.height(3.dp))
                Text(
                    label,
                    color = if (active) Ink else InkFaint,
                    fontSize = 10.sp,
                    fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun HomeIndicator() {
    Box(
        Modifier.fillMaxWidth().height(D.HomeBar).background(PaperDeep),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier.size(width = 118.dp, height = 4.dp)
                .background(Ink.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val store = remember { RecordStore(context.applicationContext) }
    val initial = remember { store.load() }

    var tab by remember { mutableStateOf(Tab.TODAY) }
    var returnTab by remember { mutableStateOf(Tab.TODAY) }
    var big by remember { mutableIntStateOf(initial.first) }
    var small by remember { mutableIntStateOf(initial.second) }
    val records = remember { mutableStateListOf<PoopRecord>().apply { addAll(initial.third) } }
    var checkedIn by remember { mutableStateOf(store.loadCheckins()) }
    var gameStats by remember { mutableStateOf(store.loadGameStats()) }

    var showRecord by remember { mutableStateOf(false) }
    var recordType by remember { mutableStateOf(RecordType.BIG) }
    var showSettings by remember { mutableStateOf(false) }
    var badgePop by remember { mutableStateOf<Badge?>(null) }

    var toast by remember { mutableStateOf<Pair<String, String>?>(null) }
    var toastEmoji by remember { mutableStateOf("🎉") }
    var confettiKey by remember { mutableIntStateOf(0) }
    var confettiVisible by remember { mutableStateOf(false) }

    LaunchedEffect(toast) {
        if (toast != null) {
            delay(2600)
            toast = null
        }
    }

    fun fireToast(emoji: String, title: String, desc: String) {
        toastEmoji = emoji
        toast = title to desc
    }

    fun selectTab(next: Tab) {
        if (next == Tab.SUPERMAN) {
            if (tab != Tab.SUPERMAN) returnTab = tab
        }
        tab = next
    }

    fun checkIn() {
        val key = LocalDate.now().toString()
        if (checkedIn.contains(key)) {
            fireToast("📌", "今天盖过章了", "一天只盖一次，明天再来。")
            return
        }
        val next = checkedIn + key
        checkedIn = next
        store.saveCheckins(next)
        fireToast("✅", "打卡成功", "今天的章盖好了，历史与未来都改不了。")
    }

    fun onGameFire(ammoId: Int) {
        val next = GameStats(
            shots = gameStats.shots + 1,
            hits = gameStats.hits + 1,
            usedShapes = gameStats.usedShapes + ammoId
        )
        gameStats = next
        store.saveGameStats(next)
    }

    Box(Modifier.fillMaxSize().background(Paper)) {
        PaperBackground()

        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) {
                when (tab) {
                    Tab.TODAY -> TodayScreen(
                        big = big,
                        small = small,
                        records = records,
                        onRecordBig = { recordType = RecordType.BIG; showRecord = true },
                        onRecordSmall = { recordType = RecordType.SMALL; showRecord = true },
                        onSettings = { showSettings = true },
                        onShake = { fireToast("🫨", "咚！", "摇出一句：今天也要顺顺畅畅的呀。") },
                        onClear = {
                            records.clear()
                            big = 0
                            small = 0
                            store.save(big, small, records)
                            fireToast("📄", "已清空演示数据", "空态长这样，点「开大」再记一笔就回来了。")
                        }
                    )
                    Tab.CALENDAR -> CalendarScreen(
                        todayBig = big,
                        todaySmall = small,
                        checkedIn = checkedIn,
                        onCheckIn = { checkIn() },
                        onNotice = { e, t, d -> fireToast(e, t, d) }
                    )
                    Tab.REPORT -> ReportScreen()
                    Tab.BADGE -> AchievementsScreen(
                        onBadgeClick = { badgePop = it },
                        gameStats = gameStats
                    )
                    Tab.SUPERMAN -> GameScreen(
                        onBack = { tab = returnTab },
                        onFire = { onGameFire(it) },
                        onNotice = { e, t, d -> fireToast(e, t, d) }
                    )
                }
            }
            TabBar(tab) { selectTab(it) }
            HomeIndicator()
        }

        if (showRecord) {
            Box(Modifier.fillMaxSize()) {
                Box(
                    Modifier.fillMaxSize()
                        .background(Ink.copy(alpha = 0.5f))
                        .clickable { showRecord = false }
                )
                Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                    RecordSheet(
                        initialType = recordType,
                        onClose = { showRecord = false },
                        onSubmit = { type, rec ->
                            records.add(0, rec)
                            if (type == RecordType.BIG) big++ else small++
                            store.save(big, small, records)
                            confettiKey++
                            confettiVisible = true
                            fireToast(
                                "🎉",
                                if (type == RecordType.BIG) "出货成功！" else "放水成功！",
                                Quips.random()
                            )
                        }
                    )
                }
            }
        }

        if (showSettings) {
            Box(Modifier.fillMaxSize().background(Paper)) {
                PaperBackground()
                SettingsScreen(onBack = { showSettings = false })
            }
        }

        if (confettiVisible) {
            ConfettiBurst(confettiKey) { confettiVisible = false }
        }

        Box(
            Modifier.fillMaxSize().padding(top = 8.dp, start = D.Gutter, end = D.Gutter),
            contentAlignment = Alignment.TopCenter
        ) {
            ToastCard(toast, toastEmoji)
        }

        if (badgePop != null) {
            val b = badgePop!!
            Box(
                Modifier.fillMaxSize()
                    .background(Ink.copy(alpha = 0.4f))
                    .clickable { badgePop = null },
                contentAlignment = Alignment.Center
            ) {
                DoodleCard(modifier = Modifier.fillMaxWidth().padding(D.Gutter)) {
                    Text(b.emoji, fontSize = 46.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                    Text(b.name, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text(b.rarity.label + " · " + b.desc, color = InkFaint, fontSize = 11.sp,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (b.unlocked) "已解锁 · ${b.desc}。这份荣誉，实至名归。"
                        else "还差一点点（${b.progress}%）：${b.desc}。继续加油，掌柜的。",
                        color = InkSoft, fontSize = 13.sp, textAlign = TextAlign.Center,
                        lineHeight = 21.sp, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(D.S4))
                    SmallButton("收下这份荣誉", { badgePop = null }, variant = BtnVariant.SOLID)
                }
            }
        }
    }
}
