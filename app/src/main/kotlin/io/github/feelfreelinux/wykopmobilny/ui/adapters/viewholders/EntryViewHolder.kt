package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.survey.SurveyWidget
import io.github.feelfreelinux.wykopmobilny.utils.copyText
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.EllipsizingTextView
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_list_item.*
import kotlinx.android.synthetic.main.entry_menu_bottomsheet.view.*

typealias EntryListener = (Entry) -> Unit

class EntryViewHolder(override val containerView: View,
                      private val userManagerApi: UserManagerApi,
                      private val settingsPreferencesApi: SettingsPreferencesApi,
                      private val navigatorApi : NewNavigatorApi,
                      private val linkHandlerApi: WykopLinkHandlerApi,
                      private val entryActionListener : EntryActionListener,
                      private val replyListener: EntryListener?) : RecyclableViewHolder(containerView), LayoutContainer {
    var type : Int = TYPE_NORMAL
    lateinit var embedView : WykopEmbedView

    lateinit var surveyView : SurveyWidget
    val enableClickListener : Boolean
        get() = replyListener == null

    companion object {
        const val TYPE_SURVEY = 4
        const val TYPE_EMBED = 5
        const val TYPE_NORMAL = 6
        const val TYPE_EMBED_SURVEY = 7
        const val TYPE_BLOCKED = 8

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(parent: ViewGroup, viewType: Int, userManagerApi: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, linkHandlerApi : WykopLinkHandlerApi, entryActionListener: EntryActionListener, replyListener: EntryListener?): EntryViewHolder {
            val view = EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false),
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    linkHandlerApi,
                    entryActionListener,
                    replyListener)

            view.containerView.tag = if (replyListener == null) {
                RecyclableViewHolder.SEPARATOR_SMALL
            } else {
                RecyclableViewHolder.SEPARATOR_NORMAL
            }

            view.type = viewType

            when (viewType) {
                TYPE_SURVEY -> view.inflateSurvey()
                TYPE_EMBED -> view.inflateEmbed()
                TYPE_EMBED_SURVEY -> {
                    view.inflateEmbed()
                    view.inflateSurvey()
                }
            }
            return view
        }

        fun getViewTypeForEntry(entry : Entry) : Int {
            return if (entry.isBlocked) EntryViewHolder.TYPE_BLOCKED
            else if (entry.embed != null && entry.survey != null) EntryViewHolder.TYPE_EMBED_SURVEY
            else if (entry.embed == null && entry.survey != null) EntryViewHolder.TYPE_SURVEY
            else if (entry.embed != null && entry.survey == null) EntryViewHolder.TYPE_EMBED
            else EntryViewHolder.TYPE_NORMAL
        }
    }

    fun bindView(entry: Entry) {
        setupHeader(entry)
        setupButtons(entry)
        setupBody(entry)
    }

    fun setupHeader(entry: Entry) {
        authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    fun setupButtons(entry : Entry) {
        moreOptionsTextView.setOnClickListener {
            openOptionsMenu(entry)
        }

        // Show comments count
        with(commentsCountTextView) {
            text = entry.commentsCount.toString()
            setOnClickListener {
                handleClick(entry)
            }
        }

        // Only show reply view in entry details
        replyTextView.isVisible = replyListener != null && userManagerApi.isUserAuthorized()
        replyTextView.setOnClickListener({ replyListener?.invoke(entry) })

        containerView.setOnClickListener {
            handleClick(entry)
        }

        // Setup vote button
        with(voteButton) {
            isEnabled = true
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
            voteListener = {
                entryActionListener.voteEntry(entry)
            }
            unvoteListener = {
                entryActionListener.unvoteEntry(entry)
            }
            setup(userManagerApi)
        }

        // Setup favorite button
        with(favoriteButton) {
            isVisible = userManagerApi.isUserAuthorized()
            isFavorite = entry.isFavorite
            setOnClickListener {
                entryActionListener.markFavorite(entry)
            }
        }

        // Setup share button
        shareTextView.setOnClickListener {
            navigatorApi.shareUrl(entry.url)
        }

    }

    private fun setupBody(entry : Entry) {
        // Add URL and click handler if body is not empty
        if (entry.body.isNotEmpty()) {
            with(entryContentTextView) {
                isVisible = true
                if (replyListener == null && settingsPreferencesApi.cutLongEntries && entry.collapsed) {
                    maxLines = EllipsizingTextView.MAX_LINES
                    ellipsize = TextUtils.TruncateAt.END
                }

                // Setup unEllipsize listener, handle clicks
                prepareBody(entry.body, { linkHandlerApi.handleUrl(it) }, {
                    if (enableClickListener && !isEllipsized) {
                        handleClick(entry)
                    } else if (enableClickListener) {
                        entry.collapsed = false
                        maxLines = Int.MAX_VALUE
                        ellipsize = null
                    }
                }, settingsPreferencesApi.openSpoilersDialog)
            }
        } else {
            entryContentTextView.isVisible = false
        }

        containerView.setOnClickListener { handleClick(entry) }

        if (type == TYPE_EMBED_SURVEY || type == TYPE_EMBED) {
            embedView.setEmbed(entry.embed!!, settingsPreferencesApi, navigatorApi, entry.isNsfw)
        }

        // Show survey
        if (type == TYPE_SURVEY || type == TYPE_EMBED_SURVEY) {
            if (entry.survey != null) {
                surveyView.voteAnswerListener = {
                    entryActionListener.voteSurvey(entry, it)
                }
                surveyView.setSurvey(entry.survey!!, userManagerApi)
            }
        }

    }

    fun openOptionsMenu(entry : Entry) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)
        (bottomSheetView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetView.apply {
            author.text = entry.author.nick
            date.text = entry.date
            entry.app?.let {
                date.text = context.getString(R.string.date_with_user_app, entry.date, entry.app)
            }

            entry_menu_copy.setOnClickListener {
                context.copyText(entry.body, "entry-body")
                dialog.dismiss()
            }

            entry_menu_copy_entry_url.setOnClickListener {
                context.copyText(entry.url, "entry-url")
                dialog.dismiss()
            }

            entry_menu_edit.setOnClickListener {
                navigatorApi.openEditEntryActivity(entry.body, entry.id)
                dialog.dismiss()
            }

            entry_menu_delete.setOnClickListener {
                ConfirmationDialog(getActivityContext()!!) {
                    entryActionListener.deleteEntry(entry)
                }.show()
                dialog.dismiss()
            }

            entry_menu_report.setOnClickListener {
                navigatorApi.openReportScreen(entry.violationUrl)
                dialog.dismiss()
            }

            entry_menu_voters.setOnClickListener {
                entryActionListener.getVoters(entry)
                dialog.dismiss()
            }

            entry_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && entry.author.nick == userManagerApi.getUserCredentials()!!.login
            entry_menu_delete.isVisible = canUserEdit
            entry_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    private fun handleClick(entry: Entry) {
        if (enableClickListener) {
            navigatorApi.openEntryDetailsActivity(entry.id, entry.embed?.isRevealed ?: false)
        }
    }

    override fun cleanRecycled() {
    }

    fun inflateEmbed() {
        embedView = entryImageViewStub.inflate() as WykopEmbedView
    }

    fun inflateSurvey() {
        surveyView = surveyStub.inflate() as SurveyWidget
    }
}