package io.github.wykopmobilny.ui.suggestions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.databinding.AutosuggestItemBinding
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.layoutInflater

class UsersSuggestionsAdapter(
    context: Context,
    private val suggestionApi: SuggestApi
) : ArrayAdapter<Author>(context, R.layout.autosuggest_item), Filterable {

    val items = arrayListOf<Author>()
    private val itemsFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence =
            (resultValue as Author).nick

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null) {
                val data = ArrayList<Author>()
                if (constraint.matches("[\\w-]+".toRegex())) {
                    data.addAll(suggestionApi.getUserSuggestions(constraint.toString()).blockingGet())
                }
                filterResults.values = data.toList()
                filterResults.count = data.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            items.clear()
            if (results != null && results.count > 0) {
                items.addAll(results.values as List<Author>)
                notifyDataSetChanged()
            } else notifyDataSetInvalidated()
        }
    }

    override fun getCount() = items.size

    override fun getItem(index: Int) = items[index]

    override fun getFilter() = itemsFilter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?.let(AutosuggestItemBinding::bind) ?: AutosuggestItemBinding.inflate(context.layoutInflater)
        val item = items[position]
        view.textView.setTextColor(getGroupColor(item.group, false))
        view.textView.text = item.nick
        view.avatarView.isVisible = true
        view.avatarView.setAuthor(item)
        return view.root
    }
}
