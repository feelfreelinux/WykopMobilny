package io.github.feelfreelinux.wykopmobilny.ui.suggestions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagSuggestion
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.autosuggest_item.view.*

class HashTagsSuggestionsAdapter(
    context: Context,
    val suggestionApi: SuggestApi
) : ArrayAdapter<TagSuggestion>(context, R.layout.autosuggest_item), Filterable {

    val items = arrayListOf<TagSuggestion>()

    override fun getCount() = items.size

    override fun getItem(index: Int) = items[index]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        printout(items.size.toString())
        val view = convertView ?: View.inflate(context, R.layout.autosuggest_item, null)
        val item = items[position]
        view.textView.text = "${item.tag} (${item.followers})"
        view.avatarView.isVisible = false
        return view
    }

    override fun getFilter() = object : Filter() {

        override fun convertResultToString(resultValue: Any): CharSequence =
            (resultValue as TagSuggestion).tag.removePrefix("#")

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            constraint?.let {
                val items = ArrayList<TagSuggestion>()
                if (it.matches("[\\w-]+".toRegex())) {
                    items.addAll(suggestionApi.getTagSuggestions(it.toString()).blockingGet())
                }
                filterResults.values = items.toList()
                filterResults.count = items.size
            }
            return filterResults
        }

        override fun publishResults(contraint: CharSequence?, results: FilterResults?) {
            items.clear()
            if (results != null && results.count > 0) {
                items.addAll(results.values as List<TagSuggestion>)
                notifyDataSetChanged()
            } else notifyDataSetInvalidated()
        }
    }
}