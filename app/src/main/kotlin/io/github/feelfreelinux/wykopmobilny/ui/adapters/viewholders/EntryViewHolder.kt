package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.app.ShareCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.textview.EllipsizingTextView
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_list_item.*


typealias EntryListener = (Entry) -> Unit

class EntryViewHolder(override val containerView: View,
                      val userManagerApi: UserManagerApi,
                      val settingsPreferencesApi: SettingsPreferencesApi) : RecyclableViewHolder(containerView), LayoutContainer {
    var replyListener : EntryListener? = null

    fun bindView(entry: Entry) {
        setupHeader(entry)
        setupButtons(entry)
        setupBody(entry)

    }

    fun setupHeader(entry: Entry) {
        authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    fun setupButtons(entry : Entry) {
        moreOptionsTextView.setOnClickListener {  }

        // Show comments count
        with(commentsCountTextView) {
            text = entry.commentsCount.toString()
            setOnClickListener {}
        }

        // Only show reply view in entry details
        replyTextView.isVisible = replyListener == null && userManagerApi.isUserAuthorized()
        replyTextView.setOnClickListener({ replyListener?.invoke(entry) })

        containerView.setOnClickListener {  }

        // Setup vote button
        with(voteButton) {
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
            voteListener = {}
            unvoteListener = {}
        }

        with(favoriteButton) {
            isVisible = userManagerApi.isUserAuthorized()
            isFavorite = entry.isFavorite
            setOnClickListener {

            }
        }

        shareTextView.setOnClickListener {

        }

    }

    fun setupBody(entry : Entry) {
        // Add URL and click handler if body is not empty
        if (entry.body.isNotEmpty()) {
            with(entryContentTextView) {
                isVisible = true
                if (replyListener == null && settingsPreferencesApi.cutLongEntries) {
                    maxLines = EllipsizingTextView.MAX_LINES
                    ellipsize = TextUtils.TruncateAt.END
                }
                prepareBody(entry.body, {}, {}, settingsPreferencesApi.openSpoilersDialog)
            }
        } else {
            entryContentTextView.isVisible = false
        }

        // Show survey
        if (entry.survey != null) {
            survey.voteAnswerListener = {}
            survey.setSurvey(entry.survey, userManagerApi)
        } else {
            survey.isVisible = false
        }

    }

    override fun cleanRecycled() {
    }

    fun markEntryAsRemoved() {
        entryContentTextView.setText(R.string.entryRemoved)
        entryImageView.isVisible = false
    }
}