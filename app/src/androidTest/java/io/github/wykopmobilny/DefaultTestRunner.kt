package io.github.wykopmobilny

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import java.util.Locale

class DefaultTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application =
        super.newApplication(cl, TestApp::class.java.name, context)

    override fun onCreate(arguments: Bundle?) {
        Locale.setDefault(Locale("en", "US"))
        super.onCreate(arguments)
    }
}
