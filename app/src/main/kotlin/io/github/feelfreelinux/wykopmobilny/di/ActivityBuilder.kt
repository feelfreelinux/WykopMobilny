package io.github.feelfreelinux.wykopmobilny.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit.LinkCommentEditActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit.LinkCommentEditModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit.LinkCommentEditView
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters.DownvotersActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters.DownvotersModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters.UpvotersActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters.UpvotersModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationFragmentProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryDetailModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivityFragmentProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsBroadcastReceiver
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivityModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsFragmentProvider

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindWykopStartupBroadcastReceiver() : WykopNotificationsBroadcastReceiver

    @ContributesAndroidInjector(modules = [LoginScreenModule::class])
    abstract fun bindLoginActivity(): LoginScreenActivity

    @ContributesAndroidInjector(modules = [EntryDetailModule::class])
    abstract fun bindEntryDetailsActivity(): EntryActivity

    @ContributesAndroidInjector(modules = [MainNavigationModule::class, MainNavigationFragmentProvider::class])
    abstract fun bindMainNavigationActivity() : MainNavigationActivity

    @ContributesAndroidInjector(modules = [SettingsActivityModule::class, SettingsFragmentProvider::class])
    abstract fun bindSettingsActivity() : SettingsActivity

    @ContributesAndroidInjector(modules = [ConversationActivityModule::class])
    abstract fun bindConversationActivity() : ConversationActivity

    @ContributesAndroidInjector()
    abstract fun bindPhotoViewActivity() : PhotoViewActivity

    @ContributesAndroidInjector(modules = [AddEntryActivityModule::class])
    abstract fun bindAddEntryActivity() : AddEntryActivity

    @ContributesAndroidInjector(modules = [EditEntryActivityModule::class])
    abstract fun bindEditEntryActivity() : EditEntryActivity

    @ContributesAndroidInjector(modules = [EditEntryCommentActivityModule::class])
    abstract fun bindEditEntryCommentActivity() : EditEntryCommentActivity

    @ContributesAndroidInjector(modules = [LinkDetailsModule::class])
    abstract fun bindLinkDetailsActivity() : LinkDetailsActivity

    @ContributesAndroidInjector(modules = [TagActivityFragmentProvider::class, TagActivityModule::class])
    abstract fun bindTagActivity() : TagActivity

    @ContributesAndroidInjector(modules = [UpvotersModule::class])
    abstract fun bindUpvotersActivity() : UpvotersActivity

    @ContributesAndroidInjector(modules = [DownvotersModule::class])
    abstract fun bindDownvotersActivity() : DownvotersActivity

    @ContributesAndroidInjector(modules = [LinkCommentEditModule::class])
    abstract fun bindLinkEditCommentActivity() : LinkCommentEditActivity
}