package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionAdapter : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        val AUTHORITY = "io.github.feelfreelinux.SearchSuggestionAdapter"
        val MODE = DATABASE_MODE_QUERIES
    }
}