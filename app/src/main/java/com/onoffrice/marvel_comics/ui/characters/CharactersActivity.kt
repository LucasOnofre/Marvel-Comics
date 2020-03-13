package com.onoffrice.marvel_comics.ui.characters

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.onoffrice.marvel_comics.R
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

//    private val viewModel by lazy {
//        val factory = AppInjector.getFaqViewModelFactory()
//        ViewModelProviders.of(this, factory).get(FaqViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar("")
        setObservers()
        setListeners()
        //viewModel.onViewCreated()
    }

    private fun setListeners() {
    }

    private fun setObservers() {
//        viewModel.run {
//            questions.observe(this@CharactersActivity, Observer { adapter.items = it })
//            loadingEvent.observe(this@CharactersActivity, Observer { showLoading(it) })
//            errorEvent.observe(this@CharactersActivity, Observer { displayError(it) })
//        }
    }

    private fun displayError(message: String) {
      toast(message)
    }

}
fun Context.createFaqIntent() = intentFor<CharactersActivity>()

