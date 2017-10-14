package io.github.feelfreelinux.wykopmobilny.di.components

import dagger.Component
import io.github.feelfreelinux.wykopmobilny.di.AppModule
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.PresentersModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import io.github.feelfreelinux.wykopmobilny.di.modules.ViewPresentersModule
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.FavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.entry.EntryFavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.CommentView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.EntryVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment.EntryCommentVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbar
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseFeedList
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags.HashTagsNotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification.NotificationsListActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActivity
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
    fun inject(tagActivity: TagActivity)
    fun inject(entryVoteButton: EntryVoteButton)
    fun inject(entryCommentVoteButton: EntryCommentVoteButton)
    fun inject(entryView: EntryWidget)
    fun inject(commentView : CommentView)
    fun inject(addEntryActivity: AddEntryActivity)
    fun inject(entryFavoriteButton: EntryFavoriteButton)
    fun inject(baseVoteButton: BaseVoteButton)
    fun inject(favoriteButton: FavoriteButton)
    fun inject(baseFeedList: BaseFeedList)
    fun inject(inputToolbar: InputToolbar)
    fun inject(notificationsListActivity: NotificationsListActivity)
    fun inject(hashTagsNotificationsListActivity: HashTagsNotificationsListActivity)
    fun inject(photoViewActivity: PhotoViewActivity)
    fun inject(editEntryActivity: EditEntryActivity)
}