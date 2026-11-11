package com.eliamo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliamo.app.data.Badge
import com.eliamo.app.data.Badges
import com.eliamo.app.data.GameStats
import com.eliamo.app.data.Rarity
import com.eliamo.app.data.resolveBadge
import com.eliamo.app.ui.components.DoodleCard
import com.eliamo.app.ui.components.DoodleChip
import com.eliamo.app.ui.components.paperFrame
import com.eliamo.app.ui.theme.*

private enum class BadgeFilter(val label: String) {
    ALL("全部"), UNLOCKED("已解锁"), LOCKED("未解锁"), LEGEND("传说")
}

@Composable
private fun RarityTag(rarity: Rarity) {
    val color = when (rarity) {
        Rarity.COMMON -> InkFaint
        Rarity.RARE -> Mint
        Rarity.EPIC -> PoopBrown
        Rarity.LEGEND -> StampDeep
    }
    Box(
        Modifier
            .background(
                when (rarity) {
                    Rarity.COMMON -> PaperDeep
                    Rarity.RARE -> GrassSoft
                    Rarity.EPIC -> TapeSoft
                    Rarity.LEGEND -> StampSoft
                },
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(rarity.label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BadgeCard(badge: Badge, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .graphicsLayer { alpha = if (badge.unlocked) 1f else 0.66f }
            .paperFrame(background = PaperCard, radius = D.Card)
            .clickable { onClick() }
            .padding(D.S4)
    ) {
        Column {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Text(badge.emoji, fontSize = 32.sp, modifier = Modifier.weight(1f))
                RarityTag(badge.rarity)
            }
            Spacer(Modifier.height(D.S2))
            Text(badge.name, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,
                maxLines = 1)
            Text(badge.desc, color = InkSoft, fontSize = 11.sp, lineHeight = 16.sp,
                minLines = 2)
            Spacer(Modifier.height(10.dp))
            Box(Modifier.fillMaxWidth().height(8.dp)
                .background(PaperSunk, RoundedCornerShape(999.dp))) {
                Box(Modifier.fillMaxWidth(badge.progress / 100f).height(8.dp)
                    .background(
                        if (badge.unlocked) GrassGreen else StampRed,
                        RoundedCornerShape(999.dp)
                    ))
            }
            Spacer(Modifier.height(6.dp))
            Text(
                if (badge.unlocked) "✓ 已解锁" else "${badge.progress}% · 还差一点",
                color = InkFaint, fontSize = 11.sp
            )
        }
    }
}

@Composable
fun AchievementsScreen(
    onBadgeClick: (Badge) -> Unit,
    gameStats: GameStats,
    modifier: Modifier = Modifier
) {
    var filter by remember { mutableStateOf(BadgeFilter.ALL) }
    val resolved = remember(gameStats) { Badges.map { resolveBadge(it, gameStats) } }
    val unlockedCount = resolved.count { it.unlocked }
    val shown = when (filter) {
        BadgeFilter.ALL -> resolved
        BadgeFilter.UNLOCKED -> resolved.filter { it.unlocked }
        BadgeFilter.LOCKED -> resolved.filter { !it.unlocked }
        BadgeFilter.LEGEND -> resolved.filter { it.rarity == Rarity.LEGEND }
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = D.Gutter, vertical = D.S2)
    ) {
        Spacer(Modifier.height(10.dp))
        Text("荣誉墙 · 共 ${resolved.size} 枚", color = InkFaint, fontSize = 12.sp)
        Text("一坨一荣誉", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text("把不好意思说的事，做成勋章", color = InkSoft, fontSize = 13.sp)

        Spacer(Modifier.height(D.S4))
        DoodleCard(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("收集进度", color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f))
                Text("$unlockedCount / ${resolved.size}", color = Ink, fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(D.S3))
            Box(Modifier.fillMaxWidth().height(10.dp)
                .background(PaperSunk, RoundedCornerShape(999.dp))) {
                Box(Modifier.fillMaxWidth(unlockedCount / resolved.size.toFloat()).height(10.dp)
                    .background(StampRed, RoundedCornerShape(999.dp)))
            }
            Spacer(Modifier.height(D.S2))
            Text("战斗模式相关徽章会随发射、命中、弹体收集实时涨进度。",
                color = InkFaint, fontSize = 11.sp)
        }

        Spacer(Modifier.height(D.S3))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(D.S2)) {
            BadgeFilter.entries.forEach { f ->
                DoodleChip(f.label, filter == f, { filter = f })
            }
        }

        Spacer(Modifier.height(D.S2))
        Text("当前显示 ${shown.size} 枚", color = InkFaint, fontSize = 11.sp)
        Spacer(Modifier.height(D.S2))

        shown.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(D.S3)) {
                row.forEach { b ->
                    BadgeCard(b, { onBadgeClick(b) }, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(D.S3))
        }
        Spacer(Modifier.height(D.S4))
    }
}
