package com.example.jongonzalez.filmica

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object FilmsRepo {

    val films: MutableList<Film> = mutableListOf() // Crea una lista vacia
    get() {
        if (field.isEmpty()) {
            field.addAll(dummyFilms())
        }
        return field
    }

    private fun dummyFilms(): MutableList<Film> {
        return (1..10).map { i: Int ->
            return@map Film(
                    id = "${i}",
                    title = "Film ${i}",
                    overview = "Overview ${i}",
                    genre = "Genre ${i}",
                    rating = i.toFloat(),
                    date = "2019-05-${i}"
            )
        }.toMutableList()

        /*val list: MutableList<Film> = mutableListOf()
        for (i in 1..10) {
            list.add(Film( title = "Film ${i}",
                    overview = "Overview ${i}",
                    genre = "Genre ${i}",
                    rating = i.toFloat(),
                    date = "2019-05-${i}"
            ))
        }

        return list*/


    }

    fun findFilmById(id: String): Film? {
        return films.find {
            return@find it.id == id
        }
    }

    fun discoverFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (VolleyError) -> Unit) {
        val url = Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", "ba183dab37e6815e6c9850d504396ed8")
                .appendPath("discover")
                .appendPath("movie")
                .build()
                .toString()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val films = mutableListOf<Film>()
                    val filmsArray = response.getJSONArray("results")
                    for (i in 0..(filmsArray.length() - 1)) {
                        val film = Film.parseFilm(filmsArray.getJSONObject(i))
                        films.add(film)
                    }
                    onResponse.invoke(films)
                },
                { error ->
                    error.printStackTrace()
                    onError.invoke(error)
                })

        Volley.newRequestQueue(context)
                .add(request)
    }
}