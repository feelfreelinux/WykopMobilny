package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.WykopSuggestion
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import kotlinx.android.synthetic.main.autosuggest_item.view.*


class WykopSuggestionsAdapter(context: Context, private val textViewResourceId: Int, val suggestApi: SuggestApi) : ArrayAdapter<WykopSuggestion>(context, textViewResourceId), Filterable {
    val mData = arrayListOf<WykopSuggestion>()

    override fun getCount() = mData.size

    override fun getItem(index: Int) = mData[index]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(context, textViewResourceId, null)
        val suggestion = mData[position]
        val tv = view.textView
        suggestion.author?.apply {
            tv.setTextColor(getGroupColor(group, false))
            tv.text = "@$nick"
        }

        suggestion.tag?.let {
            tv.text = "${it.tag} (${it.followers})"
        }
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any): CharSequence =
                    (resultValue as WykopSuggestion).replaceConstraint

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val text = constraint.toString()
                    mData.clear()
                    if (text.contains('@')) {
                        val typedText = text.substringAfterLast('@')
                        if (typedText.isNotEmpty()) {
                            if (!typedText.matches(".*([ \\t]).*".toRegex())) {
                                if (typedText.length >= 2) {
                                    val suggestions = suggestApi.getUserSuggestions(typedText)
                                            .blockingGet()
                                    mData.addAll(suggestions.subList(0, 7).map { WykopSuggestion(it, null, text.substringBeforeLast("@") + "@${it.nick}") })
                                }
                            }
                        }
                    }
                    if (text.contains('#')) {
                        val typedText = text.substringAfterLast('#')
                        if (typedText.isNotEmpty()) {
                            if (!typedText.matches(".*([ \\t]).*".toRegex())) {
                                if (typedText.length >= 2) {
                                    val suggestions = suggestApi.getTagSuggestions(typedText)
                                            .blockingGet()
                                    mData.addAll(suggestions.subList(0, 7).map { WykopSuggestion(null, it, text.substringBeforeLast("#") + it.tag) })
                                }
                            }
                        }
                    }

                    filterResults.values = mData
                    filterResults.count = mData.size
                }
                return filterResults
            }

            override fun publishResults(contraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}