package io.github.feelfreelinux.wykopmobilny.ui.suggestions

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView

class WykopSuggestionsTokenizer(
    val setUsersAdapter: () -> Unit,
    val setHashTagsAdapter: () -> Unit
) : MultiAutoCompleteTextView.Tokenizer {
    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length

        while (i > 0 && text[i - 1] == ' ') {
            i--
        }

        return if (i > 0 && text[i - 1] == ' ') {
            text
        } else {
            if (text is Spanned) {
                val sp = SpannableString(text.toString() + " ")
                TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                sp
            } else {
                text.toString() + " "
            }
        }
    }

    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor

        while (i > 0 && text[i - 1] != '@' && text[i - 1] != '#') {
            i--
        }

        //Check if token really started with @, else we don't have a valid token
        return if (i < 1 || (text[i - 1] != '@' && text[i - 1] != '#')) {
            cursor
        } else if (text[i - 1] == '@') {
            setUsersAdapter()
            i
        } else if (text[i - 1] == '#') {
            setHashTagsAdapter()
            i
        } else cursor

    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        val len = text.length

        while (i < len) {
            if (text[i] == ' ') {
                return i
            } else {
                i++
            }
        }

        return len
    }
}