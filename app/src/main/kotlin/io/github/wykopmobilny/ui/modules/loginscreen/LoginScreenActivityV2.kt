package io.github.wykopmobilny.ui.modules.loginscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.wykopmobilny.databinding.ActivityContainerBinding
import io.github.wykopmobilny.ui.login.android.loginFragment
import io.github.wykopmobilny.utils.viewBinding

class LoginScreenActivityV2 : AppCompatActivity() {

    private val binding by viewBinding(ActivityContainerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, loginFragment())
                .commit()
        }
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, LoginScreenActivityV2::class.java)
    }
}
