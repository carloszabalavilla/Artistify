package com.czabala.miproyecto.core

import com.google.gson.JsonParser

/** Keeps the existing search/id API contract while using only fictional fixtures. */
object DemoResponses {
    fun filter(raw: String, kind: String, query: String?, ids: String?): String {
        if (kind !in listOf("artists", "tracks", "search-artists", "search-tracks")) return raw
        val json = JsonParser.parseString(raw).asJsonObject
        if (kind.startsWith("search-")) {
            val key = if (kind == "search-artists") "artists" else "tracks"
            val section = json.getAsJsonObject(key)
            val items = section.getAsJsonArray("items")
            for (index in items.size() - 1 downTo 0) {
                val data = items[index].asJsonObject.getAsJsonObject("data")
                val name = if (key == "artists") data.getAsJsonObject("profile").get("name").asString else data.get("name").asString
                if (!name.contains(query.orEmpty(), ignoreCase = true)) items.remove(index)
            }
            section.addProperty("totalCount", items.size())
        } else if (!ids.isNullOrBlank()) {
            val requested = ids.split(",").toSet()
            val items = json.getAsJsonArray(kind)
            for (index in items.size() - 1 downTo 0) if (items[index].asJsonObject.get("id").asString !in requested) items.remove(index)
        }
        return json.toString()
    }
}
