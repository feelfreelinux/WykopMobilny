package io.github.wykopmobilny.domain.navigation.android

import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.domain.navigation.AppRestarter

@Module
internal abstract class FrameworkModule {

    @Binds
    abstract fun AndroidAppRestarter.appRestarter(): AppRestarter
}
