package com.example.jongonzalez.filmica.view.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.*
import android.widget.SimpleAdapter
import android.widget.Toast
import com.example.jongonzalez.filmica.R
import com.example.jongonzalez.filmica.data.Film
import com.example.jongonzalez.filmica.data.FilmsRepo
import com.example.jongonzalez.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_watchlist.*
import kotlinx.android.synthetic.main.item_film.view.*

class DetailFragment: Fragment() {

    var film: Film? = null

    companion object {
        fun newInstance(filmId: String, tag: String): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("id", filmId)
            bundle.putString("tag", tag)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            shareFilm()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun shareFilm() {
        val intent = Intent(Intent.ACTION_SEND)

        film?.let {
            val text = getString(R.string.template_share, it.title, it.rating)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
        }

        startActivity(Intent.createChooser(intent, getString(R.string.title_share)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id", "")
        val tag = arguments?.getString("tag")

        when (tag) {
            "films" -> film = FilmsRepo.findFilmById(id!!)
            "trends" -> film = FilmsRepo.findTrendFilmById(id!!)
            "watchlist" -> FilmsRepo.getFilmById(context!!, id!!) {
                film = it
                setupFilmView()
            }
            "search" -> film = FilmsRepo.findSearchFilmById(id!!)
        }


        setupFilmView()

        buttonAdd.setOnClickListener {
            film?.let { film ->
                FilmsRepo.saveFilm(context!!, film) {
                    Snackbar.make(view, "Added to watchlist", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                FilmsRepo.deleteFilm(context!!, film) {
                                    Toast.makeText(context, "Undone action", Toast.LENGTH_LONG).show()
                                }
                            }
                            .show()
                }
            }
        }

    }

    private fun setupFilmView() {
        film?.let {
            labelTitle.text = it.title
            labelDescription.text = it.overview
            labelGenre.text = it.genre
            labelDate.text = it.date
            labelRating.text = it.rating.toString()
            loadImage(it)

        }
    }

    private fun loadImage(film: Film) {
        val target = SimpleTarget {
            imgPoster.setImageBitmap(it)
            setColor(it)
        }

        imgPoster.tag = target

        Picasso.with(context)
                .load(film.getPosterUrl())
                .error(R.drawable.placeholder)
                .into(target)

    }

    private fun setColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate {
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor
            val overlayColor = Color.argb(
                    (Color.alpha(color) * 0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
            )

            overlay.setBackgroundColor(overlayColor)
            buttonAdd.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}