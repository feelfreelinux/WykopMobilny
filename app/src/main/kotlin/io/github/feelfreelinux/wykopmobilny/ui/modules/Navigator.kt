package io.github.feelfreelinux.wykopmobilny.ui.modules

import android.app.Activity
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser

interface NavigatorApi {
    fun openMainActivity(context: Activity, targetFragment: String? = null)
    fun openEntryDetailsActivity(context: Activity, entryId: Int)
    fun openTagActivity(context: Activity, tag: String)
    fun openConversationListActivity(context: Activity, user: String)
    fun openPhotoViewActivity(context: Activity, url: String)
    fun openSettingsActivity(context: Activity)
    fun openLoginScreen(context: Activity, requestCode: Int)
    fun openAddEntryActivity(context: Activity, receiver: String? = null)
    fun openEditEntryActivity(context: Activity, body: String, entryId: Int)
    fun openEditEntryCommentActivity(context: Activity, body: String, entryId: Int, commentId: Int)
    fun openBrowser(context: Activity, url: String)
}

class Navigator : NavigatorApi {
    override fun openMainActivity(context: Activity, targetFragment: String?) {
        context.startActivity(NavigationActivity.getIntent(context, targetFragment)
                .apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    override fun openEntryDetailsActivity(context: Activity, entryId: Int) {
        context.startActivity(EntryActivity.createIntent(context, entryId, null))
    }

    override fun openTagActivity(context: Activity, tag: String) {
        context.startActivity(TagActivity.createIntent(context, tag))
    }

    override fun openConversationListActivity(context: Activity, user: String) {
        context.startActivity(ConversationActivity.createIntent(context, user))
    }

    override fun openPhotoViewActivity(context: Activity, url: String) {
        context.startActivity(PhotoViewActivity.createIntent(context, url))
    }

    override fun openSettingsActivity(context: Activity) {
        context.startActivity(SettingsActivity.createIntent(context))
    }

    override fun openLoginScreen(context: Activity, requestCode: Int) {
        context.startActivityForResult(LoginScreenActivity.createIntent(context), requestCode)
    }

    override fun openAddEntryActivity(context: Activity, receiver: String?) {
        context.startActivity(AddEntryActivity.createIntent(context, receiver))
    }

    override fun openEditEntryActivity(context: Activity, body: String, entryId: Int) {
        context.startActivity(EditEntryActivity.createIntent(context, body, entryId))
    }

    override fun openEditEntryCommentActivity(context: Activity, body: String, entryId: Int, commentId: Int) {
        context.startActivity(EditEntryCommentActivity.createIntent(context, body, entryId, commentId))
    }

    override fun openBrowser(context: Activity, url: String) {
        context.openBrowser(url)
    }
}