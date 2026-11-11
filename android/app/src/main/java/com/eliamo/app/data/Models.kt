package com.eliamo.app.data

import java.time.LocalDate

enum class RecordType { BIG, SMALL }

data class PoopRecord(
    val id: Long,
    val time: String,
    val type: RecordType,
    val title: String,
    val desc: String,
    val emoji: String
)

data class ShapeType(val id: Int, val emoji: String, val name: String, val desc: String)

data class Mood(val emoji: String, val name: String)

enum class Rarity(val label: String) {
    COMMON("普通"), RARE("稀有"), EPIC("史诗"), LEGEND("传说")
}

enum class DynamicStat { NONE, SHOTS, HITS, SHAPES }

data class Badge(
    val emoji: String,
    val name: String,
    val desc: String,
    val unlocked: Boolean,
    val progress: Int,
    val rarity: Rarity = Rarity.COMMON,
    val dynamic: DynamicStat = DynamicStat.NONE
)

// 小游戏弹体：tint 为 ARGB Long，flight 决定飞行轨迹
data class Ammo(
    val id: Int,
    val emoji: String,
    val name: String,
    val power: Int,
    val tint: Long,
    val quip: String,
    val flight: String
)

data class DayInfo(
    val date: LocalDate,
    val big: Int,
    val small: Int,
    val checked: Boolean
) {
    val total: Int get() = big + small
}

data class GameStats(
    val shots: Int = 0,
    val hits: Int = 0,
    val usedShapes: Set<Int> = emptySet()
)
