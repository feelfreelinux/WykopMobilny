package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.FullConversation
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.ui.adapters.PMMessageAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ExitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.activity_conversation.view.*
import javax.inject.Inject

class ConversationActivity : BaseActivity(), ConversationView, InputToolbarListener {
    @Inject lateinit var conversationAdapter : PMMessageAdapter
    val user by lazy { intent.getStringExtra(EXTRA_USER) }
    var receiver : Author? = null
    @Inject lateinit var presenter: ConversationPresenter
    @Inject lateinit var userManagerApi : UserManagerApi
    @Inject lateinit var suggestionApi : SuggestApi
    lateinit var conversationDataFragment: DataFragment<FullConversation>

    companion object {
        val EXTRA_USER = "USER"
        val DATA_FRAGMENT_TAG = "CONVERSATION_TAG"

        fun createIntent(context: Context, user: String): Intent {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(ConversationActivity.EXTRA_USER, user)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        conversationDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user
        swiperefresh.setOnRefreshListener { presenter.loadConversation() }
        inputToolbar.setCustomHint(getString(R.string.reply))

        presenter.subscribe(this)
        presenter.user = user
        recyclerView?.apply {
            prepare()
            adapter = conversationAdapter
            (layoutManager as LinearLayoutManager).reverseLayout = true
            setHasFixedSize(false)
        }

        if (conversationDataFragment.data == null) {
            toolbar.avatarview.isVisible = false
            loadingView?.isVisible = true
            presenter.loadConversation()
        } else {
            showConversation(conversationDataFragment.data!!)
        }

        inputToolbar.inputToolbarListener = this
        inputToolbar.setup(userManagerApi, suggestionApi)

    }

    override fun showConversation(conversation: FullConversation) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        receiver = conversation.receiver
        toolbar.apply {
            subtitle = if (conversation.messages.isNotEmpty()) conversation.messages.last().date else null
            avatarview.setAuthor(conversation.receiver)
            avatarview.isVisible = true
            avatarview.setOnClickListener {
                getActivityContext()!!.startActivity(ProfileActivity.createIntent(getActivityContext()!!, conversation.receiver.nick))
            }
        }

        avatarview.setOnClickListener {
            startActivity(ProfileActivity.createIntent(this, receiver?.nick!!))
        }

        conversationAdapter.apply {
            messages.clear()
            messages.addAll(conversation.messages.reversed())
            notifyDataSetChanged()
        }
        recyclerView?.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        receiver?.let {
            conversationDataFragment.data =
                    FullConversation(conversationAdapter.messages.reversed(), receiver!!)
        }
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

    override fun sendPhoto(photo: String?, body: String, containsAdultContent : Boolean) {
        presenter.sendMessage(body, photo, containsAdultContent)
    }

    override fun sendPhoto(photo: TypedInputStream, body: String, containsAdultContent: Boolean) {
        presenter.sendMessage(body, photo, containsAdultContent)
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