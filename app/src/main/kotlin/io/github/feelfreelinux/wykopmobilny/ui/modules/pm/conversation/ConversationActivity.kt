package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.R.id.toolbar
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.ui.adapters.PMMessageAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.openConversationActivity(user : String) {
    val intent = Intent(this, ConversationActivity::class.java)
    intent.putExtra(ConversationActivity.EXTRA_USER, user)
    startActivity(intent)
}

class ConversationActivity : BaseActivity(), ConversationView, InputToolbarListener {
    val conversationAdapter by lazy { PMMessageAdapter() }
    val user by lazy { intent.getStringExtra(EXTRA_USER) }
    @Inject lateinit var presenter : ConversationPresenter
    lateinit var conversationDataFragment : DataFragment<List<PMMessage>>

    companion object {
        val EXTRA_USER = "USER"
        val DATA_FRAGMENT_TAG = "CONVERSATION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        WykopApp.uiInjector.inject(this)
        conversationDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user
        swiperefresh.setOnRefreshListener { presenter.loadConversation() }
        inputToolbar.setCustomHint(getString(R.string.reply))

        presenter.subscribe(this)
        presenter.user = user
        recyclerView.apply {
            prepare()
            adapter = conversationAdapter
            (layoutManager as LinearLayoutManager).reverseLayout = true
        }

        if (conversationDataFragment.data == null) {
            loadingView.isVisible = true
            presenter.loadConversation()
        } else {
            showConversation(conversationDataFragment.data!!)
        }

        inputToolbar.inputToolbarListener = this

    }

    override fun showConversation(items: List<PMMessage>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        conversationAdapter.apply {
            messages.clear()
            messages.addAll(items.reversed())
            notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        conversationDataFragment.data = conversationAdapter.messages
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.insert_photo_galery)), BaseInputActivity.USER_ACTION_INSERT_PHOTO)
    }

    override fun onBackPressed() {
        if (inputToolbar.hasUserEditedContent()) {
            ExitConfirmationDialog(this, {
                finish()
            })?.show()
        } else finish()
    }

    override fun sendPhoto(photo: String?, body: String) {
        presenter.sendMessage(body, photo)
    }

    override fun sendPhoto(photo: TypedInputStream, body: String) {
        presenter.sendMessage(body, photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseInputActivity.USER_ACTION_INSERT_PHOTO -> {
                    inputToolbar.setPhoto(data?.data)
                }
            }
        }
    }

    override fun hideInputToolbar() {
        inputToolbar.isVisible = false
    }

    override fun hideInputbarProgress() {
        inputToolbar.showProgress(false)
    }

    override fun resetInputbarState() {
        inputToolbar.resetState()
    }
}