package io.github.feelfreelinux.wykopmobilny.ui.suggestions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagSuggestion
import io.github.feelfreelinux.wykopmobilny.models.dataclass.WykopSuggestion
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.autosuggest_item.view.*

class UsersSuggestionsAdapter(context: Context, val suggestionApi: SuggestApi) : ArrayAdapter<Author>(context, R.layout.autosuggest_item), Filterable {
    val mData = arrayListOf<Author>()
    val mFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence =
                (resultValue as Author).nick

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null) {
                val data = ArrayList<Author>()
                if (constraint.matches("[\\w-]+".toRegex())) data.addAll(suggestionApi.getUserSuggestions(constraint.toString()).blockingGet())
                filterResults.values = data.toList()
                filterResults.count = data.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mData.clear()
            if (results != null && results.count > 0) {
                mData.addAll(results.values as List<Author>)
                notifyDataSetChanged()
            } else notifyDataSetInvalidated()
        }
    }

    override fun getCount() = mData.size

    override fun getItem(index: Int) = mData[index]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: View.inflate(context, R.layout.autosuggest_item, null)
        val item = mData[position]
        view.textView.setTextColor(getGroupColor(item.group, false))
        view.textView.text = "@${item.nick}"
        return view
    }

    override fun getFilter(): Filter {
        return mFilter
    }
}