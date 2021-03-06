package com.example.jongonzalez.filmica.data

import android.arch.persistence.room.Query
import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray

object FilmsRepo {

    private val films: MutableList<Film> = mutableListOf() // Crea una lista vacio
    private val trendsFilms: MutableList<Film> = mutableListOf()
    private val searchFilms: MutableList<Film> = mutableListOf()

    @Volatile
    private var db: FilmDatabase? = null

    private fun getDbInstance(context: Context): FilmDatabase {
        if (db == null) {
            db = Room.databaseBuilder(context.applicationContext, FilmDatabase::class.java, "filmica-db").build()
        }
        return db as FilmDatabase
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
    }

    fun findFilmById(id: String): Film? {
        return films.find {
            return@find it.id == id
        }
    }

    fun findTrendFilmById(id: String): Film? {
        return trendsFilms.find {
            return@find it.id == id
        }
    }

    fun findSearchFilmById(id: String): Film? {
        return searchFilms.find {
            return@find it.id == id
        }
    }

    fun saveFilm(context: Context, film: Film, callback: (Film) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }

            async.await()
            callback.invoke(film)
        }

    }

    fun getFilms(context: Context, callback: (List<Film>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }

            val films = async.await()
            callback.invoke(films)
        }
    }

    fun getFilmById(context: Context, id: String, callback: (Film) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilmById(id)
            }

            val film = async.await()
            callback.invoke(film)
        }
    }

    fun deleteFilm(context: Context, film: Film, callback: (Film) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }

            async.await()
            callback.invoke(film)
        }
    }

    fun getDiscoverFilmsList(): List<Film> {
        return FilmsRepo.films
    }

    fun getTrendsFilmsList(): List<Film> {
        return FilmsRepo.trendsFilms
    }

    fun discoverFilms(page: Int, context: Context, onResponse: (List<Film>) -> Unit, onError: (VolleyError) -> Unit) {
        val url = ApiRoutes.discoverMoviesUrl(page)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val films = Film.parseFilms(response.getJSONArray("results"))
                    if (page == 1) FilmsRepo.films.clear()

                    FilmsRepo.films.addAll(films)

                    onResponse.invoke(FilmsRepo.films)
                },
                { error ->
                    error.printStackTrace()
                    onError.invoke(error)
                })

        Volley.newRequestQueue(context)
                .add(request)
    }

    fun getTrendsFilms(page: Int = 1, context: Context, onResponse: (List<Film>) -> Unit, onError: (VolleyError) -> Unit) {
        val url = ApiRoutes.trendsMoviesUrl(page)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                {response ->
                    val films = Film.parseFilms(response.getJSONArray("results"))
                    if (page == 1) FilmsRepo.trendsFilms.clear()

                    FilmsRepo.trendsFilms.addAll(films)

                    onResponse.invoke(FilmsRepo.trendsFilms)
                },
                { error ->
                    error.printStackTrace()
                    onError.invoke(error)
                })

        Volley.newRequestQueue(context)
                .add(request)
    }

    fun getSearchFilms(context: Context, query: String, onResponse: (List<Film>) -> Unit, onError: (VolleyError) -> Unit) {
        val url = ApiRoutes.searchMovieUrl(query)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                {response ->
                    val films = Film.parseFilms(response.getJSONArray("results"))
                    FilmsRepo.searchFilms.clear()
                    FilmsRepo.searchFilms.addAll(films)

                    onResponse.invoke(FilmsRepo.searchFilms)
                },
                {error ->
                    error.printStackTrace()
                    onError(error)
                })
        Volley.newRequestQueue(context)
                .add(request)
    }
}