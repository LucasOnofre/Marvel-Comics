package com.onoffrice.marvel_comics.ui.mostExpensiveComic

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.ui.AppInjector
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.activity_most_expensive_comic.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class MostExpensiveComicActivity : BaseActivity(R.layout.activity_most_expensive_comic) {

    private val viewModel by lazy {
        val factory = AppInjector.getMostExpensiveComicViewModelFactory()
        ViewModelProvider(this, factory).get(MostExpensiveComicViewModel::class.java)
    }

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
        comicTitle.text = comic.title
        comicDescription.text = comic.description
        comicPrice.text = getString(R.string.comic_price, comic.prices[0].price.toString())
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

