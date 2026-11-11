package com.eliamo.app.data

val Shapes = listOf(
    ShapeType(1, "🫘", "羊屎蛋", "倔强型"),
    ShapeType(2, "🍇", "葡萄串", "纠结型"),
    ShapeType(3, "🌽", "玉米棒", "标准型"),
    ShapeType(4, "🍌", "香蕉船", "顺滑型"),
    ShapeType(5, "🍮", "软糖", "松散型"),
    ShapeType(6, "🥣", "泥浆", "奔放型"),
    ShapeType(7, "💧", "纯液体", "无需描述")
)

val Amounts = listOf("一点点", "偏少", "常规", "有点多", "惊艳全场")
val Minutes = listOf("＜1 分", "约 3 分", "约 5 分", "10 分+", "记不清了")
val Moods = listOf(
    Mood("😌", "舒坦"), Mood("😎", "从容"), Mood("🫠", "艰难"),
    Mood("🚀", "喷射"), Mood("🤔", "意外")
)

val Quips = listOf(
    "地球轻了 0.2kg，辛苦了。",
    "这一坨，很有分量。",
    "今日 KPI 已完成，可以下班了。",
    "肠胃董事会已通过本次提案。",
    "恭喜，又为厕所贡献了一次 GDP。",
    "记录已归档，历史会记住今天。",
    "顺滑得不像话，建议申报非遗。"
)

val HitQuips = listOf(
    "命中！人偶的表情管理彻底失效。",
    "正中眉心，这一发很有故事。",
    "啪叽——名牌都歪了。",
    "伤害不高，但侮辱性极强。",
    "人偶表示：我想换个班。",
    "现场过于激烈，已打码。",
    "这一坨，飞得比工资到账还快。"
)

// 小游戏弹体，与记录页的布里斯托 7 型共用世界观
val AmmoList = listOf(
    Ammo(1, "🫘", "羊屎蛋", 1, 0xFFA97142, "粒粒分明，像下了一阵豆子雨", "fast"),
    Ammo(2, "🍇", "葡萄串", 2, 0xFF7C5AA6, "连成一串，命中率有点随缘", "wobble"),
    Ammo(3, "🌽", "玉米棒", 3, 0xFFD89B22, "标准型，稳得像上班打卡", "straight"),
    Ammo(4, "🍌", "香蕉船", 4, 0xFFE6B84F, "顺滑得不像话，建议申报非遗", "arc"),
    Ammo(5, "🍮", "软糖", 2, 0xFFE8836B, "软趴趴，但糊得最均匀", "wobble"),
    Ammo(6, "🥣", "泥浆", 5, 0xFF6B5B4A, "范围伤害，画面不宜久看", "heavy"),
    Ammo(7, "💧", "纯液体", 3, 0xFF5FA98B, "无需描述，懂的都懂", "fast")
)

// 保留旧数据，供战报等演示使用
val Heat = listOf(0, 2, 1, 3, 0, 1, 2, 4, 1, 0, 2, 7, 3, 1, 2, 0, 3, 2, 5, 1, 0, 2, 1, 4, 2, 0, 1, 3, 2, 1)

private data class RawBadge(val e: String, val n: String, val d: String, val r: Rarity)

