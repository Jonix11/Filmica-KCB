package com.example.jongonzalez.filmica.view.search


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_search.*

import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.view.util.GenericFilmsFragments
import com.example.jongonzalez.filmica.view.watchlist.WatchListAdapter
import java.lang.IllegalArgumentException
import android.view.inputmethod.InputMethodManager
import com.example.jongonzalez.filmica.data.Film
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.films.FilmsAdapter


class SearchFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        if (searchEditText.text.isNotEmpty()) {
            reload()
        }

        searchEditText.setOnKeyListener { view, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                reload()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                true
            }
            false
        }
    }

    private fun reload() {
        if (searchEditText.text.length < 3) {
            Toast.makeText(context, "The search query has to have at least three characters", Toast.LENGTH_LONG).show()
        } else {
            showProgress()
            FilmsRepo.getSearchFilms(context!!, searchEditText.text.toString(),
                    { films ->
                        if (films.count() > 0) {
                            if (films.count() > 10) {
                                val list: MutableList<Film> = mutableListOf()
                                for (i in 0..9) {
                                    list.add(films[i])
                                }
                                adapter.setFilms(list)
                            } else {
                                adapter.setFilms(films)
                            }
                            showList()
                        } else {
                            showNoSearch()
                        }
                    },
                    { error ->

                    })

        }

        // hide the keyboard
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

    }

    private fun showList() {
        searchProgress?.visibility = View.INVISIBLE
        searchList.visibility = View.VISIBLE
        layoutNoSearch.visibility = View.INVISIBLE
    }

    private fun showNoSearch() {
        searchProgress.visibility = View.INVISIBLE
        layoutNoSearch?.visibility = View.VISIBLE
        searchList.visibility = View.INVISIBLE
    }

    private fun showProgress() {
        searchProgress.visibility = View.VISIBLE
        layoutNoSearch.visibility = View.INVISIBLE
        searchList.visibility = View.INVISIBLE
    }
}
