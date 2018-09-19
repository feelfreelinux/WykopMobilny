package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

class EditEntryCommentActivity : BaseInputActivity<EditEntryCommentPresenter>(), EditEntryCommentView {

    companion object {
        const val EXTRA_ENTRY_ID = "ENTRY_ID"
        const val EXTRA_COMMENT_ID = "COMMENT_ID"

        fun createIntent(context: Context, body: String, entryId: Int, commentId: Int) =
            Intent(context, EditEntryCommentActivity::class.java).apply {
                putExtra(EXTRA_ENTRY_ID, entryId)
                putExtra(EXTRA_COMMENT_ID, commentId)
                putExtra(EXTRA_BODY, body)
            }
    }

    @Inject override lateinit var suggestionApi: SuggestApi
    @Inject override lateinit var presenter: EditEntryCommentPresenter

    override val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, 0) }
    override val commentId by lazy { intent.getIntExtra(EXTRA_COMMENT_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSuggestions()
        presenter.subscribe(this)
        supportActionBar?.setTitle(R.string.edit_comment)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun exitActivity() {
        val data = Intent()
        data.putExtra("commentId", commentId)
        data.putExtra("commentBody", textBody)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}