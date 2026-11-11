package com.eliamo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.Shapes
import com.eliamo.app.ui.components.*
import com.eliamo.app.ui.icons.DoodleIcon
import com.eliamo.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    var generation by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(generation) {
        loading = true
        delay(600)
        loading = false
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
                Text("第 46 周 · 一本正经", color = InkFaint, fontSize = 12.sp)
                Text("本周战报", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("数据不会说谎，但它会笑", color = InkSoft, fontSize = 13.sp)
            }
            StampChip("内部资料")
        }

        Spacer(Modifier.height(D.S4))
        if (loading) {
            DoodleCard(modifier = Modifier.fillMaxWidth()) {
                SkeletonBlock(22.dp, 0.45f)
                Spacer(Modifier.height(D.S3))
                SkeletonBlock(60.dp)
                Spacer(Modifier.height(D.S3))
                SkeletonBlock(14.dp, 0.8f)
            }
            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth()) { SkeletonBlock(110.dp) }
            Spacer(Modifier.height(D.S2))
            Text("正在召集数据研究院院士…", color = InkFaint, fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        } else {
            DoodleCard(modifier = Modifier.fillMaxWidth(), tilt = -0.6f, withTape = true) {
                Text("本周总出货量", color = InkFaint, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("12", color = Ink, fontSize = 52.sp, fontWeight = FontWeight.ExtraBold)
                    Text("坨", color = Ink, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
                    Text("放水 31 次", color = InkFaint, fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp, start = 12.dp))
                }
                Spacer(Modifier.height(D.S3))
                Row(Modifier.fillMaxWidth().height(22.dp)) {
                    Box(Modifier.weight(0.28f).fillMaxHeight()
                        .background(PoopBrown, RoundedCornerShape(topStart = 999.dp, bottomStart = 999.dp)),
                        contentAlignment = Alignment.Center) {
                        Text("大 28%", color = PaperCard, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(Modifier.weight(0.72f).fillMaxHeight()
                        .background(PeeGold, RoundedCornerShape(topEnd = 999.dp, bottomEnd = 999.dp)),
                        contentAlignment = Alignment.Center) {
                        Text("小 72%", color = PaperCard, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth()) {
                Text("时段分布", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth().height(110.dp).padding(top = D.S3),
                    verticalAlignment = Alignment.Bottom) {
                    listOf("早八" to 18, "午间" to 9, "傍晚" to 12, "深夜" to 4).forEachIndexed { i, (n, value) ->
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom) {
                            Box(
                                Modifier
                                    .width(38.dp)
                                    .height((value * 4).dp)
                                    .background(
                                        if (i == 0) StampRed else GrassGreen,
                                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(n, color = InkFaint, fontSize = 10.sp)
                        }
                    }
                }
                Text("黄金时段：08:00–10:00，稳定得可疑", color = InkFaint, fontSize = 11.sp)
            }

            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth()) {
                Text("形态分布", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(D.S2))
                val counts = listOf(1, 2, 5, 9, 3, 1, 1)
                Shapes.forEachIndexed { i, s ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("${s.id} 型", color = InkSoft, fontSize = 12.sp,
                            modifier = Modifier.width(48.dp))
                        Box(Modifier.weight(1f).height(12.dp)
                            .background(PaperSunk, RoundedCornerShape(999.dp))) {
                            Box(Modifier.fillMaxWidth(counts[i] / 9f).height(12.dp)
                                .background(PoopBrown, RoundedCornerShape(999.dp)))
                        }
                        Text("${counts[i]}", color = InkFaint, fontSize = 11.sp,
                            modifier = Modifier.width(26.dp), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                    }
                }
            }

            Spacer(Modifier.height(D.S3))
            DoodleCard(modifier = Modifier.fillMaxWidth(), background = TapeSoft, tilt = 0.5f) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("数据研究院结论", color = Ink, fontSize = 13.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    StampChip("已盖章")
                }
                Spacer(Modifier.height(10.dp))
                Text("「综合评估：输出稳定、形态优良、时段规律。建议继续保持膳食纤维摄入，并减少第 3 型以下出现频率。」",
                    color = Ink, fontSize = 16.sp, lineHeight = 27.sp)
                Text("—— 屙了么数据研究院 · 2026/11/11", color = InkFaint, fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }

            Spacer(Modifier.height(D.S4))
            SmallButton("重新生成战报", { generation++ }, icon = DoodleIcon.REFRESH,
                variant = BtnVariant.BORDERED)
        }
        Spacer(Modifier.height(D.S6))
    }
}
