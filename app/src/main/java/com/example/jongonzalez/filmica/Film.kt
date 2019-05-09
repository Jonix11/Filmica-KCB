package com.example.jongonzalez.filmica

import org.json.JSONObject

data class Film(
        val id: String = "",
        val title: String = "No title",
        val genre: String = "No genre",
        val rating: Float = 0.0f,
        val overview: String = "No overview",
        val date: String = "1999-09-19"
) {
    companion object {
        fun parseFilm(jsonFilm: JSONObject): Film {
            with(jsonFilm) {
                 return Film(
                        id = getInt("id").toString(),
                        title = getString("title"),
                        overview = getString("overview"),
                        rating = getDouble("vote_average").toFloat(),
                        date = getString("release_date")
                        )
            }
        }
    }
}