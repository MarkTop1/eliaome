package com.eliamo.app.data

import java.time.LocalDate
import java.time.YearMonth

// 纯函数规则：人偶名称校验 / 打卡时间规则 / 跨月历史数据生成
object NameValidator {
    const val MAX_LENGTH = 12

    fun validate(raw: String): String? {
        val name = raw.trim()
        if (name.isEmpty()) return "名字不能空着，人偶会伤心的"
        if (name.length > MAX_LENGTH) return "最多 12 个字，名牌写不下"
        if (raw.any { it == '\n' || it == '\r' || it == '\t' }) return "名字里不能有换行或制表符"
        if (name.contains('<') || name.contains('>')) return "名字里不能带 < 或 >"
        return null
    }
}

object CheckInRules {
    fun isFuture(date: LocalDate, today: LocalDate): Boolean = date.isAfter(today)
    fun isPast(date: LocalDate, today: LocalDate): Boolean = date.isBefore(today)
    fun isToday(date: LocalDate, today: LocalDate): Boolean = date.isEqual(today)

    fun canCheckIn(date: LocalDate, today: LocalDate, checkedIn: Set<String>): Boolean =
        isToday(date, today) && !checkedIn.contains(date.toString())

    fun daysInMonth(year: Int, month: Int): Int = YearMonth.of(year, month).lengthOfMonth()

    // 周日 = 0，与日历表头「日 一 二 三 四 五 六」一致
    fun firstWeekday(year: Int, month: Int): Int = LocalDate.of(year, month, 1).dayOfWeek.value % 7
}

// 游戏类徽章按累计战绩实时结算进度
fun resolveBadge(badge: Badge, stats: GameStats): Badge = when (badge.dynamic) {
    DynamicStat.SHOTS -> {
        val p = (stats.shots * 10).coerceAtMost(100)
        badge.copy(progress = p, unlocked = p >= 100)
    }
    DynamicStat.HITS -> {
        val p = (stats.hits * 5).coerceAtMost(100)
        badge.copy(progress = p, unlocked = p >= 100)
    }
    DynamicStat.SHAPES -> {
        val p = (stats.usedShapes.size * 100 / 7).coerceAtMost(100)
        badge.copy(progress = p, unlocked = p >= 100)
    }
    DynamicStat.NONE -> badge
}

// 历史数据按日期确定性生成，保证每次打开结果一致；未来日期恒为空
fun dayInfoFor(
    date: LocalDate,
    today: LocalDate,
    todayBig: Int,
    todaySmall: Int,
    checkedIn: Set<String>
): DayInfo = when {
    CheckInRules.isFuture(date, today) -> DayInfo(date, 0, 0, false)
    CheckInRules.isToday(date, today) -> DayInfo(date, todayBig, todaySmall, checkedIn.contains(date.toString()))
    else -> {
        val seed = (date.year * 372 + date.monthValue * 31 + date.dayOfMonth * 17) % 97
        val big = if (seed % 4 == 0) 0 else (seed % 3) + 1
        val small = (seed % 5) + 1
        DayInfo(date, big, small, big + small > 0 && seed % 3 != 0)
    }
}
