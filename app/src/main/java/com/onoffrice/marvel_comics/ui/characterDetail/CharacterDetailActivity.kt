package com.onoffrice.marvel_comics.ui.characterDetail

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.ui.mostExpensiveComic.createMostExpensiveComicIntent
import com.onoffrice.marvel_comics.utils.extensions.isNetworkConnected
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import com.onoffrice.marvel_comics.utils.extensions.startActivitySlideTransition
import kotlinx.android.synthetic.main.activity_character_detail.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class CharacterDetailActivity : BaseActivity(R.layout.activity_character_detail) {

    private val viewModel by inject<CharacterDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getExtras(intent.extras)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        mostExpensiveComicBtn.setOnClickListener {
            if (isNetworkConnected()) {
                viewModel.getCharacterComics()
            } else {
                toast(getString(R.string.no_network_connection))
            }
        }
    }

    private fun setObservers() {
        viewModel.run {
            character.observe(this@CharacterDetailActivity,    Observer { displayCharactersDetail(it) })
            comic.observe(this@CharacterDetailActivity,        Observer { openMostExpensiveCharacterComics(it) })
            loadingEvent.observe(this@CharacterDetailActivity, Observer { displayLoading(it) })
            errorEvent.observe(this@CharacterDetailActivity,   Observer { displayError(it) })
        }
    }

    private fun openMostExpensiveCharacterComics(comic: ComicModel) {
        startActivitySlideTransition(createMostExpensiveComicIntent(comic))
    }

    private fun displayCharactersDetail(character: Character) {
        setToolbar(character.name,true)
        characterBio.text  = character.description

        //Load's the character poster using Picasso
        characterPoster.loadImage(character.thumbnail.getPathExtension())
    }

    private fun displayLoading(loading: Boolean) {
        progressBar.setVisible(loading)
    }

    private fun displayError(message: String) {
      toast(message)
    }
}
fun Context.createCharacterDetailActivityIntent(character: Character) =
    intentFor<CharacterDetailActivity>(Constants.EXTRA_CHARACTER_DETAIL to character)

