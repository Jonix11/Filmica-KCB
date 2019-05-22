package com.example.jongonzalez.filmica.view.trends


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_films.*

import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.data.Film
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.films.FilmsAdapter
import com.example.jongonzalez.filmica.view.films.FilmsFragment
import com.example.jongonzalez.filmica.view.util.GridOffsetDecoration
import kotlinx.android.synthetic.main.layout_error.*

class TrendsFragment : Fragment() {

    lateinit var listener: FilmsFragment.OnFilmClickListener

    val list: RecyclerView by lazy {
        listFilms.addItemDecoration(GridOffsetDecoration())
        return@lazy listFilms
    }
    val adapter = FilmsAdapter {
        listener.onClick(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FilmsFragment.OnFilmClickListener) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached activity isn't implementing ${FilmsFragment.OnFilmClickListener::class.java.canonicalName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter
        buttonRetry.setOnClickListener {
            reload()
        }
    }

    override fun onResume() {
        super.onResume()

        reload()
    }

    private fun reload() {
        showProgress()
        FilmsRepo.trendsFilms(context!!,
                { films ->
                    showList()
                    adapter.setFilms(films)

                }, { error ->
            showError()
        }
        )
    }

    private fun showList() {
        filmsProgress?.visibility = View.INVISIBLE
        list.visibility = View.VISIBLE
        layoutError.visibility = View.INVISIBLE
    }

    private fun showError() {
        filmsProgress.visibility = View.INVISIBLE
        layoutError?.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
    }

    private fun showProgress() {
        filmsProgress.visibility = View.VISIBLE
        layoutError.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }


}
