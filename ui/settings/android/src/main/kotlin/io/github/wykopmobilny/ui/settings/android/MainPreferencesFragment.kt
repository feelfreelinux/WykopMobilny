package io.github.wykopmobilny.ui.settings.android

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import io.github.wykopmobilny.ui.settings.SettingsDependencies
import io.github.wykopmobilny.ui.settings.android.databinding.FragmentSettingsBinding
import io.github.wykopmobilny.ui.settings.android.di.DaggerSettingsUiComponent
import io.github.wykopmobilny.utils.destroyDependency
import io.github.wykopmobilny.utils.requireDependency
import io.github.wykopmobilny.utils.viewBinding

fun preferencesMainFragment(): Fragment = MainPreferencesFragment()

internal class MainPreferencesFragment : Fragment(R.layout.fragment_settings) {

    val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onAttach(context: Context) {
        DaggerSettingsUiComponent.factory()
            .create(
                deps = context.requireDependency(),
            )
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!childFragmentManager.popBackStackImmediate()) {
                isEnabled = false
                activity?.onBackPressed()
            }
        }

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.container.id, GeneralPreferencesFragment())
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().destroyDependency<SettingsDependencies>()
    }
}
