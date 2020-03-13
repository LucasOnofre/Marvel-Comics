package com.onoffrice.marvel_comics.ui.characters

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.ui.AppInjector
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class CharactersActivity : BaseActivity(R.layout.activity_characters) {

//    private val adapter: FaqAdapter by lazy {
//        FaqAdapter(context = this).apply {
//            faqRv.setup(this, LinearLayoutManager(this@FaqActivity, LinearLayoutManager.VERTICAL, false))
//            faqRv.isNestedScrollingEnabled = true
//        }
//    }

    private val viewModel by lazy {
        val factory = AppInjector.getCharacterViewModelFactory()
        ViewModelProvider(this, factory).get(CharactersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar("")
        setObservers()
        setListeners()
        viewModel.getCharacters(10)
    }

    private fun setListeners() {
    }

    private fun setObservers() {
        viewModel.run {
            characters.observe(this@CharactersActivity, Observer { toast(it.toString())})
           // loadingEvent.observe(this@CharactersActivity, Observer { showLoading(it) })
            errorEvent.observe(this@CharactersActivity, Observer { displayError(it) })
        }
    }

    private fun displayError(message: String) {
      toast(message)
    }

}
fun Context.createCharactersActivityIntent() = intentFor<CharactersActivity>()

