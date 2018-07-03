package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import kotlinx.android.synthetic.main.activity_write_comment.*
import javax.inject.Inject

class AddEntryActivity : BaseInputActivity<AddEntryPresenter>(), AddEntryActivityView {
    @Inject override lateinit var suggestionApi: SuggestApi
    @Inject override lateinit var presenter: AddEntryPresenter
    @Inject lateinit var navigator : NewNavigatorApi

    override fun openEntryActivity(id: Int) {
        navigator.openEntryDetailsActivity(id, false)
        finish()
    }

    companion object {
        fun createIntent(context : Activity, receiver: String?, textBody : String? = null) : Intent {
            val intent = Intent(context, AddEntryActivity::class.java)
            intent.putExtra(BaseInputActivity.EXTRA_BODY, textBody)
            intent.putExtra(BaseInputActivity.EXTRA_RECEIVER, receiver)
            return intent
        }
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

            } else if (intent.type.startsWith("image/")) {
                val imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri?
                imageUri?.let {
                    markupToolbar.photo = imageUri
                }
            }
        }
    }
}