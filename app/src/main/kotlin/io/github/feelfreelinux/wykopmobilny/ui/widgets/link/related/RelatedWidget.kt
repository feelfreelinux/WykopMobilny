package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_related_layout.view.*

class RelatedWidget(context: Context, attrs: AttributeSet) :
    androidx.cardview.widget.CardView(context, attrs), RelatedWidgetView {

    init {
        View.inflate(context, R.layout.link_related_layout, this)
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

    fun setRelatedData(related: Related, userManagerApi: UserManagerApi, relatedWidgetPresenter: RelatedWidgetPresenter) {
        relatedItem = related
        presenter = relatedWidgetPresenter
        title.text = related.title
        urlTextView.text = related.url
        presenter.subscribe(this)
        presenter.relatedId = relatedItem.id
        setOnClickListener {
            presenter.handleLink(related.url)
        }
        shareTextView.setOnClickListener {
            shareUrl()
        }
        setVoteCount(related.voteCount)
        authorHeaderView.isVisible = related.author != null
        userNameTextView.isVisible = related.author != null
        related.author?.apply {
            userNameTextView.text = nick
            userNameTextView.setOnClickListener { openProfile() }
            authorHeaderView.setOnClickListener { openProfile() }
            authorHeaderView.setAuthor(this)
            userNameTextView.setTextColor(context.getGroupColor(group))
        }
        setupButtons()
        plusButton.setup(userManagerApi)
        minusButton.setup(userManagerApi)
    }

    private fun openProfile() {
        val context = getActivityContext()!!
        context.startActivity(ProfileActivity.createIntent(context, relatedItem.author!!.nick))
    }

    private fun setupButtons() {
        when (relatedItem.userVote) {
            1 -> {
                plusButton.isButtonSelected = true
                plusButton.isEnabled = false
                minusButton.isButtonSelected = false
                minusButton.isEnabled = true
                minusButton.voteListener = {
                    presenter.voteDown()
                }
            }

            0 -> {
                plusButton.isButtonSelected = false
                plusButton.isEnabled = true
                plusButton.voteListener = {
                    presenter.voteUp()
                }
                minusButton.isButtonSelected = false
                minusButton.isEnabled = true
                minusButton.voteListener = {
                    presenter.voteDown()
                }
            }

            -1 -> {
                minusButton.isButtonSelected = true
                minusButton.isEnabled = false
                plusButton.isButtonSelected = false
                plusButton.isEnabled = true
                plusButton.voteListener = {
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
        voteCountTextView.text = if (relatedItem.voteCount > 0) "+${relatedItem.voteCount}" else "${relatedItem.voteCount}"
        if (relatedItem.voteCount > 0) {
            voteCountTextView.setTextColor(ContextCompat.getColor(context, R.color.plusPressedColor))
        } else if (relatedItem.voteCount < 0) {
            voteCountTextView.setTextColor(ContextCompat.getColor(context, R.color.minusPressedColor))
        }
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)

    private fun shareUrl() {
        ShareCompat.IntentBuilder
            .from(getActivityContext()!!)
            .setType("text/plain")
            .setChooserTitle(R.string.share)
            .setText(url)
            .startChooser()
    }

}