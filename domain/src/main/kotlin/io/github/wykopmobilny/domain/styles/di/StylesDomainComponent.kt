package io.github.wykopmobilny.domain.styles.di

import dagger.Subcomponent
import io.github.wykopmobilny.styles.StylesDependencies

@StylesScope
@Subcomponent(modules = [StylesModule::class])
interface StylesDomainComponent : StylesDependencies
