package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.textview.EllipsizingTextView
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entry_layout.view.*
import kotlinx.android.synthetic.main.entry_menu_bottomsheet.view.*

class EntryWidget : ConstraintLayout, EntryView, LayoutContainer {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val containerView: View? = View.inflate(context, R.layout.entry_layout, this)

    companion object {
        fun createView(context: Context): EntryWidget {
            val widget = EntryWidget(context)
            widget.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            return widget
        }
    }

    private lateinit var userManagerApi: UserManagerApi
    private lateinit var presenter: EntryPresenter
    private lateinit var entry: Entry
    private lateinit var settingsPreferencesApi: SettingsPreferencesApi
    private lateinit var votersDialogListener: (List<Voter>) -> Unit
    override var shouldEnableClickListener = true
    var replyListener: ((Author) -> Unit)? = null

    init {
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (::presenter.isInitialized)
            presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    fun setEntryData(entry: Entry, userManager: UserManagerApi, settingsApi: SettingsPreferencesApi, entryPresenter: EntryPresenter) {
        presenter = entryPresenter
        userManagerApi = userManager
        voteButton.setup(userManager)
        settingsPreferencesApi = settingsApi
        presenter.subscribe(this)
        this.entry = entry
        presenter.entryId = entry.id
        entryImageView.setEmbed(entry.embed, settingsApi, entryPresenter.navigatorApi, entry.isNsfw)
        setupHeader()
        setupBody()
        setupButtons()
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    private fun setupButtons() {
        moreOptionsTextView.setOnClickListener { openOptionsMenu() }
        commentsCountTextView.apply {
            text = entry.commentsCount.toString()
            if (shouldEnableClickListener) {
                setOnClickListener {
                    presenter.openDetails(entry.embed?.isRevealed ?: false)
                }
            }
        }

        replyTextView.isVisible = !shouldEnableClickListener && userManagerApi.isUserAuthorized()
        if (shouldEnableClickListener) {
            replyTextView.setOnClickListener(null)
            setOnClickListener {
                presenter.openDetails(entry.embed?.isRevealed ?: false)
            }
        } else {
            replyTextView.setOnClickListener { replyListener?.invoke(entry.author) }

        }

        voteButton.apply {
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
            voteListener = { presenter.voteEntry() }
            unvoteListener = { presenter.unvoteEntry() }
        }

        favoriteButton.apply {
            isFavorite = entry.isFavorite
            isVisible = userManagerApi.isUserAuthorized()
            setOnClickListener {
                presenter.markFavorite()
            }

        }

        shareTextView.setOnClickListener {
            shareUrl()
        }
    }

    private fun setupBody() {
        if (entry.body.isNotEmpty()) {
            entryContentTextView.apply {
                isVisible = true
                if (shouldEnableClickListener && settingsPreferencesApi.cutLongEntries) {
                    maxLines = EllipsizingTextView.MAX_LINES
                    ellipsize = TextUtils.TruncateAt.END
                }
                prepareBody(entry.body, { presenter.handleLink(it) }, {
                    if (shouldEnableClickListener && !isEllipsized) {
                        presenter.openDetails(true)
                    } else if (shouldEnableClickListener) {
                        maxLines = Int.MAX_VALUE
                        ellipsize = null
                    }
                }, settingsPreferencesApi.openSpoilersDialog)
            }
        } else entryContentTextView.isVisible = false

        if (entry.survey != null) {
            survey.voteAnswerListener = { presenter.voteAnswer(it) }
            survey.setSurvey(entry.survey!!, userManagerApi)
        } else survey.isVisible = false


    }

    override fun showSurvey(surveyData: Survey) {
        survey.setSurvey(surveyData, userManagerApi)
    }

    override fun showErrorDialog(e: Throwable) =
            context.showExceptionDialog(e)

    override fun markEntryAsRemoved() {
        entryContentTextView.setText(R.string.entryRemoved)
        entryImageView.isVisible = false
    }

    override fun markEntryUnvoted(voteCount: Int) {
        voteButton.apply {
            isButtonSelected = false
            isEnabled = true
        }.voteCount = voteCount
        entry.isVoted = false
        entry.voteCount = voteCount
    }

    override fun markEntryFavorite(isFavorite: Boolean) {
        favoriteButton.isFavorite = isFavorite
        entry.isFavorite = isFavorite
    }

    override fun markEntryVoted(voteCount: Int) {
        voteButton.apply {
            isButtonSelected = true
            isEnabled = true
        }.voteCount = voteCount
        entry.isVoted = true
        entry.voteCount = voteCount
    }

    fun openProfile(username : String) {
        getActivityContext()!!.startActivity(ProfileActivity.createIntent(getActivityContext()!!, username))
    }

    fun openOptionsMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)
        (bottomSheetView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetView.apply {
            author.apply {
                text = entry.author.nick
                setOnClickListener { openProfile(entry.author.nick) }
            }
            date.text = entry.date
            entry.app?.let {
                date.text = context.getString(R.string.date_with_user_app, entry.date, entry.app)
            }

            entry_menu_copy.setOnClickListener {
                presenter.copyContent(entry)
                dialog.dismiss()
            }

            entry_menu_copy_entry_url.setOnClickListener {
                presenter.copyUrl(url)
                dialog.dismiss()
            }

            entry_menu_edit.setOnClickListener {
                presenter.editEntry(entry)
                dialog.dismiss()
            }

            entry_menu_delete.setOnClickListener {
                presenter.deleteEntry()
                dialog.dismiss()
            }

            entry_menu_report.setOnClickListener {
                presenter.reportContent(entry.violationUrl)
                dialog.dismiss()
            }

            entry_menu_voters.setOnClickListener {
                openVotersMenu()
                dialog.dismiss()
            }

            entry_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && entry.author.nick == userManagerApi.getUserCredentials()!!.login
            entry_menu_delete.isVisible = canUserEdit
            entry_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    fun openVotersMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val votersDialogView = activityContext.layoutInflater.inflate(R.layout.dialog_voters, null)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView)
        votersDialogListener = {
            if (dialog.isShowing) {
                votersDialogView.progressView.isVisible = false
                votersDialogView.votersTextView.isVisible = true
                val spannableStringBuilder = SpannableStringBuilder()
                it
                        .map { it.author }
                        .forEachIndexed { index, author ->
                            val span = ForegroundColorSpan(getGroupColor(author.group))
                            spannableStringBuilder.appendNewSpan(author.nick, span, 0)
                            if (index < it.size - 1) spannableStringBuilder.append(", ")
                        }
                if (spannableStringBuilder.isEmpty()) {
                    votersDialogView.votersTextView.gravity = Gravity.CENTER
                    spannableStringBuilder.append(context.getString(R.string.dialogNoVotes))
                }
                votersDialogView.votersTextView.text = spannableStringBuilder
            }
        }
        dialog.show()
        presenter.getVoters()
    }

    override fun showVoters(voters: List<Voter>) {
        votersDialogListener(voters)
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(getActivityContext()!!)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url: String
        get() = "https://www.wykop.pl/wpis/${entry.id}"
}