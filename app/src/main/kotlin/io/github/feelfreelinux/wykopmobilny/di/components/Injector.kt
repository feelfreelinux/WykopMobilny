package io.github.feelfreelinux.wykopmobilny.di.components

import dagger.Component
import io.github.feelfreelinux.wykopmobilny.di.AppModule
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.PresentersModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import io.github.feelfreelinux.wykopmobilny.di.modules.ViewPresentersModule
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.NotificationViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.PMMessageViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog.VotersDialogFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment.EditEntryCommentActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsBroadcastReceiver
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist.ConversationsListFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.settings.SettingsActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.AuthorHeaderView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbar
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopImageView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.FavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.entry.EntryFavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.EntryVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment.EntryCommentVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview.DrawerHeaderWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.survey.SurveyWidget
import javax.inject.Singleton

@Singleton
@Component( modules = arrayOf(AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        PresentersModule::class,
        ViewPresentersModule::class) )
interface Injector {
    fun inject(loginScreenActivity: LoginScreenActivity)
    fun inject(mainNavigationActivity: NavigationActivity)
    fun inject(hotFragment: HotFragment)
    fun inject(entryActivity: EntryActivity)
    fun inject(entryVoteButton: EntryVoteButton)
    fun inject(entryCommentVoteButton: EntryCommentVoteButton)
    fun inject(entryView: EntryWidget)
    fun inject(commentView : CommentWidget)
    fun inject(addEntryActivity: AddEntryActivity)
    fun inject(entryFavoriteButton: EntryFavoriteButton)
    fun inject(baseVoteButton: BaseVoteButton)
    fun inject(favoriteButton: FavoriteButton)
    fun inject(inputToolbar: InputToolbar)
    fun inject(photoViewActivity: PhotoViewActivity)
    fun inject(editEntryActivity: EditEntryActivity)
    fun inject(wykopNotificationsJob: WykopNotificationsJob)
    fun inject(wykopNotificationsBroadcastReceiver: WykopNotificationsBroadcastReceiver)
    fun inject(editEntryCommentActivity: EditEntryCommentActivity)
    fun inject(drawerHeaderWidget: DrawerHeaderWidget)
    fun inject(conversationsListActivity: ConversationsListFragment)
    fun inject(conversationActivity: ConversationActivity)
    fun inject(notificationViewHolder: NotificationViewHolder)
    fun inject(notificationsListFragment: NotificationsListFragment)
    fun inject(hashTagsNotificationsListFragment: HashTagsNotificationsListFragment)
    fun inject(wykopImageView: WykopImageView)
    fun inject(pmMessageViewHolder: PMMessageViewHolder)
    fun inject(settingsFragment: SettingsActivity)
    fun inject(settingsFragment: SettingsActivity.SettingsFragment)
    fun inject(authorHeaderView: AuthorHeaderView)
    fun inject(surveyWidget: SurveyWidget)
    fun inject(votersDialogFragment: VotersDialogFragment)
    fun inject(tagEntriesFragment: TagEntriesFragment)
    fun inject(tagActivity: TagActivity)
    fun inject(wykopEmbedView: WykopEmbedView)
    fun inject(myWykopIndexFragment: MyWykopIndexFragment)
    fun inject(myWykopTagsFragment: MyWykopTagsFragment)
    fun inject(myWykopUsersFragment: MyWykopUsersFragment)
}