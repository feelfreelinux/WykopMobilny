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
import io.github.wykopmobilny.models.dataclass.TagSuggestion
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.printout

class HashTagsSuggestionsAdapter(
    context: Context,
    private val suggestionApi: SuggestApi
) : ArrayAdapter<TagSuggestion>(context, R.layout.autosuggest_item), Filterable {

    val items = arrayListOf<TagSuggestion>()

    override fun getCount() = items.size

    override fun getItem(index: Int) = items[index]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        printout(items.size.toString())
        val view = convertView?.let(AutosuggestItemBinding::bind) ?: AutosuggestItemBinding.inflate(context.layoutInflater)
        val item = items[position]
        view.textView.text = "${item.tag} (${item.followers})"
        view.avatarView.isVisible = false
        return view.root
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
