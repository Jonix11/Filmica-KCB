package com.example.jongonzalez.filmica.view.trends

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_films.*

import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.films.FilmsAdapter
import com.example.jongonzalez.filmica.view.films.FilmsFragment
import com.example.jongonzalez.filmica.view.util.EndlessRecyclerViewScrollListener
import com.example.jongonzalez.filmica.view.util.GenericFilmsFragments
import com.example.jongonzalez.filmica.view.util.GridOffsetDecoration
import kotlinx.android.synthetic.main.layout_error.*

class TrendsFragment : GenericFilmsFragments() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter
        buttonRetry.setOnClickListener {
            reload()
        }

        list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(listFilms.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                FilmsRepo.getTrendsFilms(page, context!!,
                        {
                            adapter.setFilmsPaged(it)
                        },
                        {
                            showError()
                        })
            }

        })
    }

    override fun onResume() {
        super.onResume()

        reload()
    }

    private fun reload() {
        showProgress()
        if (list.adapter?.itemCount == 0) {
            FilmsRepo.getTrendsFilms(1, context!!,
                    { films ->
                        showList()
                        adapter.setFilms(films)

                    }, { error ->
                showError()
            })
        } else {
            showList()
            adapter.setFilms(FilmsRepo.getTrendsFilmsList())
        }



    }
}
