package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry

import android.content.Context
import android.util.AttributeSet
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog.VotersDialogFragment
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import javax.inject.Inject

class EntryVoteButton : BaseVoteButton, EntryVoteButtonView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var presenter : EntryVoteButtonPresenter

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

    fun setEntryData(entry : Entry) {
        presenter.entryId = entry.id
        voteCount = entry.voteCount
        if (entry.voteCount > 0) {
            setOnLongClickListener {
                val activityContext = getActivityContext()
                if (activityContext is BaseActivity) {
                    VotersDialogFragment.newInstance(entry.id, false)
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
