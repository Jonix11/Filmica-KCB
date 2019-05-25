package com.example.jongonzalez.filmica.view.watchlist


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.data.Film
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.util.BaseFilmHolder
import com.example.jongonzalez.filmica.view.util.GenericFilmsFragments
import com.example.jongonzalez.filmica.view.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment() {

    lateinit var listener: GenericFilmsFragments.OnFilmClickListener

    val adapter: WatchListAdapter = WatchListAdapter {
        listener.onClick(it)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is GenericFilmsFragments.OnFilmClickListener) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached activity isn't implementing ${GenericFilmsFragments.OnFilmClickListener::class.java.canonicalName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeHandler()
        watchlist.adapter = adapter
    }

    private fun setupSwipeHandler() {
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
                val film = (holder as BaseFilmHolder).film
                val position = holder.adapterPosition
                deleteFilm(film, position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(watchlist)
    }

    private fun deleteFilm(film: Film, position: Int) {
        FilmsRepo.deleteFilm(context!!, film) {
            adapter.deleteFilm(position)
        }
    }

    override fun onResume() {
        super.onResume()

        FilmsRepo.getFilms(context!!) {
            adapter.setFilms(it)
        }
    }


}
