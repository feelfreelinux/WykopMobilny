package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_write_comment.*
import javax.inject.Inject

class AddEntryActivity : BaseInputActivity<AddEntryPresenter>(), AddEntryActivityView {

    companion object {
        fun createIntent(context: Activity, receiver: String?, textBody: String? = null) =
            Intent(context, AddEntryActivity::class.java).apply {
                putExtra(EXTRA_BODY, textBody)
                putExtra(EXTRA_RECEIVER, receiver)
            }
    }

    @Inject override lateinit var suggestionApi: SuggestApi

    @Inject override lateinit var presenter: AddEntryPresenter

    val navigator: NewNavigatorApi by lazy { NewNavigator(this) }

    override fun openEntryActivity(id: Int) {
        navigator.openEntryDetailsActivity(id, false)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribe(this)
        setupSuggestions()
        supportActionBar?.setTitle(R.string.add_new_entry)

        if (intent.action == Intent.ACTION_SEND && intent.type != null) {
            if (intent.type == "text/plain") {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                text?.let {
                    textBody = text
                }
            } else if (intent!!.type!!.startsWith("image/")) {
                val imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
                imageUri?.let {
                    markupToolbar.photo = imageUri
                    printout(imageUri.toString())
                }
            }
        }
    }
}
