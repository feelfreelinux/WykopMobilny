package io.github.feelfreelinux.wykopmobilny.di.components

import dagger.Component
import io.github.feelfreelinux.wykopmobilny.di.AppModule
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.PresentersModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import io.github.feelfreelinux.wykopmobilny.di.modules.ViewPresentersModule
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.favorite.FavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.favorite.entry.EntryFavoriteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.vote.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.comment_view.CommentView
import io.github.feelfreelinux.wykopmobilny.ui.elements.entry_view.EntryView
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.vote.entry.EntryVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.vote.entry.comment.EntryCommentVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.input.entry.add.AddEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.input.entry.comment.add.AddEntryCommentActivity
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedList
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.splashscreen.SplashScreenActivity
import javax.inject.Singleton

@Singleton
@Component( modules = arrayOf(AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        PresentersModule::class,
        ViewPresentersModule::class) )
interface Injector {
    fun inject(loginScreenActivity: LoginScreenActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(mainNavigationActivity: NavigationActivity)
    fun inject(hotFragment: HotFragment)
    fun inject(entryActivity: EntryActivity)
    fun inject(tagActivity: TagActivity)
    fun inject(entryVoteButton: EntryVoteButton)
    fun inject(entryCommentVoteButton: EntryCommentVoteButton)
    fun inject(entryView: EntryView)
    fun inject(commentView : CommentView)
    fun inject(addEntryActivity: AddEntryActivity)
    fun inject(addEntryCommentActivity: AddEntryCommentActivity)
    fun inject(entryFavoriteButton: EntryFavoriteButton)
    fun inject(baseVoteButton: BaseVoteButton)
    fun inject(favoriteButton: FavoriteButton)
    fun inject(baseFeedList: BaseFeedList)
}