package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

fun Context.editEntry(body: String, entryId : Int) {
    val intent = Intent(this, EditEntryActivity::class.java)
    intent.putExtra(BaseInputActivity.EXTRA_BODY, body)
    intent.putExtra(EditEntryActivity.EXTRA_ENTRY_ID, entryId)
    startActivity(intent)
}

class EditEntryActivity : BaseInputActivity<EditEntryPresenter>(), EditEntryView {
    @Inject override lateinit var presenter: EditEntryPresenter
    override val entryId by lazy { intent.getIntExtra(EXTRA_ENTRY_ID, 0) }

    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"

        fun createIntent(context: Context, body : String, entryId: Int): Intent {
            val intent = Intent(context, EditEntryActivity::class.java)
            intent.putExtra(BaseInputActivity.EXTRA_BODY, body)
            intent.putExtra(EditEntryActivity.EXTRA_ENTRY_ID, entryId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        supportActionBar?.setTitle(R.string.edit_entry)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}