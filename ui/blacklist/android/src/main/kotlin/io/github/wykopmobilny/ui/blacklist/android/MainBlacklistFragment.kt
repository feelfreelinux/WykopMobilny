package io.github.wykopmobilny.ui.blacklist.android

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import io.github.wykopmobilny.ui.blacklist.BlacklistDependencies
import io.github.wykopmobilny.ui.blacklist.BlacklistedDetailsUi
import io.github.wykopmobilny.ui.blacklist.GetBlacklistDetails
import io.github.wykopmobilny.ui.blacklist.android.databinding.FragmentBlacklistMainBinding
import io.github.wykopmobilny.ui.blacklist.android.di.BlacklistUiComponent
import io.github.wykopmobilny.ui.blacklist.android.di.DaggerBlacklistUiComponent
import io.github.wykopmobilny.utils.destroyDependency
import io.github.wykopmobilny.utils.requireDependency
import io.github.wykopmobilny.utils.viewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

fun blacklistMainFragment(): Fragment = MainBlacklistFragment()

internal class MainBlacklistFragment : Fragment(R.layout.fragment_blacklist_main) {

    private val binding by viewBinding(FragmentBlacklistMainBinding::bind)

    lateinit var rootComponent: BlacklistUiComponent

    @Inject
    lateinit var getBlacklistDetails: GetBlacklistDetails

    override fun onAttach(context: Context) {
        rootComponent = DaggerBlacklistUiComponent.factory()
            .create(
                deps = context.requireDependency(),
            )
        rootComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        lifecycleScope.launchWhenResumed {
            val shared = getBlacklistDetails().stateIn(this)

            val blacklistAdapter = BlacklistAdapter(this@MainBlacklistFragment)
            binding.viewPager.adapter = blacklistAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.setText(blacklistAdapter.getTitle(position))
            }
                .attach()
            launch { shared.collectErrorDialog() }
            launch { shared.collectSnackbar() }
            launch { shared.collectState() }
        }
    }

    private suspend fun Flow<BlacklistedDetailsUi>.collectState() {
        map { it.content }
            .collect { content ->
                when (content) {
                    is BlacklistedDetailsUi.Content.Empty -> {
                        binding.viewPager.isVisible = false
                        binding.tabLayout.isVisible = false
                        binding.emptyContainer.isVisible = true
                        binding.btnImport.isVisible = !content.isLoading
                        binding.btnProgress.isVisible = content.isLoading
                        binding.btnImport.setOnClickListener { content.loadAction() }
                    }
                    is BlacklistedDetailsUi.Content.WithData -> {
                        binding.emptyContainer.isVisible = false
                        binding.btnImport.setOnClickListener(null)
                        binding.viewPager.isVisible = true
                        binding.tabLayout.isVisible = true
                    }
                }
            }
    }

    private suspend fun Flow<BlacklistedDetailsUi>.collectErrorDialog() {
        var dialog: AlertDialog? = null
        map { it.errorDialog }
            .distinctUntilChangedBy { it?.error }
            .collect { dialogUi ->
                dialog?.dismiss()
                dialog = if (dialogUi != null) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Sooory")
                        .setMessage(dialogUi.error.message ?: dialogUi.error.toString())
                        .setNegativeButton(R.string.retry) { _, _ -> dialogUi.retryAction() }
                        .setPositiveButton(R.string.cancel) { _, _ -> dialogUi.dismissAction() }
                        .setOnCancelListener { dialogUi.dismissAction() }
                        .show()
                } else {
                    null
                }
            }
    }

    private suspend fun Flow<BlacklistedDetailsUi>.collectSnackbar() {
        var snack: Snackbar? = null
        map { it.snackbar }
            .distinctUntilChangedBy { it }
            .collect { message ->
                snack?.dismiss()
                snack = if (message == null) {
                    null
                } else {
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                }
                snack?.show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().destroyDependency<BlacklistDependencies>()
    }
}
