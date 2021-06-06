package io.github.wykopmobilny.ui.modules.search

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import androidx.cursoradapter.widget.SimpleCursorAdapter
import io.github.wykopmobilny.databinding.HistorySuggestionItemBinding
import io.github.wykopmobilny.utils.layoutInflater
import java.net.URLDecoder

class SuggestionsSimpleCursorAdapter(
    context: Context,
    layout: Int,
    c: Cursor,
    from: Array<String>,
    to: IntArray,
    flags: Int
) : SimpleCursorAdapter(context, layout, c, from, to, flags) {

    override fun convertToString(cursor: Cursor): CharSequence {
        val indexColumnSuggestion = cursor.getColumnIndex(SuggestionDatabase.FIELD_SUGGESTION)
        return URLDecoder.decode(cursor.getString(indexColumnSuggestion), "UTF-8")
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        super.bindView(view, context, cursor)
        val binding = HistorySuggestionItemBinding.bind(view)
        binding.historySuggestion.text = convertToString(cursor)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View =
        HistorySuggestionItemBinding.inflate(context.layoutInflater, parent, false).root
}
