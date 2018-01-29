package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.support.v7.widget.CardView
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entry_layout.view.*
import kotlinx.android.synthetic.main.entry_menu_bottomsheet.view.*

class EntryWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs),  EntryView {

    private lateinit var userManagerApi: UserManagerApi
    private lateinit var presenter: EntryPresenter
    private lateinit var entry: Entry
    private lateinit var votersDialogListener : (List<Voter>) -> Unit
    var shouldEnableClickListener = true
  
    init {
        View.inflate(context, R.layout.entry_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
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

    fun setEntryData(entry: Entry, userManager: UserManagerApi, settingsApi : SettingsPreferencesApi, entryPresenter: EntryPresenter) {
        presenter = entryPresenter
        userManagerApi = userManager
        voteButton.setup(userManager)
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


        if (shouldEnableClickListener) {
            setOnClickListener {
                presenter.openDetails(entry.embed?.isRevealed ?: false)
            }
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
            entryContentTextView.isVisible = true
            entryContentTextView.prepareBody(entry.body, { presenter.handleLink(it) }, { if (shouldEnableClickListener) presenter.openDetails(true) })
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

    fun openOptionsMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            entry_menu_copy.setOnClickListener {
                presenter.copyContent(entry)
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
                presenter.reportContent()
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
                        .forEachIndexed {
                            index, author ->
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

    override fun showVoters(voters : List<Voter>) {
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

    val url : String
        get() = "https://www.wykop.pl/wpis/${entry.id}"
}