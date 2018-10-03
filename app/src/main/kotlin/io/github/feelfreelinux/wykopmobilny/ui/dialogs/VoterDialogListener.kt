package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.dialog_voters.view.*

typealias VotersDialogListener = (List<Voter>) -> Unit

fun createVotersDialogListener(
    dialog: com.google.android.material.bottomsheet.BottomSheetDialog,
    votersDialogView: View
): (List<Voter>) -> Unit = {
    if (dialog.isShowing) {
        votersDialogView.progressView.isVisible = false
        votersDialogView.votersTextView.isVisible = true
        val spannableStringBuilder = SpannableStringBuilder()
        it
            .map { voter -> voter.author }
            .forEachIndexed { index, author ->
                val span = ClickableUserSpan(author)
                spannableStringBuilder.appendNewSpan(author.nick, span, SPAN_EXCLUSIVE_EXCLUSIVE)
                if (index < it.size - 1) spannableStringBuilder.append(", ")
            }
        if (spannableStringBuilder.isEmpty()) {
            votersDialogView.votersTextView.gravity = Gravity.CENTER
            spannableStringBuilder.append(votersDialogView.context.getString(R.string.dialogNoVotes))
        }
        votersDialogView.votersTextView.movementMethod = (LinkMovementMethod.getInstance()) // Auuu
        votersDialogView.votersTextView.text = spannableStringBuilder
    }
}

class ClickableUserSpan(val author: Author) : ClickableSpan() {
    override fun onClick(authorView: View?) {
        val activity = authorView?.getActivityContext()
        activity?.startActivity(ProfileActivity.createIntent(activity, author.nick))
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = getGroupColor(author.group)
        ds.isUnderlineText = false
    }
}