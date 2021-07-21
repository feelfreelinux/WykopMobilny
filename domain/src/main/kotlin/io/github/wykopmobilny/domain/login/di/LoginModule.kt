package io.github.wykopmobilny.domain.login.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.domain.login.LoginQuery
import io.github.wykopmobilny.ui.base.SimpleViewStateStorage
import io.github.wykopmobilny.ui.login.Login

@Module
internal abstract class LoginModule {

    @Binds
    abstract fun LoginQuery.login(): Login

    companion object {

        @Provides
        @LoginScope
        fun viewState() = SimpleViewStateStorage()
    }
}
