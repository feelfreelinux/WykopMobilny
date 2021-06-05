package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Selection
import android.view.View
import androidx.core.widget.doAfterTextChanged
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer
import kotlinx.android.synthetic.main.blacklist_block_form_item.view.*

class BlacklistBlockViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    fun bind(isUserView: Boolean, blockListener: (String) -> Unit, suggestApi: SuggestApi) {
        view.apply {
            val usersSuggestionAdapter = UsersSuggestionsAdapter(context, suggestApi)
            val hashTagsSuggestionAdapter = HashTagsSuggestionsAdapter(context, suggestApi)
            val prefix = if (isUserView) "@" else "#"
            blockUserEditText.hint = if (isUserView) "Zablokuj użytkownika" else "Zablokuj tag"
            lockImageView.setOnClickListener {
                blockListener(blockUserEditText.text.toString().removePrefix(prefix))
            }
            blockUserEditText.setText(prefix, false)
            Selection.setSelection(blockUserEditText.text, blockUserEditText.text.length)

            blockUserEditText.doAfterTextChanged {
                if (!it.toString().startsWith(prefix)) {
                    blockUserEditText.setText(prefix)
                    Selection.setSelection(blockUserEditText.text, blockUserEditText.text.length)
                }
            }

            blockUserEditText.setTokenizer(
                WykopSuggestionsTokenizer(
                    {
                        if (blockUserEditText.adapter !is UsersSuggestionsAdapter) {
                            blockUserEditText.setAdapter(usersSuggestionAdapter)
                        }
                    },
                    {
                        if (blockUserEditText.adapter !is HashTagsSuggestionsAdapter) {
                            blockUserEditText.setAdapter(hashTagsSuggestionAdapter)
                        }
                    }
                )
            )
            blockUserEditText.threshold = 3
        }
    }
}
