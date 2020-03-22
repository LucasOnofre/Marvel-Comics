package com.onoffrice.marvel_comics.ui.mostExpensiveComic

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.activity_most_expensive_comic.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class MostExpensiveComicActivity : BaseActivity(R.layout.activity_most_expensive_comic) {

    private val viewModel by inject<MostExpensiveComicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getExtras(intent.extras)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
    }

    private fun setObservers() {
        viewModel.run {
            comic.observe(this@MostExpensiveComicActivity, Observer { displayComicDetail(it)})
            loadingEvent.observe(this@MostExpensiveComicActivity,Observer { displayLoading(it) })
            errorEvent.observe(this@MostExpensiveComicActivity, Observer { displayError(it) })
        }
    }

    private fun displayComicDetail(comic: ComicModel) {
        comicPoster.loadImage(comic.thumbnail.getPathExtension())
        comicTitle.text       = comic.title
        comicPrice.text       = getString(R.string.comic_price, comic.prices[0].price.toString())
        comicDescription.text = comic.description
    }

    private fun displayLoading(loading: Boolean) {
        progressBar.setVisible(loading)
    }

    private fun displayError(message: String) {
      toast(message)
    }
}
fun Context.createMostExpensiveComicIntent(comic: ComicModel) =
    intentFor<MostExpensiveComicActivity>(Constants.EXTRA_COMIC to comic)

