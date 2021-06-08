package io.github.wykopmobilny.ui.modules.search

import android.content.SearchRecentSuggestionsProvider
import io.github.wykopmobilny.BuildConfig

class SearchSuggestionAdapter : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.SearchSuggestionAdapter"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}
