package com.eliamo.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

// 轻量本地持久化：只用 Android 框架自带的 SharedPreferences + org.json，不引入额外依赖
class RecordStore(context: Context) {
    private val sp = context.getSharedPreferences("eliamo_records", Context.MODE_PRIVATE)

    fun load(): Triple<Int, Int, List<PoopRecord>> = try {
        if (!sp.contains("big")) {
            Triple(2, 5, TodayRecords)
        } else {
            val arr = JSONArray(sp.getString("records", "[]") ?: "[]")
            val list = (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                PoopRecord(
                    o.getLong("id"),
                    o.getString("time"),
                    if (o.getString("type") == "BIG") RecordType.BIG else RecordType.SMALL,
                    o.getString("title"),
                    o.getString("desc"),
                    o.getString("emoji")
                )
            }
            Triple(sp.getInt("big", 2), sp.getInt("small", 5), list)
        }
    } catch (e: Exception) {
        Triple(2, 5, TodayRecords)
    }

    fun save(big: Int, small: Int, records: List<PoopRecord>) {
        val arr = JSONArray()
        records.forEach { r ->
            arr.put(JSONObject().apply {
                put("id", r.id)
                put("time", r.time)
                put("type", r.type.name)
                put("title", r.title)
                put("desc", r.desc)
                put("emoji", r.emoji)
            })
        }
        sp.edit()
            .putInt("big", big)
            .putInt("small", small)
            .putString("records", arr.toString())
            .apply()
    }

    fun loadCheckins(): Set<String> = try {
        val arr = JSONArray(sp.getString("checkins", "[]") ?: "[]")
        (0 until arr.length()).map { arr.getString(it) }.toSet()
    } catch (e: Exception) {
        emptySet()
    }

    fun saveCheckins(keys: Set<String>) {
        val arr = JSONArray()
        keys.sorted().forEach { arr.put(it) }
        sp.edit().putString("checkins", arr.toString()).apply()
    }

    fun loadGameStats(): GameStats = try {
        val arr = JSONArray(sp.getString("game_shapes", "[]") ?: "[]")
        val shapes = (0 until arr.length()).map { arr.getInt(it) }.toSet()
        GameStats(
            sp.getInt("game_shots", 0),
            sp.getInt("game_hits", 0),
            shapes
        )
    } catch (e: Exception) {
        GameStats()
    }

    fun saveGameStats(stats: GameStats) {
        val arr = JSONArray()
        stats.usedShapes.sorted().forEach { arr.put(it) }
        sp.edit()
            .putInt("game_shots", stats.shots)
            .putInt("game_hits", stats.hits)
            .putString("game_shapes", arr.toString())
            .apply()
    }
}
