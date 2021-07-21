package io.github.wykopmobilny.ui.blacklist.android.di

import dagger.Component
import io.github.wykopmobilny.ui.blacklist.BlacklistDependencies
import io.github.wykopmobilny.ui.blacklist.android.MainBlacklistFragment
import io.github.wykopmobilny.ui.blacklist.android.page.BlacklistTagsFragment
import io.github.wykopmobilny.ui.blacklist.android.page.BlacklistUsersFragment

@Component(dependencies = [BlacklistDependencies::class])
internal interface BlacklistUiComponent {

    fun inject(fragment: MainBlacklistFragment)

    fun inject(fragment: BlacklistTagsFragment)

    fun inject(fragment: BlacklistUsersFragment)

    @Component.Factory
    interface Factory {

        fun create(
            deps: BlacklistDependencies,
        ): BlacklistUiComponent
    }
}
