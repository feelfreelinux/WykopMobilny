package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.Context
import android.database.sqlite.SQLiteCursor
import androidx.appcompat.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.R
import java.net.URLEncoder


class HistorySuggestionListener(
    val context: Context,
    val queryListener: (String) -> Unit,
    private val searchView: SearchView
) : SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private val database by lazy { SuggestionDatabase(context) }

    override fun onSuggestionSelect(position: Int) = false

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
        val cursor = database.getSuggestions(URLEncoder.encode(newText, "UTF-8"))

        val columns = arrayOf(SuggestionDatabase.FIELD_SUGGESTION)
        val columnTextId = intArrayOf(android.R.id.text1)

        val simple = SuggestionsSimpleCursorAdapter(
            context,
            R.layout.history_suggestion_item, cursor,
            columns, columnTextId, 0
        )
        searchView.suggestionsAdapter = simple
        return true
    }
}