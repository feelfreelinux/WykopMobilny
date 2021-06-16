package io.github.wykopmobilny.domain.styles.di

import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.domain.styles.GetAppStyleQuery
import io.github.wykopmobilny.styles.GetAppStyle

@Module
internal abstract class StylesModule {

    @Binds
    abstract fun GetAppStyleQuery.getAppStyle(): GetAppStyle
}
