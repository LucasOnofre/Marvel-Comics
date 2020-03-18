package com.onoffrice.marvel_comics.ui.characters

import android.content.Context
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.ui.AppInjector
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.ui.characterDetail.createCharacterDetailActivityIntent
import com.onoffrice.marvel_comics.utils.extensions.startActivitySlideTransition
import kotlinx.android.synthetic.main.activity_characters.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class CharactersActivity : BaseActivity(R.layout.activity_characters) {

    private var isLoading: Boolean = false

    private val charactersAdapter: CharactersAdapter by lazy {
        val adapter = CharactersAdapter(object : CharactersAdapter.CharacterClickListener{
            override fun onClickCharacter(character: Character) {
                startActivitySlideTransition(createCharacterDetailActivityIntent(character))
            }
        })

        val layoutManager          = GridLayoutManager(this, 2)
        charactersRv.layoutManager = layoutManager
        charactersRv.adapter       = adapter
        charactersRv.addOnScrollListener(onScrollListener())
        adapter
    }

    private val viewModel by lazy {
        val factory = AppInjector.getCharacterViewModelFactory()
        ViewModelProvider(this, factory).get(CharactersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.getCharacters()
    }

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount         = recyclerView.layoutManager?.childCount ?: 0
                val totalItemCount           = recyclerView.layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= MifareUltralight.PAGE_SIZE
                ) {
                    isLoading = true
                    displayLoading(isLoading)
                    viewModel.getCharacters(totalItemCount)
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.run {
            characters.observe(this@CharactersActivity,  Observer { displayCharactersList(it)})
            loadingEvent.observe(this@CharactersActivity,Observer { displayLoading(it) })
            errorEvent.observe(this@CharactersActivity,  Observer { displayError(it) })
        }
    }

    private fun displayCharactersList(characters: List<Character>) {
        isLoading = false
        charactersAdapter.setCharacters(characters)
    }

    private fun displayLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    private fun displayError(message: String) {
      toast(message)
    }
}
fun Context.createCharactersActivityIntent() = intentFor<CharactersActivity>()

