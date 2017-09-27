package io.github.feelfreelinux.wykopmobilny.ui.input.entry.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.input.BaseInputPresenter
import javax.inject.Inject

fun Context.createNewEntry(receiver: String?) {
    val intent = Intent(this, AddEntryActivity::class.java)
    intent.putExtra(BaseInputActivity.EXTRA_RECEIVER, receiver)
    startActivity(intent)
}

class AddEntryActivity : BaseInputActivity<AddEntryPresenter>() {
    @Inject override lateinit var presenter: AddEntryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        supportActionBar?.setTitle(R.string.add_new_entry)
    }
}