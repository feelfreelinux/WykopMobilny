package io.github.wykopmobilny.ui.modules

import android.app.Activity
import android.content.Intent
import io.github.wykopmobilny.api.ENTRY_COMMENT_REPORT_URL
import io.github.wykopmobilny.api.ENTRY_REPORT_URL
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivity
import io.github.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import io.github.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.wykopmobilny.ui.modules.tag.TagActivity
import io.github.wykopmobilny.utils.openBrowser

interface NavigatorApi {
    fun openMainActivity(context: Activity, targetFragment: String? = null)
    fun openEntryDetailsActivity(context: Activity, entryId: Int, isRevealed: Boolean)
    fun openTagActivity(context: Activity, tag: String)
    fun openConversationListActivity(context: Activity, user: String)
    fun openPhotoViewActivity(context: Activity, url: String)
    fun openSettingsActivity(context: Activity)
    fun openLoginScreen(context: Activity, requestCode: Int)
    fun openAddEntryActivity(context: Activity, receiver: String? = null, extraBody: String? = null)
    fun openEditEntryActivity(context: Activity, body: String, entryId: Int)
    fun openEditEntryCommentActivity(context: Activity, body: String, entryId: Int, commentId: Int)
    fun openBrowser(context: Activity, url: String)
    fun openReportEntryScreen(context: Activity, entryId: Int)
    fun openReportEntryCommentScreen(context: Activity, entryCommentId: Int)
    fun openLinkDetailsActivity(context: Activity, link: Link)
}

class Navigator : NavigatorApi {

    override fun openMainActivity(context: Activity, targetFragment: String?) {
        context.startActivity(
            MainNavigationActivity.getIntent(context, targetFragment)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    }

    override fun openEntryDetailsActivity(context: Activity, entryId: Int, isRevealed: Boolean) =
        context.startActivity(EntryActivity.createIntent(context, entryId, null, isRevealed))

    override fun openTagActivity(context: Activity, tag: String) =
        context.startActivity(TagActivity.createIntent(context, tag))

    override fun openConversationListActivity(context: Activity, user: String) =
        context.startActivity(ConversationActivity.createIntent(context, user))

    override fun openPhotoViewActivity(context: Activity, url: String) =
        context.startActivity(PhotoViewActivity.createIntent(context, url))

    override fun openSettingsActivity(context: Activity) =
        context.startActivity(SettingsActivity.createIntent(context))

    override fun openLoginScreen(context: Activity, requestCode: Int) =
        context.startActivityForResult(LoginScreenActivity.createIntent(context), requestCode)

    override fun openAddEntryActivity(context: Activity, receiver: String?, extraBody: String?) =
        context.startActivity(AddEntryActivity.createIntent(context, receiver, extraBody))

    override fun openEditEntryActivity(context: Activity, body: String, entryId: Int) =
        context.startActivityForResult(EditEntryActivity.createIntent(context, body, entryId), BaseInputActivity.REQUEST_CODE)

    override fun openEditEntryCommentActivity(context: Activity, body: String, entryId: Int, commentId: Int) =
        context.startActivityForResult(
            EditEntryCommentActivity.createIntent(context, body, entryId, commentId),
            BaseInputActivity.REQUEST_CODE,
        )

    override fun openBrowser(context: Activity, url: String) =
        context.openBrowser(url)

    override fun openReportEntryScreen(context: Activity, entryId: Int) =
        context.openBrowser(ENTRY_REPORT_URL + entryId)

    override fun openReportEntryCommentScreen(context: Activity, entryCommentId: Int) =
        context.openBrowser(ENTRY_COMMENT_REPORT_URL + entryCommentId)

    override fun openLinkDetailsActivity(context: Activity, link: Link) =
        context.startActivity(LinkDetailsActivity.createIntent(context, link))
}
