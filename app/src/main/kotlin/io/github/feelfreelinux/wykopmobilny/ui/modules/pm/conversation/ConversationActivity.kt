package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.databinding.ActivityConversationBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.FullConversation
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.ui.adapters.PMMessageAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.exitConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbarListener
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.prepareNoDivider
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class ConversationActivity : BaseActivity(), ConversationView, InputToolbarListener {

    companion object {
        private const val EXTRA_USER = "USER"
        private const val DATA_FRAGMENT_TAG = "CONVERSATION_TAG"

        fun createIntent(context: Context, user: String) =
            Intent(context, ConversationActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
    }

    private val binding by viewBinding(ActivityConversationBinding::inflate)

    @Inject
    lateinit var conversationAdapter: PMMessageAdapter

    @Inject
    lateinit var presenter: ConversationPresenter

    @Inject
    lateinit var userManagerApi: UserManagerApi

    @Inject
    lateinit var suggestionApi: SuggestApi

    override val enableSwipeBackLayout = true
    override val isActivityTransfluent = true

    val user by lazy { intent.getStringExtra(EXTRA_USER)!! }
    var receiver: Author? = null

    lateinit var contentUri: Uri
    lateinit var conversationDataFragment: DataFragment<FullConversation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversationDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user
        binding.swiperefresh.setOnRefreshListener { presenter.loadConversation() }
        binding.inputToolbar.setCustomHint(getString(R.string.reply))

        presenter.subscribe(this)
        presenter.user = user
        binding.recyclerView?.apply {
            prepareNoDivider()
            adapter = conversationAdapter
            (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).reverseLayout = true
            setHasFixedSize(false)
        }

        if (conversationDataFragment.data == null) {
            binding.avatarview.isVisible = false
            binding.loadingView.isVisible = true
            presenter.loadConversation()
        } else {
            showConversation(conversationDataFragment.data!!)
        }

        binding.inputToolbar.inputToolbarListener = this
        binding.inputToolbar.setup(userManagerApi, suggestionApi)
    }

    override fun showConversation(conversation: FullConversation) {
        binding.loadingView.isVisible = false
        binding.swiperefresh.isRefreshing = false
        receiver = conversation.receiver
        binding.toolbar.apply {
            subtitle =
                if (conversation.messages.isNotEmpty()) conversation.messages.last().date else null
            binding.avatarview.setAuthor(conversation.receiver)
            binding.avatarview.isVisible = true
            binding.avatarview.setOnClickListener {
                getActivityContext()!!.startActivity(
                    ProfileActivity.createIntent(
                        getActivityContext()!!,
                        conversation.receiver.nick
                    )
                )
            }
        }

        binding.avatarview.setOnClickListener { startActivity(ProfileActivity.createIntent(this, receiver?.nick!!)) }

        conversationAdapter.apply {
            messages.clear()
            messages.addAll(conversation.messages.reversed())
            notifyDataSetChanged()
        }
        binding.recyclerView.invalidate()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        receiver?.let {
            conversationDataFragment.data = FullConversation(conversationAdapter.messages.reversed(), it)
        }
    }

    override fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.insert_photo_galery),
            ),
            BaseInputActivity.USER_ACTION_INSERT_PHOTO,
        )
    }

    override fun onBackPressed() {
        if (binding.inputToolbar.hasUserEditedContent()) {
            exitConfirmationDialog(this) { finish() }?.show()
        } else {
            finish()
        }
    }

    override fun sendPhoto(photo: String?, body: String, containsAdultContent: Boolean) =
        presenter.sendMessage(body, photo, containsAdultContent)

    override fun sendPhoto(photo: WykopImageFile, body: String, containsAdultContent: Boolean) =
        presenter.sendMessage(body, photo, containsAdultContent)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BaseInputActivity.USER_ACTION_INSERT_PHOTO -> binding.inputToolbar.setPhoto(data?.data)
                BaseInputActivity.USER_ACTION_INSERT_PHOTO_CAMERA -> binding.inputToolbar.setPhoto(contentUri)
            }
        }
    }

    override fun hideInputToolbar() {
        binding.inputToolbar.isVisible = false
    }

    override fun hideInputbarProgress() = binding.inputToolbar.showProgress(false)

    override fun resetInputbarState() = binding.inputToolbar.resetState()

    override fun openCamera(uri: Uri) {
        contentUri = uri
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, BaseInputActivity.USER_ACTION_INSERT_PHOTO_CAMERA)
    }
}
