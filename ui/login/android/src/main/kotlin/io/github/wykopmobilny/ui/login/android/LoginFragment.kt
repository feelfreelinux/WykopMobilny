package io.github.wykopmobilny.ui.login.android

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.Lazy
import io.github.wykopmobilny.ui.login.android.databinding.FragmentLoginBinding
import io.github.wykopmobilny.ui.login.android.di.DaggerLoginUiComponent
import io.github.wykopmobilny.utils.collectEvent
import io.github.wykopmobilny.utils.requireDependency
import io.github.wykopmobilny.utils.viewBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

fun loginFragment(): Fragment = LoginFragment()

internal class LoginFragment : Fragment(R.layout.fragment_login) {

    val binding by viewBinding(FragmentLoginBinding::bind)

    @Inject
    lateinit var factory: Lazy<ViewModelProvider.Factory>

    override fun onAttach(context: Context) {
        DaggerLoginUiComponent.factory()
            .create(
                deps = context.requireDependency(),
            )
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<LoginViewModel>(factoryProducer = { factory.get() })

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(binding.webView, true)
        }
        @SuppressLint("SetJavaScriptEnabled")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                viewModel.onNewUrl(request.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.url.collectEvent(binding.webView::loadUrl)
        }
        lifecycleScope.launchWhenResumed {
            viewModel.isLoading.collect { binding.fullScreenProgress.isVisible = it }
        }
    }
}
