package io.github.wykopmobilny.domain.login.di

import dagger.Subcomponent
import io.github.wykopmobilny.ui.login.LoginDependencies

@LoginScope
@Subcomponent(modules = [LoginModule::class])
interface LoginDomainComponent : LoginDependencies
