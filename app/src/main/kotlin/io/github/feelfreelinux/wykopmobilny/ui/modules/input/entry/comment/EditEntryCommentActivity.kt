package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

fun Context.editEntryComment(body : String, entryId : Int, commentId : Int) {
    val intent = Intent(this, EditEntryCommentActivity::class.java)
    intent.apply {
        putExtra(EditEntryCommentActivity.EXTRA_ENTRY_ID, entryId)
        putExtra(EditEntryCommentActivity.EXTRA_COMMENT_ID, commentId)
        putExtra(BaseInputActivity.EXTRA_BODY, body)
    }
    startActivity(intent)
}

class EditEntryCommentActivity : BaseInputActivity<EditEntryCommentPresenter>(), EditEntryCommentView {
    override val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, 0) }
    override val commentId by lazy { intent.getIntExtra(EXTRA_COMMENT_ID, 0) }
    @Inject override lateinit var presenter : EditEntryCommentPresenter

    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"
        val EXTRA_COMMENT_ID = "COMMENT_ID"

        fun createIntent(context : Context, body : String, entryId : Int, commentId : Int): Intent {
            val intent = Intent(context, EditEntryCommentActivity::class.java)
            intent.apply {
                putExtra(EditEntryCommentActivity.EXTRA_ENTRY_ID, entryId)
                putExtra(EditEntryCommentActivity.EXTRA_COMMENT_ID, commentId)
                putExtra(BaseInputActivity.EXTRA_BODY, body)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        supportActionBar?.setTitle(R.string.edit_comment)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}