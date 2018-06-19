package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.support.v7.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SuggestionDatabase
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SuggestionsSimpleCursorAdapter
import io.github.feelfreelinux.wykopmobilny.utils.hideKeyboard
import rx.subjects.PublishSubject


class HistorySuggestionListener(val context : Context, val searchView : SearchView, val queryListener : (String) -> Unit) : SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    val database by lazy { SuggestionDatabase(context) }

    override fun onSuggestionSelect(position: Int): Boolean {

        return false
    }

    override fun onSuggestionClick(position: Int): Boolean {

        val cursor = searchView.suggestionsAdapter.getItem(position) as SQLiteCursor
        val indexColumnSuggestion = cursor.getColumnIndex(SuggestionDatabase.FIELD_SUGGESTION)

        searchView.setQuery(cursor.getString(indexColumnSuggestion), false)

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (query.length > 2) {
            searchView.clearFocus()
            queryListener(query)
            val result = database.insertSuggestion(query)
            return result.toInt() != -1
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {

        val cursor = database.getSuggestions(newText)

            val columns = arrayOf<String>(SuggestionDatabase.FIELD_SUGGESTION)
            val columnTextId = intArrayOf(android.R.id.text1)

            val simple = SuggestionsSimpleCursorAdapter(context,
                    R.layout.history_suggestion_item, cursor,
                    columns, columnTextId, 0)
            searchView.suggestionsAdapter = simple
            return true
    }
}