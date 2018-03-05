package io.github.feelfreelinux.wykopmobilny.utils


import android.app.Activity
import android.os.Build
import android.view.View

/**
 * Class responsible for changing the view from full screen to non-full screen and vice versa.
 *
 * @author Pierfrancesco Soffritti
 */
class FullScreenManager (private val context: Activity, private vararg val views: View) {

    /**
     * call this method to enter full screen
     */
    fun enterFullScreen() {
        val decorView = context.window.decorView

        hideSystemUI(decorView)

        for (view in views) {
            view.visibility = View.GONE
            view.invalidate()
        }
    }

    /**
     * call this method to exit full screen
     */
    fun exitFullScreen() {
        val decorView = context.window.decorView

        showSystemUI(decorView)

        for (view in views) {
            view.visibility = View.VISIBLE
            view.invalidate()
        }
    }

    // hides the system bars.
    private fun hideSystemUI(mDecorView: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    // This snippet shows the system bars.
    private fun showSystemUI(mDecorView: View) {
        mDecorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //        mDecorView.setSystemUiVisibility(
        //                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}