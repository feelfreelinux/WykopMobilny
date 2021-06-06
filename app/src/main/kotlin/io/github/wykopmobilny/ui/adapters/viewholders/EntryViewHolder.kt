package io.github.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.EntryListItemBinding
import io.github.wykopmobilny.databinding.EntryMenuBottomsheetBinding
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.ui.dialogs.confirmationDialog
import io.github.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.wykopmobilny.ui.widgets.survey.SurveyWidget
import io.github.wykopmobilny.utils.copyText
import io.github.wykopmobilny.utils.getActivityContext
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.textview.EllipsizingTextView
import io.github.wykopmobilny.utils.textview.prepareBody
import io.github.wykopmobilny.utils.textview.stripWykopFormatting
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

typealias EntryListener = (Entry) -> Unit

class EntryViewHolder(
    private val binding: EntryListItemBinding,
    private val userManagerApi: UserManagerApi,
    private val settingsPreferencesApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val linkHandlerApi: WykopLinkHandlerApi,
    private val entryActionListener: EntryActionListener,
    private val replyListener: EntryListener?
) : RecyclableViewHolder(binding.root) {

    companion object {
        const val TYPE_SURVEY = 4
        const val TYPE_EMBED = 5
        const val TYPE_NORMAL = 6
        const val TYPE_EMBED_SURVEY = 7
        const val TYPE_BLOCKED = 8

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(
            parent: ViewGroup,
            viewType: Int,
            userManagerApi: UserManagerApi,
            settingsPreferencesApi: SettingsPreferencesApi,
            navigatorApi: NewNavigatorApi,
            linkHandlerApi: WykopLinkHandlerApi,
            entryActionListener: EntryActionListener,
            replyListener: EntryListener?
        ): EntryViewHolder {
            val view = EntryViewHolder(
                EntryListItemBinding.inflate(parent.layoutInflater, parent, false),
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                entryActionListener,
                replyListener,
            )

            view.itemView.tag = if (replyListener == null) {
                SEPARATOR_SMALL
            } else {
                SEPARATOR_NORMAL
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

        fun getViewTypeForEntry(entry: Entry): Int {
            return if (entry.isBlocked) TYPE_BLOCKED
            else if (entry.embed != null && entry.survey != null) TYPE_EMBED_SURVEY
            else if (entry.embed == null && entry.survey != null) TYPE_SURVEY
            else if (entry.embed != null && entry.survey == null) TYPE_EMBED
            else TYPE_NORMAL
        }
    }

    var type: Int = TYPE_NORMAL
    lateinit var embedView: WykopEmbedView
    private lateinit var surveyView: SurveyWidget
    private val enableClickListener: Boolean
        get() = replyListener == null

    fun bindView(entry: Entry) {
        setupHeader(entry)
        setupButtons(entry)
        setupBody(entry)
    }

    private fun setupHeader(entry: Entry) {
        binding.authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    private fun setupButtons(entry: Entry) {
        binding.moreOptionsTextView.setOnClickListener {
            openOptionsMenu(entry)
        }

        // Show comments count
        with(binding.commentsCountTextView) {
            text = entry.commentsCount.toString()
            setOnClickListener {
                handleClick(entry)
            }
        }

        // Only show reply view in entry details
        binding.replyTextView.isVisible =
            replyListener != null && userManagerApi.isUserAuthorized() && entry.isCommentingPossible
        binding.replyTextView.setOnClickListener { replyListener?.invoke(entry) }

        itemView.setOnClickListener {
            handleClick(entry)
        }

        // Setup vote button
        with(binding.voteButton) {
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
        with(binding.favoriteButton) {
            isVisible = userManagerApi.isUserAuthorized()
            isFavorite = entry.isFavorite
            setOnClickListener {
                entryActionListener.markFavorite(entry)
            }
        }

        // Setup share button
        binding.shareTextView.setOnClickListener {
            navigatorApi.shareUrl(entry.url)
        }
    }

    private fun setupBody(entry: Entry) {
        // Add URL and click handler if body is not empty
        if (entry.body.isNotEmpty()) {
            with(binding.entryContentTextView) {
                isVisible = true
                if (replyListener == null && settingsPreferencesApi.cutLongEntries && entry.collapsed) {
                    maxLines = EllipsizingTextView.MAX_LINES
                    ellipsize = TextUtils.TruncateAt.END
                }

                // Setup unEllipsize listener, handle clicks
                prepareBody(
                    entry.body, { linkHandlerApi.handleUrl(it) },
                    {
                        if (enableClickListener && !isEllipsized) {
                            handleClick(entry)
                        } else if (enableClickListener) {
                            entry.collapsed = false
                            maxLines = Int.MAX_VALUE
                            ellipsize = null
                        }
                    },
                    settingsPreferencesApi.openSpoilersDialog
                )
            }
        } else {
            binding.entryContentTextView.isVisible = false
        }

        itemView.setOnClickListener { handleClick(entry) }

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

    private fun openOptionsMenu(entry: Entry) {
        val activityContext = itemView.getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = EntryMenuBottomsheetBinding.inflate(activityContext.layoutInflater)
        dialog.setContentView(bottomSheetView.root)
        (bottomSheetView.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetView.apply {
            author.text = entry.author.nick
            date.text = entry.fullDate
            entry.app?.let {
                date.text = activityContext.getString(
                    R.string.date_with_user_app,
                    entry.fullDate,
                    entry.app
                )
            }

            entryMenuCopy.setOnClickListener {
                it.context.copyText(entry.body.stripWykopFormatting(), "entry-body")
                dialog.dismiss()
            }

            entryMenuCopyEntryUrl.setOnClickListener {
                it.context.copyText(entry.url, "entry-url")
                dialog.dismiss()
            }

            entryMenuEdit.setOnClickListener {
                navigatorApi.openEditEntryActivity(entry.body, entry.id)
                dialog.dismiss()
            }

            entryMenuDelete.setOnClickListener {
                confirmationDialog(it.context) {
                    entryActionListener.deleteEntry(entry)
                }.show()
                dialog.dismiss()
            }

            entryMenuReport.setOnClickListener {
                navigatorApi.openReportScreen(entry.violationUrl)
                dialog.dismiss()
            }

            entryMenuVoters.setOnClickListener {
                entryActionListener.getVoters(entry)
                dialog.dismiss()
            }

            entryMenuReport.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit =
                userManagerApi.isUserAuthorized() && entry.author.nick == userManagerApi.getUserCredentials()!!.login
            entryMenuDelete.isVisible = canUserEdit
            entryMenuEdit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.root.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.root.height
        }
        dialog.show()
    }

    private fun handleClick(entry: Entry) {
        if (enableClickListener) {
            navigatorApi.openEntryDetailsActivity(entry.id, entry.embed?.isRevealed ?: false)
        }
    }

    fun inflateEmbed() {
        embedView = binding.entryImageViewStub.inflate() as WykopEmbedView
    }

    fun inflateSurvey() {
        surveyView = binding.surveyStub.inflate() as SurveyWidget
    }
}
