package io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

class LinkCommentEditActivity : BaseInputActivity<LinkCommentEditPresenter>(), LinkCommentEditView {
    @Inject override lateinit var presenter: LinkCommentEditPresenter
    @Inject override lateinit var suggestionApi: SuggestApi

    companion object {
        val EXTRA_LINKID = "LINK_ID"

        fun createIntent(context: Context, body : String, linkId: Int): Intent {
            val intent = Intent(context, LinkCommentEditActivity::class.java)
            intent.putExtra(BaseInputActivity.EXTRA_BODY, body)
            intent.putExtra(EXTRA_LINKID, linkId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribe(this)
        presenter.linkCommentId = intent.getIntExtra(EXTRA_LINKID, 0)
        setupSuggestions()
        supportActionBar?.setTitle(R.string.edit_comment)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}