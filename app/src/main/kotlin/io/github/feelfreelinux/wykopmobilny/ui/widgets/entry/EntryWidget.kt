package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.entry_layout.view.*
import kotlinx.android.synthetic.main.entry_menu_bottomsheet.view.*
import javax.inject.Inject

class EntryWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs),  EntryView, URLClickedListener {

    @Inject lateinit var linkHandler: WykopLinkHandlerApi
    @Inject lateinit var userManager: UserManagerApi
    @Inject lateinit var clipboardHelper: ClipboardHelperApi
    @Inject lateinit var presenter: EntryPresenter
    @Inject lateinit var navigator: NavigatorApi
    private lateinit var entry: Entry
    var shouldEnableClickListener = true
  
    init {
        WykopApp.uiInjector.inject(this)
        View.inflate(context, R.layout.entry_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    fun setEntryData(entry: Entry) {
        this.entry = entry
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
                    navigator.openEntryDetailsActivity(getActivityContext()!!, entry.id)
                }
            }
        }

        if (shouldEnableClickListener) {
            setOnClickListener {
                navigator.openEntryDetailsActivity(getActivityContext()!!, entry.id)
            }
        }

        voteButton.apply {
            setEntryData(entry)
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
        }

        favoriteButton.setEntryData(entry)
    }

    private fun setupBody() {
        if (entry.body.isNotEmpty()) {
            entryContentTextView.isVisible = true
            entryContentTextView.prepareBody(entry.body, this)
        } else entryContentTextView.isVisible = false
        entryImageView.setEmbed(entry.embed)

        if (entry.survey != null) {
            survey.setSurvey(entry.survey!!, entry.id)
        } else survey.isVisible = false
    }

    override fun handleUrl(url: String) {
        linkHandler.handleUrl(getActivityContext()!!, url)
    }

    override fun showErrorDialog(e: Throwable) =
            context.showExceptionDialog(e)

    override fun markEntryAsRemoved() {
        entryContentTextView.setText(R.string.entryRemoved)
        entryImageView.isVisible = false
    }

    fun removeEntry() {
        presenter.deleteEntry(entry.id)
    }

    fun editEntry() {
        navigator.openEditEntryActivity(getActivityContext()!!, entry.body, entry.id)
    }

    fun copyContent() {
        clipboardHelper.copyTextToClipboard(entry.body.removeHtml())
    }

    fun reportContent() {
        navigator.openReportEntryScreen(getActivityContext()!!, entry.id)
    }

    fun openOptionsMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            entry_menu_copy.setOnClickListener {
                copyContent()
                dialog.dismiss()
            }

            entry_menu_edit.setOnClickListener {
                editEntry()
                dialog.dismiss()
            }

            entry_menu_delete.setOnClickListener {
                removeEntry()
                dialog.dismiss()
            }

            entry_menu_report.setOnClickListener {
                reportContent()
                dialog.dismiss()
            }

            entry_menu_report.isVisible = userManager.isUserAuthorized()

            val canUserEdit = userManager.isUserAuthorized() && entry.author.nick == userManager.getUserCredentials()!!.login
            entry_menu_delete.isVisible = canUserEdit
            entry_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }
}