package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.adapters.WykopSuggestionsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import kotlinx.android.synthetic.main.activity_write_comment.*
import javax.inject.Inject

class AddEntryActivity : BaseInputActivity<AddEntryPresenter>() {
    @Inject override lateinit var suggestionApi: SuggestApi
    @Inject override lateinit var presenter: AddEntryPresenter

    companion object {
        fun createIntent(context : Context, receiver: String?) : Intent {
            val intent = Intent(context, AddEntryActivity::class.java)
            intent.putExtra(BaseInputActivity.EXTRA_RECEIVER, receiver)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        body.setAdapter(WykopSuggestionsAdapter(this, R.layout.autosuggest_item, suggestionApi))
        supportActionBar?.setTitle(R.string.add_new_entry)
    }
}