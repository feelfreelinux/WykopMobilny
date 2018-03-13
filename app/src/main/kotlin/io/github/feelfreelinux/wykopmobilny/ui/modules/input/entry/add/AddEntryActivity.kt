package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import javax.inject.Inject

class AddEntryActivity : BaseInputActivity<AddEntryPresenter>() {
    @Inject override lateinit var suggestionApi: SuggestApi
    @Inject override lateinit var presenter: AddEntryPresenter

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
    }
}