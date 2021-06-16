package io.github.wykopmobilny.ui.login.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.ui.login.LoginDependencies
import io.github.wykopmobilny.ui.login.android.LoginFragment
import io.github.wykopmobilny.ui.login.android.LoginViewModel
import javax.inject.Provider

@Component(
    dependencies = [LoginDependencies::class],
    modules = [LoginModule::class],
)
internal interface LoginUiComponent {

    fun inject(fragment: LoginFragment)

    @Component.Factory
    interface Factory {

        fun create(
            deps: LoginDependencies,
        ): LoginUiComponent
    }
}

@Module
internal class LoginModule {

    @Provides
    @Suppress("UNCHECKED_CAST")
    fun vmFactory(loginViewModel: Provider<LoginViewModel>) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            loginViewModel.get() as T
    }
}
