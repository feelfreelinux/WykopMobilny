package io.github.wykopmobilny.domain.blacklist.di

import dagger.Subcomponent
import io.github.wykopmobilny.ui.blacklist.BlacklistDependencies

@BlacklistScope
@Subcomponent(modules = [BlacklistModule::class])
interface BlacklistDomainComponent : BlacklistDependencies
