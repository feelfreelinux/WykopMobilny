package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.confirmdetails.AddLinkDetailsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.duplicateslist.AddLinkDuplicatesListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput.AddlinkUrlInputFragment
import kotlinx.android.synthetic.main.toolbar.*

class AddlinkActivity : BaseActivity() {
    lateinit var draft : NewLinkResponse

    companion object {
        fun createIntent(context : Context) : Intent {
            return Intent(context, AddlinkActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addlink)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.add_new_link)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        openFragment(AddlinkUrlInputFragment.newInstance(), "url_input")
    }

    fun openFragment(fragment: Fragment, tag : String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentView, fragment, tag)
                // Add this transaction to the back stack
                .addToBackStack(null)
                .commit()
    }

    fun openDuplicatesActivity(response : NewLinkResponse) {
        draft = response
        openFragment(AddLinkDuplicatesListFragment.newInstance(), "duplicates_list")
    }

    fun openDetailsScreen() {
        openFragment(AddLinkDetailsFragment.newInstance(), "details_fragment")
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        }
        else {
            super.onBackPressed()
        }
    }
}