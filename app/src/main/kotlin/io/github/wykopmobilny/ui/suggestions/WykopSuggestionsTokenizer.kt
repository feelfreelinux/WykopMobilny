package io.github.wykopmobilny.ui.suggestions

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView

class WykopSuggestionsTokenizer(
    val setUsersAdapter: () -> Unit,
    val setHashTagsAdapter: () -> Unit
) : MultiAutoCompleteTextView.Tokenizer {

    override fun terminateToken(text: CharSequence): CharSequence {
        var index = text.length

        while (index > 0 && text[index - 1] == ' ') {
            index--
        }

        return if (index > 0 && text[index - 1] == ' ') {
            text
        } else {
            if (text is Spanned) {
                val sp = SpannableString("$text ")
                TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                sp
            } else {
                "$text "
            }
        }
    }

    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var index = cursor

        while (index > 0 && text[index - 1] != '@' && text[index - 1] != '#') {
            index--
        }

        // Check if token really started with @, else we don't have a valid token
        return if (index < 1 || (text[index - 1] != '@' && text[index - 1] != '#')) {
            cursor
        } else if (text[index - 1] == '@') {
            setUsersAdapter()
            index
        } else if (text[index - 1] == '#') {
            setHashTagsAdapter()
            index
        } else cursor
    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var index = cursor
        val len = text.length

        while (index < len) {
            if (text[index] == ' ') {
                return index
            } else {
                index++
            }
        }

        return len
    }
}
