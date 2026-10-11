package dev.appoutlet.outpost.feature.article

import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {
    init {
        println("ArticleViewModel started")
    }

    override fun onCleared() {
        super.onCleared()
        println("ArticleViewModel cleared")
    }
}
