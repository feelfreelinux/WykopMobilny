package io.github.feelfreelinux.wykopmobilny.di.components

import dagger.Component
import io.github.feelfreelinux.wykopmobilny.di.AppModule
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.PresentersModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import io.github.feelfreelinux.wykopmobilny.di.modules.ViewPresentersModule
import io.github.feelfreelinux.wykopmobilny.ui.add_user_input.AddUserInputActivity
import io.github.feelfreelinux.wykopmobilny.ui.elements.comment_view.CommentView
import io.github.feelfreelinux.wykopmobilny.ui.elements.entry_view.EntryView
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.EntryVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment.EntryCommentVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.loginscreen.LoginScreenActivity
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryActivity
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
    fun inject(addUserInputActivity: AddUserInputActivity)
}