private val RawBadges = listOf(
    RawBadge("🏆", "黄金便便", "连续 7 天有出货", Rarity.LEGEND),
    RawBadge("🚀", "喷射战士", "单次耗时 ＜1 分钟", Rarity.RARE),
    RawBadge("💧", "水王", "单日开小 10 次", Rarity.RARE),
    RawBadge("⏰", "早八准时", "连续 5 天 8 点前后", Rarity.COMMON),
    RawBadge("🌙", "深夜食堂", "凌晨 2 点也来一笔", Rarity.RARE),
    RawBadge("📅", "七日不断更", "连续打卡 7 天", Rarity.RARE),
    RawBadge("🎨", "形态收藏家", "集齐 7 种形态", Rarity.EPIC),
    RawBadge("⚖️", "一坨千金", "单月累计 30 坨", Rarity.EPIC),
    RawBadge("🌅", "月初开门红", "每月 1 号有记录", Rarity.COMMON),
    RawBadge("🏁", "月末收官", "每月最后一天有记录", Rarity.COMMON),
    RawBadge("🛋️", "周末不休息", "周六周日都出货", Rarity.COMMON),
    RawBadge("💼", "工作日全勤", "周一到周五无断档", Rarity.RARE),
    RawBadge("🔥", "连续三十天", "连续打卡 30 天", Rarity.LEGEND),
    RawBadge("💯", "百次俱乐部", "累计记录 100 次", Rarity.EPIC),
    RawBadge("🖐️", "五百次传说", "累计记录 500 次", Rarity.LEGEND),
    RawBadge("👑", "千次里程碑", "累计记录 1000 次", Rarity.LEGEND),
    RawBadge("🍌", "香蕉船船长", "第 4 型出现 20 次", Rarity.RARE),
    RawBadge("🫘", "羊屎蛋猎人", "第 1 型出现 10 次", Rarity.COMMON),
    RawBadge("🌽", "玉米棒爱好者", "第 3 型出现 15 次", Rarity.COMMON),
    RawBadge("🍮", "软糖鉴赏家", "第 5 型出现 10 次", Rarity.COMMON),
    RawBadge("🥣", "泥浆艺术家", "第 6 型出现 8 次", Rarity.RARE),
    RawBadge("💦", "纯液体大师", "第 7 型出现 5 次", Rarity.EPIC),
    RawBadge("🍇", "葡萄串收藏家", "第 2 型出现 10 次", Rarity.COMMON),
    RawBadge("📖", "七型全图鉴", "七种形态各至少一次", Rarity.EPIC),
    RawBadge("⚡", "三分钟战士", "单次耗时 ＜3 分钟", Rarity.COMMON),
    RawBadge("🕰️", "十分钟的沉思", "单次耗时 ＞10 分钟", Rarity.COMMON),
    RawBadge("🧘", "一小时哲学家", "单次耗时 ＞60 分钟", Rarity.LEGEND),
    RawBadge("🏃", "速通达人", "连续 5 次 ＜1 分钟", Rarity.RARE),
    RawBadge("🐦", "早起鸟儿", "早上 6 点前记录", Rarity.RARE),
    RawBadge("☀️", "午间例行", "12 点到 14 点记录", Rarity.COMMON),
    RawBadge("🌇", "傍晚散步", "18 点到 20 点记录", Rarity.COMMON),
    RawBadge("🌌", "凌晨三点", "凌晨 3 点记录", Rarity.EPIC),
    RawBadge("🍚", "饭点准时", "饭后 30 分钟内记录", Rarity.COMMON),
    RawBadge("☕", "咖啡因信徒", "喝咖啡后记录", Rarity.COMMON),
    RawBadge("🥬", "膳食纤维标兵", "连续 7 天备注蔬菜", Rarity.RARE),
    RawBadge("🐉", "火龙果勇士", "备注提到火龙果", Rarity.EPIC),
    RawBadge("🌶️", "火锅挑战者", "备注提到火锅", Rarity.COMMON),
    RawBadge("🧋", "奶茶代价", "备注提到奶茶", Rarity.COMMON),
    RawBadge("✈️", "出差不断更", "出差期间仍打卡", Rarity.RARE),
    RawBadge("🗺️", "旅行打卡", "在 3 个不同城市记录", Rarity.EPIC),
    RawBadge("🎏", "节假日坚守", "法定节假日有记录", Rarity.RARE),
    RawBadge("🧨", "春节特别篇", "大年初一有记录", Rarity.LEGEND),
    RawBadge("🎂", "生日纪念", "生日当天有记录", Rarity.EPIC),
    RawBadge("🎆", "跨年最后一坨", "12 月 31 日有记录", Rarity.LEGEND),
    RawBadge("🎇", "新年第一坨", "1 月 1 日有记录", Rarity.LEGEND),
    RawBadge("😤", "周一勇气奖", "连续 4 个周一有记录", Rarity.COMMON),
    RawBadge("🎉", "周五解脱奖", "连续 4 个周五有记录", Rarity.COMMON),
    RawBadge("🥇", "月度冠军", "单月出货量第一", Rarity.EPIC),
    RawBadge("📊", "季度劳模", "连续 3 个月满勤", Rarity.LEGEND),
    RawBadge("🏅", "年度总冠军", "全年无断档", Rarity.LEGEND),
    RawBadge("🍃", "零负担日", "一整天没有记录", Rarity.COMMON),
    RawBadge("🎒", "满载而归", "份量选「惊艳全场」", Rarity.RARE),
    RawBadge("🫧", "一点点先生", "份量选「一点点」", Rarity.COMMON),
    RawBadge("✨", "惊艳全场", "单次获得最高评价", Rarity.EPIC),
    RawBadge("😌", "心情全收集", "五种心情都用过", Rarity.RARE),
    RawBadge("✍️", "备注小作文", "单条备注超过 30 字", Rarity.RARE),
    RawBadge("📝", "备注诗人", "累计写下 10 条备注", Rarity.EPIC),
    RawBadge("🍜", "毒鸡汤品鉴师", "集齐 7 句毒鸡汤", Rarity.RARE),
    RawBadge("🗓️", "热力图画家", "单月打卡超过 25 天", Rarity.EPIC),
    RawBadge("🦸", "屙了么超人", "战斗模式发射 10 次", Rarity.LEGEND),
    RawBadge("🎯", "百发百中", "战斗模式命中 20 次", Rarity.EPIC),
    RawBadge("🌈", "弹体收藏家", "用过 7 种形状弹体", Rarity.EPIC),
    RawBadge("🧟", "人偶克星", "单局命中 10 次", Rarity.RARE),
    RawBadge("🕹️", "战斗模式常客", "累计重置 5 局", Rarity.COMMON)
)

private val UnlockedIndices = setOf(0, 1, 2, 3, 6, 8, 10, 16, 24, 29, 32, 45)

val Badges: List<Badge> = RawBadges.mapIndexed { i, b ->
    val dynamic = when (b.n) {
        "屙了么超人" -> DynamicStat.SHOTS
        "百发百中" -> DynamicStat.HITS
        "弹体收藏家" -> DynamicStat.SHAPES
        else -> DynamicStat.NONE
    }
    val unlocked = dynamic == DynamicStat.NONE && i in UnlockedIndices
    val progress = when {
        dynamic != DynamicStat.NONE -> 0
        unlocked -> 100
        else -> 15 + (i * 37) % 75
    }
    Badge(b.e, b.n, b.d, unlocked, progress, b.r, dynamic)
}

val TodayRecords = listOf(
    PoopRecord(1, "08:12", RecordType.BIG, "开大 · 香蕉船（4 型）", "顺滑得不像话，建议申报非遗", "💩"),
    PoopRecord(2, "10:36", RecordType.SMALL, "开小 · 常规", "例行公事，稳", "💧"),
    PoopRecord(3, "13:05", RecordType.SMALL, "开小 · 有点多", "中午那杯美式立功了", "💧"),
    PoopRecord(4, "19:48", RecordType.BIG, "开大 · 玉米棒（3 型）", "很有分量，盖章", "💩"),
    PoopRecord(5, "21:10", RecordType.SMALL, "开小 · 常规", "睡前最后一次", "💧")
)
