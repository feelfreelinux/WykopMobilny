package io.github.feelfreelinux.wykopmobilny.ui.modules

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ShareCompat
import com.google.android.youtube.player.YouTubeStandalonePlayer
import io.github.feelfreelinux.wykopmobilny.GOOGLE_KEY
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.ENTRYCOMMENT_REPORT_URL
import io.github.feelfreelinux.wykopmobilny.api.ENTRY_REPORT_URL
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.embedview.EmbedViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.embedview.YoutubeActivity
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
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.NotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import java.util.regex.Pattern


interface NewNavigatorApi {
    fun openMainActivity(targetFragment: String? = null)
    fun openEntryDetailsActivity(entryId: Int, isRevealed: Boolean)
    fun openTagActivity(tag: String)
    fun openConversationListActivity(user: String)
    fun openPhotoViewActivity(url: String)
    fun openSettingsActivity()
    fun openLoginScreen(requestCode: Int)
    fun openAddEntryActivity(receiver: String? = null, extraBody : String? = null)
    fun openEditEntryActivity(body: String, entryId: Int)
    fun openEditLinkCommentActivity(commentId: Int, body: String, linkId: Int)
    fun openEditEntryCommentActivity(body: String, entryId: Int, commentId: Int)
    fun openBrowser(url: String)
    fun openReportScreen(violationUrl: String)
    fun openLinkDetailsActivity(link: Link)
    fun openLinkDetailsActivity(linkId : Int, commentId : Int = -1)
    fun openLinkUpvotersActivity(linkId : Int)
    fun openLinkDownvotersActivity(linkId : Int)
    fun openLinkRelatedActivity(linkId : Int)
    fun openProfileActivity(username : String)
    fun openNotificationsListActivity(preselectIndex : Int = NotificationsListActivity.PRESELECT_NOTIFICATIONS)
    fun openEmbedActivity(url : String)
    fun openYoutubeActivity(url : String)
    fun openAddLinkActivity()
    fun shareUrl(url : String)
}

class NewNavigator(val context : Activity) : NewNavigatorApi {
    companion object {
        val STARTED_FROM_NOTIFICATIONS_CODE = 228
    }

    override fun openMainActivity(targetFragment: String?) {
        context.startActivity(MainNavigationActivity.getIntent(context, targetFragment)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    override fun openEntryDetailsActivity(entryId: Int, isRevealed: Boolean) {
        context.startActivity(EntryActivity.createIntent(context, entryId, null, isRevealed))
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
        context.startActivityForResult(EditEntryActivity.createIntent(context, body, entryId), BaseInputActivity.EDIT_ENTRY)
    }

    override fun openEditLinkCommentActivity(commentId: Int, body: String, linkId: Int) {
        context.startActivityForResult(LinkCommentEditActivity.createIntent(context, commentId, body, linkId), BaseInputActivity.EDIT_LINK_COMMENT)
    }

    override fun openEditEntryCommentActivity(body: String, entryId: Int, commentId: Int) {
        context.startActivityForResult(EditEntryCommentActivity.createIntent(context, body, entryId, commentId), BaseInputActivity.EDIT_ENTRY_COMMENT)
    }
    override fun openBrowser(url: String) {
        context.openBrowser(url)
    }

    override fun openReportScreen(violationUrl : String) {
        context.openBrowser(violationUrl)
    }

    override fun openLinkDetailsActivity(link : Link) {
        context.startActivity(LinkDetailsActivity.createIntent(context, link))
    }

    override fun openLinkUpvotersActivity(linkId : Int) {
        context.startActivity(UpvotersActivity.createIntent(linkId, context))
    }

    override fun openLinkDetailsActivity(linkId: Int, commentId: Int) {
        context.startActivity(LinkDetailsActivity.createIntent(context, linkId, commentId))
    }

    override fun openLinkDownvotersActivity(linkId : Int) {
        context.startActivity(DownvotersActivity.createIntent(linkId, context))
    }

    override fun openLinkRelatedActivity(linkId : Int) {
        context.startActivity(RelatedActivity.createIntent(linkId, context))
    }

    override fun openProfileActivity(username: String) {
        context.startActivity(ProfileActivity.createIntent(context, username))
    }

    override fun openNotificationsListActivity(preselectIndex: Int) {
        context.startActivityForResult(NotificationsListActivity.createIntent(context, preselectIndex), STARTED_FROM_NOTIFICATIONS_CODE)
    }

    override fun openEmbedActivity(url: String) {
        context.startActivity(EmbedViewActivity.createIntent(context, url))
    }

    override fun openYoutubeActivity(url: String) {
        context.startActivity(YoutubeActivity.createIntent(context, url))
    }

    override fun openAddLinkActivity() {
        context.startActivity(AddlinkActivity.createIntent(context))
    }

    override fun shareUrl(url: String) {
        ShareCompat.IntentBuilder
                .from(context)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }
}