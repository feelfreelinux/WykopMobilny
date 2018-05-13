package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.survey.SurveyWidget
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.EllipsizingTextView
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_list_item.*


typealias EntryListener = (Entry) -> Unit

class EntryViewHolder(override val containerView: View,
                      val userManagerApi: UserManagerApi,
                      val settingsPreferencesApi: SettingsPreferencesApi,
                      val navigatorApi : NewNavigatorApi,
                      val entryActionListener : EntryActionListener,
                      val replyListener: EntryListener?) : RecyclableViewHolder(containerView), LayoutContainer {
    var type : Int = TYPE_NORMAL
    lateinit var embedView : WykopEmbedView
    lateinit var surveyView : SurveyWidget

    companion object {
        const val TYPE_SURVEY = 4
        const val TYPE_EMBED = 5
        const val TYPE_NORMAL = 6
        const val TYPE_EMBED_SURVEY = 7
        const val TYPE_BLOCKED = 8

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(parent: ViewGroup, viewType: Int, userManagerApi: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, entryActionListener: EntryActionListener, replyListener: EntryListener?): EntryViewHolder {
            val view = EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false),
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    entryActionListener,
                    replyListener)

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
        moreOptionsTextView.setOnClickListener {  }

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

        shareTextView.setOnClickListener {

        }

    }

    private fun setupBody(entry : Entry) {
        // Add URL and click handler if body is not empty
        if (entry.body.isNotEmpty()) {
            with(entryContentTextView) {
                isVisible = true
                if (replyListener == null && settingsPreferencesApi.cutLongEntries) {
                    maxLines = EllipsizingTextView.MAX_LINES
                    ellipsize = TextUtils.TruncateAt.END
                }
                prepareBody(entry.body, {
                }, { handleClick(entry) }, settingsPreferencesApi.openSpoilersDialog)
            }
        } else {
            entryContentTextView.isVisible = false
        }


        if (type == TYPE_EMBED_SURVEY || type == TYPE_EMBED) {
            embedView.setEmbed(entry.embed!!, settingsPreferencesApi, navigatorApi, entry.isNsfw)
        }

        // Show survey
        if (type == TYPE_SURVEY || type == TYPE_EMBED_SURVEY) {
            if (entry.survey != null) {
                surveyView.voteAnswerListener = {}
                surveyView.setSurvey(entry.survey!!, userManagerApi)
            } else {
                surveyView.isVisible = false
            }
        }

    }

    private fun handleClick(entry: Entry) {
        if (replyListener == null) {
            navigatorApi.openEntryDetailsActivity(entry.id, if (::embedView.isInitialized) embedView.resized else false)
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