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
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.films.FilmsAdapter
import com.example.jongonzalez.filmica.view.films.FilmsFragment
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
    }

    override fun onResume() {
        super.onResume()

        reload()
    }

    private fun reload() {
        showProgress()
        FilmsRepo.getTrendsFilms(context!!,
                { films ->
                    showList()
                    adapter.setFilms(films)

                }, { error ->
            showError()
        })
    }
}
