package com.example.jongonzalez.filmica.data

import android.net.Uri
import com.example.jongonzalez.filmica.BuildConfig

object ApiRoutes {

    fun discoverMoviesUrl(page: Int, language: String = "en-US", sort: String = "popularity.desc"): String {
        return getUriBuilder()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sort)
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("include_video", "false")
                .build()
                .toString()
    }

    fun trendsMoviesUrl(page: Int, language: String = "en-US"): String {
        return getUriBuilder()
                .appendPath("trending")
                .appendPath("movie")
                .appendPath("week")
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("language", language)
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("include_video", "false")
                .build()
                .toString()
    }

    fun searchMovieUrl(searchQuery: String, language: String = "en-US", sort: String = "popularity.desc"): String {
        return getUriBuilder()
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("query", searchQuery)
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sort)
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("include_video", "false")
                .build()
                .toString()
    }

    private fun getUriBuilder(): Uri.Builder =
            Uri.Builder()
                    .scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)

}