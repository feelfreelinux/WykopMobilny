package io.github.wykopmobilny.ui.modules.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.wykopmobilny.base.ThemableActivity
import io.github.wykopmobilny.databinding.ActivityContainerBinding
import io.github.wykopmobilny.ui.settings.android.preferencesMainFragment
import io.github.wykopmobilny.utils.viewBinding

internal class SettingsActivity : ThemableActivity() {

    private val binding by viewBinding(ActivityContainerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, preferencesMainFragment())
                .commit()
        }
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
