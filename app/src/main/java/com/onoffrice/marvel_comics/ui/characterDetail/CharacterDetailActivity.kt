package com.onoffrice.marvel_comics.ui.characterDetail

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.ui.AppInjector
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import kotlinx.android.synthetic.main.activity_character_detail.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class CharacterDetailActivity : BaseActivity(R.layout.activity_character_detail) {

    private val viewModel by lazy {
        val factory = AppInjector.getCharacterDetailViewModelFactory()
        ViewModelProvider(this, factory).get(CharacterDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getExtras(intent.extras)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        mostExpensiveComicBtn.setOnClickListener {
            viewModel.getCharacterComics()
        }
    }

    private fun setObservers() {
        viewModel.run {
            character.observe(this@CharacterDetailActivity,    Observer { displayCharactersDetail(it)})
            loadingEvent.observe(this@CharacterDetailActivity, Observer { displayLoading(it) })
            errorEvent.observe(this@CharacterDetailActivity,   Observer { displayError(it) })
        }
    }

    private fun displayCharactersDetail(character: Character) {
        setToolbar(character.name,true)
        characterBio.text  = character.description

        //Loads the character poster using Picasso
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

