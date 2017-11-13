package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment

import android.content.Context
import android.util.AttributeSet
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog.VotersDialogFragment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import javax.inject.Inject

class EntryCommentVoteButton : BaseVoteButton, EntryCommentVoteButtonView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var presenter : EntryCommentVoteButtonPresenter

    init {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
    }

    override fun unvote() {
        presenter.unvote()
    }

    override fun vote() {
        presenter.vote()
    }

    fun setCommentData(comment : EntryComment) {
        presenter.commentId = comment.id
        voteCount = comment.voteCount

        if (comment.voteCount > 0) {
            setOnLongClickListener {
                val activityContext = getActivityContext()
                if (activityContext is BaseActivity) {
                    VotersDialogFragment.newInstance(comment.id, true)
                            .show(
                                    activityContext.supportFragmentManager,
                                    "voteCountDialog"
                            )
                }
                true
            }
        }else setOnLongClickListener(null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }
}
