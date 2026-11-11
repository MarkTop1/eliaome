package com.eliamo.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.icons.DoodleIconView
import com.eliamo.app.ui.theme.*

@Composable
private fun SwitchRow(title: String, desc: String, on: Boolean, onToggle: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(vertical = D.S2),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = InkFaint, fontSize = 11.sp)
        }
        DoodleSwitch(on, onToggle)
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var remind by remember { mutableStateOf(true) }
    var privacy by remember { mutableStateOf(false) }
    var shake by remember { mutableStateOf(true) }
    var quiet by remember { mutableStateOf(false) }
    var export by remember { mutableStateOf("idle") }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S2)
    ) {
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("我的 · 设置", color = InkFaint, fontSize = 12.sp)
                Text("掌柜的档案", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("连续打卡 6 天 · 共记录 148 次", color = InkSoft, fontSize = 13.sp)
            }
            Box(Modifier.size(D.Touch).paperFrame(background = PaperCard, border = LineSolid, radius = 14.dp)
                .clickable { onBack() }, contentAlignment = Alignment.Center) {
                DoodleIconView(DoodleIcon.CLOSE, size = 22.dp)
            }
        }

        Spacer(Modifier.height(D.S4))
        DoodleCard(modifier = Modifier.fillMaxWidth(), tilt = -0.6f, withTape = true) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(52.dp).paperFrame(background = StampSoft, border = Ink,
                    dashed = false, borderWidth = 2.dp, radius = D.Button),
                    contentAlignment = Alignment.Center) { Text("🐷", fontSize = 28.sp) }
                Spacer(Modifier.width(D.S3))
                Column {
                    Text("掌柜的", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    Text("ID: ELIAOMO_0001 · 注册于 2026/09/28", color = InkFaint, fontSize = 11.sp)
                }
            }
        }

        SectionTitle("日常")
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            SwitchRow("饭点催一催", "每天 12:30 提醒你诚实交代", remind) { remind = !remind }
            SwitchRow("隐私模式 · 防偷看", "锁屏上不显示今日战绩", privacy) { privacy = !privacy }
            SwitchRow("摇一摇彩蛋", "摇手机听个响， purely for fun", shake) { shake = !shake }
            SwitchRow("深夜静音", "23:00 后不弹任何提醒", quiet) { quiet = !quiet }
        }

        SectionTitle("数据")
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            SmallButton("导出我的战绩", { export = "error" }, icon = DoodleIcon.SHARE,
                variant = BtnVariant.BORDERED)
            if (export == "error") {
                Spacer(Modifier.height(D.S3))
                ErrorCard(
                    "⚠️ 导出失败：手机没插 U 盘",
                    "开个玩笑。点下面按钮再来一次。",
                    { export = "ok" }
                )
            }
            if (export == "ok") {
                Spacer(Modifier.height(D.S2))
                Text("✅ 已导出到 下载/屙了么_战绩.txt（假装）",
                    color = GrassGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(D.S3))
        DoodleCard(modifier = Modifier.fillMaxWidth(), background = PaperDeep) {
            Text("关于屙了么", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("v1.0 · 一本正经地记录不正经的事。\n设计语言：HeroUI 按钮变体 / Watermelon 变形按钮与标签页 / Bencho 彩纸、滑动确认与热力图。",
                color = InkSoft, fontSize = 11.sp, lineHeight = 19.sp)
        }
        Spacer(Modifier.height(D.S6))
    }
}
