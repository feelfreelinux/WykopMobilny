package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionAdapter : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "io.github.feelfreelinux.SearchSuggestionAdapter"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}
