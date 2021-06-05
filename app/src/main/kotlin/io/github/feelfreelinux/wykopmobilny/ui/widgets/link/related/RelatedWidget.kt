package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.LinkRelatedLayoutBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class RelatedWidget(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs), RelatedWidgetView {

    private val binding = LinkRelatedLayoutBinding.inflate(layoutInflater, this)

    init {
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    val url: String
        get() = relatedItem.url

    private lateinit var presenter: RelatedWidgetPresenter
    private lateinit var relatedItem: Related

    fun setRelatedData(
        related: Related,
        userManagerApi: UserManagerApi,
        relatedWidgetPresenter: RelatedWidgetPresenter
    ) {
        relatedItem = related
        presenter = relatedWidgetPresenter
        binding.title.text = related.title
        binding.urlTextView.text = related.url
        presenter.subscribe(this)
        presenter.relatedId = relatedItem.id
        setOnClickListener {
            presenter.handleLink(related.url)
        }
        binding.shareTextView.setOnClickListener {
            shareUrl()
        }
        setVoteCount(related.voteCount)
        binding.authorHeaderView.isVisible = related.author != null
        binding.userNameTextView.isVisible = related.author != null
        related.author?.apply {
            binding.userNameTextView.text = nick
            binding.userNameTextView.setOnClickListener { openProfile() }
            binding.authorHeaderView.setOnClickListener { openProfile() }
            binding.authorHeaderView.setAuthor(this)
            binding.userNameTextView.setTextColor(context.getGroupColor(group))
        }
        setupButtons()
        binding.plusButton.setup(userManagerApi)
        binding.minusButton.setup(userManagerApi)
    }

    private fun openProfile() {
        val context = getActivityContext()!!
        context.startActivity(ProfileActivity.createIntent(context, relatedItem.author!!.nick))
    }

    private fun setupButtons() {
        when (relatedItem.userVote) {
            1 -> {
                binding.plusButton.isButtonSelected = true
                binding.plusButton.isEnabled = false
                binding.minusButton.isButtonSelected = false
                binding.minusButton.isEnabled = true
                binding.minusButton.voteListener = { presenter.voteDown() }
            }

            0 -> {
                binding.plusButton.isButtonSelected = false
                binding.plusButton.isEnabled = true
                binding.plusButton.voteListener = { presenter.voteUp() }
                binding.minusButton.isButtonSelected = false
                binding.minusButton.isEnabled = true
                binding.minusButton.voteListener = { presenter.voteDown() }
            }

            -1 -> {
                binding.minusButton.isButtonSelected = true
                binding.minusButton.isEnabled = false
                binding.plusButton.isButtonSelected = false
                binding.plusButton.isEnabled = true
                binding.plusButton.voteListener = {
                    presenter.voteUp()
                }
            }
        }
    }

    override fun markUnvoted() {
        relatedItem.userVote = -1
        setupButtons()
    }

    override fun markVoted() {
        relatedItem.userVote = 1
        setupButtons()
    }

    override fun setVoteCount(voteCount: Int) {
        relatedItem.voteCount = voteCount
        binding.voteCountTextView.text = if (relatedItem.voteCount > 0) "+${relatedItem.voteCount}" else "${relatedItem.voteCount}"
        if (relatedItem.voteCount > 0) {
            R.color.plusPressedColor
        } else if (relatedItem.voteCount < 0) {
            R.color.minusPressedColor
        } else {
            null
        }?.let { binding.voteCountTextView.setTextColor(ContextCompat.getColor(context, it)) }
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)

    private fun shareUrl() {
        ShareCompat.IntentBuilder(getActivityContext()!!)
            .setType("text/plain")
            .setChooserTitle(R.string.share)
            .setText(url)
            .startChooser()
    }
}
