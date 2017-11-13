package io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.dialog_voters.*
import javax.inject.Inject


class VotersDialogFragment : DialogFragment(), VotersDialogView {
    @Inject lateinit var presenter : VotersDialogPresenter

    companion object {
        val ENTRY_ID_EXTRA = "ENTRY_ID"
        val COMMENT_ID_EXTRA = "COMMENT_ID"

        fun newInstance(id : Int, isComment : Boolean = false) : VotersDialogFragment {
            val votersDialogFragment = VotersDialogFragment()
            val bundle = Bundle()
            if (isComment) bundle.putInt(COMMENT_ID_EXTRA, id)
            else bundle.putInt(ENTRY_ID_EXTRA, id)
            votersDialogFragment.arguments = bundle
            return votersDialogFragment

        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)

        if (arguments.containsKey(COMMENT_ID_EXTRA)) {
            presenter.commentId = arguments.getInt(COMMENT_ID_EXTRA)
            presenter.getCommentVoters()
        } else {
            presenter.entryId = arguments.getInt(ENTRY_ID_EXTRA)
            presenter.getVoters()
        }


        val votersDialogBuilder = AlertDialog.Builder(context)
        val votersView = View.inflate(context, R.layout.dialog_voters, null)
        votersDialogBuilder.apply {
            setTitle(R.string.voters)
            setPositiveButton(android.R.string.ok, null)
            setView(votersView)
        }
        return votersDialogBuilder.create()
    }

    override fun showVoters(voters : List<Voter>) {
        dialog.progressView.isVisible = false
        val spannableStringBuilder = SpannableStringBuilder()
        voters
                .map { it.author }
                .forEachIndexed {
                    index, author ->
                    val span = ForegroundColorSpan(getGroupColor(author.group))
                    spannableStringBuilder.appendNewSpan(author.nick, span, 0)
                    if (index < voters.size - 1) spannableStringBuilder.append(", ")
                }
        dialog.votersTextView.text = spannableStringBuilder
    }

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}