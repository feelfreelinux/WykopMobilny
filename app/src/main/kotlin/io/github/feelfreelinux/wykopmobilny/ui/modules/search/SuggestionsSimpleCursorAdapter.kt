package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.content.Context
import android.database.Cursor
import androidx.cursoradapter.widget.SimpleCursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import kotlinx.android.synthetic.main.history_suggestion_item.view.*
import java.net.URLDecoder

class SuggestionsSimpleCursorAdapter(val context: Context, layout: Int, c: Cursor, from: Array<String>, to: IntArray, flags: Int) : androidx.cursoradapter.widget.SimpleCursorAdapter(context, layout, c, from, to, flags) {
    val inflater by lazy {
        context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }

    override fun convertToString(cursor: Cursor): CharSequence {
        val indexColumnSuggestion = cursor.getColumnIndex(SuggestionDatabase.FIELD_SUGGESTION)

        return URLDecoder.decode(cursor.getString(indexColumnSuggestion), "UTF-8")
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        super.bindView(view, context, cursor)

        view.historySuggestion.text = convertToString(cursor)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return inflater.inflate(R.layout.history_suggestion_item,  parent, false)
    }
}