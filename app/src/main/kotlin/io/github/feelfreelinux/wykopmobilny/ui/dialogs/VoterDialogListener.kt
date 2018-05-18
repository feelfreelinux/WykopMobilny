package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.support.design.widget.BottomSheetDialog
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.dialog_voters.view.*

typealias VotersDialogListener = (List<Voter>) -> Unit
fun CreateVotersDialogListener(dialog : BottomSheetDialog, votersDialogView : View) : (List<Voter>) -> Unit = {
    if (dialog.isShowing) {
        votersDialogView.progressView.isVisible = false
        votersDialogView.votersTextView.isVisible = true
        val spannableStringBuilder = SpannableStringBuilder()
        it
                .map { it.author }
                .forEachIndexed { index, author ->
                    val span = ForegroundColorSpan(getGroupColor(author.group))
                    spannableStringBuilder.appendNewSpan(author.nick, span, 0)
                    if (index < it.size - 1) spannableStringBuilder.append(", ")
                }
        if (spannableStringBuilder.isEmpty()) {
            votersDialogView.votersTextView.gravity = Gravity.CENTER
            spannableStringBuilder.append(votersDialogView.context.getString(R.string.dialogNoVotes))
        }
        votersDialogView.votersTextView.text = spannableStringBuilder
    }
}