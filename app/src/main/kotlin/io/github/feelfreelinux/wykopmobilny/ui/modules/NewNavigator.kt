package io.github.feelfreelinux.wykopmobilny.ui.modules

import android.app.Activity
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.api.ENTRYCOMMENT_REPORT_URL
import io.github.feelfreelinux.wykopmobilny.api.ENTRY_REPORT_URL
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit.LinkCommentEditActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters.DownvotersActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.related.RelatedActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters.UpvotersActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser


interface NewNavigatorApi {
    fun openMainActivity(targetFragment: String? = null)
    fun openEntryDetailsActivity(entryId: Int)
    fun openTagActivity(tag: String)
    fun openConversationListActivity(user: String)
    fun openPhotoViewActivity(url: String)
    fun openSettingsActivity()
    fun openLoginScreen(requestCode: Int)
    fun openAddEntryActivity(receiver: String? = null, extraBody : String? = null)
    fun openEditEntryActivity(body: String, entryId: Int)
    fun openEditLinkCommentActivity(body: String, linkId: Int)
    fun openEditEntryCommentActivity(body: String, entryId: Int, commentId: Int)
    fun openBrowser(url: String)
    fun openReportEntryScreen(entryId: Int)
    fun openReportEntryCommentScreen(entryCommentId: Int)
    fun openLinkDetailsActivity(link: Link)
    fun openLinkUpvotersActivity(linkId : Int)
    fun openLinkDownvotersActivity(linkId : Int)
    fun openLinkRelatedActivity(linkId : Int)
}

class NewNavigator(val context : Activity) : NewNavigatorApi {
    companion object {
        val STARTED_FROM_NOTIFICATIONS_CODE = 228
    }

    override fun openMainActivity(targetFragment: String?) {
        context.startActivity(MainNavigationActivity.getIntent(context, targetFragment)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    override fun openEntryDetailsActivity(entryId: Int) {
        context.startActivity(EntryActivity.createIntent(context, entryId, null))
    }

    override fun openTagActivity(tag: String) {
        context.startActivity(TagActivity.createIntent(context, tag))
    }

    override fun openConversationListActivity(user: String) {
        context.startActivity(ConversationActivity.createIntent(context, user))
    }

    override fun openPhotoViewActivity(url: String) {
        context.startActivity(PhotoViewActivity.createIntent(context, url))
    }

    override fun openSettingsActivity() {
        context.startActivity(SettingsActivity.createIntent(context))
    }

    override fun openLoginScreen(requestCode: Int) {
        context.startActivityForResult(LoginScreenActivity.createIntent(context), requestCode)
    }

    override fun openAddEntryActivity(receiver: String?, extraBody : String?) {
        context.startActivity(AddEntryActivity.createIntent(context, receiver, extraBody))
    }

    override fun openEditEntryActivity(body: String, entryId: Int) {
        context.startActivityForResult(EditEntryActivity.createIntent(context, body, entryId), BaseInputActivity.REQUEST_CODE)
    }

    override fun openEditLinkCommentActivity(body: String, linkId: Int) {
        context.startActivityForResult(LinkCommentEditActivity.createIntent(context, body, linkId), BaseInputActivity.REQUEST_CODE)
    }

    override fun openEditEntryCommentActivity(body: String, entryId: Int, commentId: Int) {
        context.startActivityForResult(EditEntryCommentActivity.createIntent(context, body, entryId, commentId), BaseInputActivity.REQUEST_CODE)
    }
    override fun openBrowser(url: String) {
        context.openBrowser(url)
    }

    override fun openReportEntryScreen(entryId: Int) {
        context.openBrowser(ENTRY_REPORT_URL +entryId)
    }

    override fun openReportEntryCommentScreen(entryCommentId: Int) {
        context.openBrowser(ENTRYCOMMENT_REPORT_URL +entryCommentId)
    }

    override fun openLinkDetailsActivity(link : Link) {
        context.startActivity(LinkDetailsActivity.createIntent(context, link))
    }

    override fun openLinkUpvotersActivity(linkId : Int) {
        context.startActivity(UpvotersActivity.createIntent(linkId, context))
    }

    override fun openLinkDownvotersActivity(linkId : Int) {
        context.startActivity(DownvotersActivity.createIntent(linkId, context))
    }

    override fun openLinkRelatedActivity(linkId : Int) {
        context.startActivity(RelatedActivity.createIntent(linkId, context))
    }
}