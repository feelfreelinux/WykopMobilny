package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_related_layout.view.*

class RelatedWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs), RelatedWidgetView {
    init {
        View.inflate(context, R.layout.link_related_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    lateinit var presenter : RelatedWidgetPresenter

    lateinit var relatedItem : Related

    fun setRelatedData(related : Related, userManagerApi: UserManagerApi, relatedWidgetPresenter: RelatedWidgetPresenter) {
        relatedItem = related
        presenter = relatedWidgetPresenter
        title.text = related.title
        urlTextView.text = related.url
        userNameTextView.text = related.author.nick
        userNameTextView.setTextColor(context.getGroupColor(related.author.group))
        presenter.subscribe(this)
        presenter.relatedId = relatedItem.id
        title.setOnClickListener {
            context.openBrowser(related.url)
        }
        userNameTextView.setOnClickListener {
            context.openBrowser(related.url)
        }
        setOnClickListener {
            context.openBrowser(related.url)
        }
        setVoteCount(related.voteCount)
        authorHeaderView.setAuthor(related.author)
        setupButtons()
        plusButton.setup(userManagerApi)
        minusButton.setup(userManagerApi)
    }

    fun setupButtons() {
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
    }

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }
}