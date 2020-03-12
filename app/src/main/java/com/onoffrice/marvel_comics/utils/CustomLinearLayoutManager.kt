package com.onoffrice.marvel_comics.utils

import android.content.Context

class CustomLinearLayoutManager(context: Context) : androidx.recyclerview.widget.LinearLayoutManager(context) {
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}

