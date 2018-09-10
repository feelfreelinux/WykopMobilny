package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.app.ShareCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.R.id.favoriteButton
import io.github.feelfreelinux.wykopmobilny.R.id.link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_bury_menu_bottomsheet.view.*
import kotlinx.android.synthetic.main.link_details_header_layout.view.*
import kotlinx.android.synthetic.main.link_menu_bottomsheet.view.*
import java.net.URL

class LinkWidget(context: Context, attrs: AttributeSet) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs), LinkView, URLClickedListener {

    lateinit var link : Link
    lateinit var presenter : LinkPresenter
    lateinit var userManager : UserManagerApi

    init {
        View.inflate(context, R.layout.link_details_header_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun showVoteCount(digResponse: DigResponse) {
        link.buryCount = digResponse.buries
        link.voteCount = digResponse.diggs
        diggCountTextView.text = digResponse.diggs.toString()
    }

    override fun showBurried() {
        link.userVote = "bury"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("bury")
        diggCountTextView.unvoteListener =  {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showDigged() {
        link.userVote = "dig"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("dig")
        diggCountTextView.unvoteListener =  {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showUnvoted() {
        diggCountTextView.isButtonSelected = false
        diggCountTextView.setVoteState(null)
        diggCountTextView.voteListener = {
            presenter.voteUp()
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
    }

    override fun handleUrl(url: String) {
        presenter.handleUrl(url)
    }

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }

    override fun markFavorite() {
        link.userFavorite = !link.userFavorite
        favoriteButton.isFavorite = link.userFavorite
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(getActivityContext()!!)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url : String
        get() = "https://www.wykop.pl/link/${link.id}"

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (::presenter.isInitialized)
            presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (::presenter.isInitialized) presenter.unsubscribe()
    }
}