package com.onoffrice.marvel_comics.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.contains
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.onoffrice.marvel_comics.R

/**
 * Created by mobile2you on 18/08/16.
 */
abstract class BaseActivity(private val layoutRes: Int?) : AppCompatActivity() {

    protected val tag = javaClass.enclosingClass?.simpleName ?: javaClass.simpleName

    private var view: ViewGroup? = null
    private lateinit var loadingView: View

    val context: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        //FIX ORIENTATION TO PORTRAIT
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        view = (window.decorView.rootView as? ViewGroup)
    }

    //TOOLBAR METHODS
    fun setToolbar(title: String, displayHomeAsUpEnabled: Boolean) {
        setToolbar(title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
    }

    //ACTION BAR METHODS
    fun setToolbar(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(title)
    }

    //MENU METHODS
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //this is used because when user hits home button the previous view is reconstructed
                //and when back button (at navbar) is pressed this doesn't happen,
                //so this makes the previous view never reconstructed when home is hit.
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //LOADING METHODS
    /**
     * Allows the programmer to set a custom layout to be used as a loading
     * @param layoutRes a layout which will be used as loading
     */
    fun setLoadingLayout(@LayoutRes layoutRes: Int) {
        loadingView = View.inflate(this, layoutRes, null)
    }

    /**
     * Updates visibility of the loading
     * @param isLoading Whether the loading should be shown or not
     */
    fun showLoading(isLoading: Boolean) {
        view?.run {
            if (isLoading && !contains(loadingView)) {
                addView(loadingView)
            } else if (!isLoading && contains(loadingView)) {
                removeView(loadingView)
            }
        }
    }

    /**
     * Allows the programmer to set the loading visibility using a swipe refresh layout
     * @param isLoading Whether the loading should be shown or not
     * @param swipeView The view that is a swipe refresh that will work as loading instead of the default layout
     */
    fun showLoading(isLoading: Boolean, swipeView: SwipeRefreshLayout?) {
        swipeView?.let {
            it.isRefreshing = isLoading
        } ?: showLoading(isLoading)
    }
}