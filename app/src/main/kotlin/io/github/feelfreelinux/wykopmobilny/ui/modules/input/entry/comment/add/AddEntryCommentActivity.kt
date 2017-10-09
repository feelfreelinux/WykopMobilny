package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

fun Context.createNewEntryComment(entryId : Int, receiver : String?) {
    val intent = Intent(this, AddEntryCommentActivity::class.java)
    intent.putExtra(BaseInputActivity.EXTRA_RECEIVER, receiver)
    intent.putExtra(AddEntryCommentActivity.EXTRA_ENTRY_ID, entryId)
    startActivity(intent)
}

class AddEntryCommentActivity : BaseInputActivity<AddEntryCommentPresenter>(), AddEntryCommentView {
    override val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, -1) }
    @Inject override lateinit var presenter: AddEntryCommentPresenter

    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        supportActionBar?.setTitle(R.string.add_comment)
    }

}