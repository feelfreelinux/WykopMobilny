package io.github.wykopmobilny.ui.adapters.viewholders

import android.text.Selection
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.databinding.BlacklistBlockFormItemBinding
import io.github.wykopmobilny.ui.suggestions.HashTagsSuggestionsAdapter
import io.github.wykopmobilny.ui.suggestions.UsersSuggestionsAdapter
import io.github.wykopmobilny.ui.suggestions.WykopSuggestionsTokenizer

class BlacklistBlockViewHolder(private val binding: BlacklistBlockFormItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(isUserView: Boolean, blockListener: (String) -> Unit, suggestApi: SuggestApi) {
        binding.apply {
            val usersSuggestionAdapter = UsersSuggestionsAdapter(root.context, suggestApi)
            val hashTagsSuggestionAdapter = HashTagsSuggestionsAdapter(root.context, suggestApi)
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
