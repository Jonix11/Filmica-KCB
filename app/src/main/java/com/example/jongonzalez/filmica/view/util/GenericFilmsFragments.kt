package com.example.jongonzalez.filmica.view.util

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.data.Film
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.films.FilmsAdapter
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*

open class GenericFilmsFragments: Fragment() {
    lateinit var listener: OnFilmClickListener

    val list: RecyclerView by lazy {
        listFilms.addItemDecoration(GridOffsetDecoration())
        return@lazy listFilms
    }
    val adapter = FilmsAdapter {
        listener.onClick(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFilmClickListener) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached activity isn't implementing ${OnFilmClickListener::class.java.canonicalName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    protected fun showList() {
        filmsProgress?.visibility = View.INVISIBLE
        list.visibility = View.VISIBLE
        layoutError.visibility = View.INVISIBLE
    }

    protected fun showError() {
        filmsProgress.visibility = View.INVISIBLE
        layoutError?.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
    }

    protected fun showProgress() {
        filmsProgress.visibility = View.VISIBLE
        layoutError.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }

    interface OnFilmClickListener {
        fun onClick(film: Film)
    }
}