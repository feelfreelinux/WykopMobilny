package io.github.wykopmobilny.domain.navigation.android

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.github.wykopmobilny.domain.navigation.Framework

@Component(modules = [FrameworkModule::class])
interface FrameworkComponent : Framework {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
        ): FrameworkComponent
    }
}
