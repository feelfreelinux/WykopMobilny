package io.github.wykopmobilny.ui.modules.input.link.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.suggest.SuggestApi
import io.github.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

class LinkCommentEditActivity : BaseInputActivity<LinkCommentEditPresenter>(), LinkCommentEditView {

    companion object {
        private const val EXTRA_LINKID = "LINK_ID"
        private const val EXTRA_COMMENTID = "COMMENT_ID"

        fun createIntent(context: Context, commentId: Int, body: String, linkId: Int): Intent {
            val intent = Intent(context, LinkCommentEditActivity::class.java)
            intent.putExtra(EXTRA_BODY, body)
            intent.putExtra(EXTRA_LINKID, linkId)
            intent.putExtra(EXTRA_COMMENTID, commentId)
            return intent
        }
    }

    @Inject
    override lateinit var presenter: LinkCommentEditPresenter

    @Inject
    override lateinit var suggestionApi: SuggestApi

    val commentId by lazy { intent.getIntExtra(EXTRA_COMMENTID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribe(this)
        presenter.linkCommentId = intent.getIntExtra(EXTRA_LINKID, 0)
        setupSuggestions()
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
