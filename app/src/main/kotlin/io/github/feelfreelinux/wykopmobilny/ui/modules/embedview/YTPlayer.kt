package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast

import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.ErrorReason
import com.google.android.youtube.player.YouTubePlayerView

import android.content.Context
import android.media.AudioManager
import androidx.core.content.ContextCompat.startActivity
import android.content.pm.ResolveInfo
import android.net.Uri


import android.app.Activity
import android.view.View
import android.view.WindowManager
import androidx.annotation.NonNull
import io.github.feelfreelinux.wykopmobilny.GOOGLE_KEY
import java.util.regex.Matcher
import java.util.regex.Pattern


object YouTubeUrlParser {

    // (?:youtube(?:-nocookie)?\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})
    internal val reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"

    fun getVideoId(@NonNull videoUrl: String): String? {
        val pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(videoUrl)

        return if (matcher.find()) matcher.group(1) else null
    }

    fun getVideoUrl(@NonNull videoId: String): String {
        return "http://youtu.be/$videoId"
    }
}

object StatusBarUtil {

    fun hide(activity: Activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = activity.window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        }
    }
}


object AudioUtil {

    private val mSingletonLock = Any()
    private var audioManager: AudioManager? = null

    private fun getInstance(context: Context?): AudioManager? {
        synchronized(mSingletonLock) {
            if (audioManager != null)
                return audioManager
            if (context != null)
                audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager
        }
    }

    fun adjustMusicVolume(context: Context, up: Boolean, showInterface: Boolean) {
        val direction = if (up) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER
        val flag = AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE or if (showInterface) AudioManager.FLAG_SHOW_UI else 0
        getInstance(context)!!.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, flag)
    }

    fun playKeyClickSound(context: Context, volume: Int) {
        if (volume == 0)
            return
        getInstance(context)!!.playSoundEffect(AudioManager.FX_KEY_CLICK, volume.toFloat() / 100.0f)
    }
}

enum class Orientation {
    AUTO, AUTO_START_WITH_LANDSCAPE, ONLY_LANDSCAPE, ONLY_PORTRAIT
}

class YTPlayer : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener, YouTubePlayer.PlayerStateChangeListener {

    private var googleApiKey: String? = null
    private var videoId: String? = null

    private var playerStyle: YouTubePlayer.PlayerStyle? = null
    private var orientation: Orientation? = null
    private var showAudioUi: Boolean = false
    private var handleError: Boolean = false
    private var animEnter: Int = 0
    private var animExit: Int = 0

    private var playerView: YouTubePlayerView? = null
    private var player: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()

        playerView = YouTubePlayerView(this)
        playerView!!.initialize(googleApiKey, this)

        addContentView(playerView, LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT))

        playerView!!.setBackgroundResource(android.R.color.black)

        StatusBarUtil.hide(this)
    }

    private fun initialize() {
        try {
            val ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            googleApiKey = GOOGLE_KEY
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (googleApiKey == null)
            throw NullPointerException("Google API key must not be null. Set your api key as meta data in AndroidManifest.xml file.")

        videoId = intent.getStringExtra(EXTRA_VIDEO_ID)
        if (videoId == null)
            throw NullPointerException("Video ID must not be null")

            playerStyle = YouTubePlayer.PlayerStyle.DEFAULT

            orientation = Orientation.AUTO

        showAudioUi = intent.getBooleanExtra(EXTRA_SHOW_AUDIO_UI, true)
        handleError = intent.getBooleanExtra(EXTRA_HANDLE_ERROR, true)
        animEnter = intent.getIntExtra(EXTRA_ANIM_ENTER, 0)
        animExit = intent.getIntExtra(EXTRA_ANIM_EXIT, 0)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                         player: YouTubePlayer,
                                         wasRestored: Boolean) {
        this.player = player
        player.setOnFullscreenListener(this)
        player.setPlayerStateChangeListener(this)

        when (orientation) {
            Orientation.AUTO -> player.fullscreenControlFlags = (YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
            Orientation.AUTO_START_WITH_LANDSCAPE -> {
                player.fullscreenControlFlags = (YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                        or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                        or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                        or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
                player.setFullscreen(true)
            }
            Orientation.ONLY_LANDSCAPE -> {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                player.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
                player.setFullscreen(true)
            }
            Orientation.ONLY_PORTRAIT -> {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                player.fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
                player.setFullscreen(true)
            }
        }

        when (playerStyle) {
            YouTubePlayer.PlayerStyle.CHROMELESS -> player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
            YouTubePlayer.PlayerStyle.MINIMAL -> player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
            YouTubePlayer.PlayerStyle.DEFAULT -> player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            else -> player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        }

        if (!wasRestored)
            player.loadVideo(videoId)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                         errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1\$s)",
                    errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            playerView!!.initialize(googleApiKey, this)
        }
    }

    // YouTubePlayer.OnFullscreenListener
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (orientation) {
            Orientation.AUTO, Orientation.AUTO_START_WITH_LANDSCAPE -> if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (player != null)
                    player!!.setFullscreen(true)
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && player != null) {
                player!!.setFullscreen(false)
            }
            Orientation.ONLY_LANDSCAPE, Orientation.ONLY_PORTRAIT -> {
            }
        }
    }

    override fun onFullscreen(fullScreen: Boolean) {
        when (orientation) {
            Orientation.AUTO, Orientation.AUTO_START_WITH_LANDSCAPE -> if (fullScreen)
                requestedOrientation = LANDSCAPE_ORIENTATION
            else
                requestedOrientation = PORTRAIT_ORIENTATION
            Orientation.ONLY_LANDSCAPE, Orientation.ONLY_PORTRAIT -> {
            }
        }
    }

    // YouTubePlayer.PlayerStateChangeListener
    override fun onError(reason: ErrorReason) {
        Log.e("onError", "onError : " + reason.name)
        if (handleError && ErrorReason.NOT_PLAYABLE == reason) {
            val video_uri = Uri.parse(YouTubeUrlParser.getVideoUrl(videoId!!))
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            val list = getPackageManager().queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY)

            if (list.isEmpty())
                intent = Intent(Intent.ACTION_VIEW, video_uri)

            startActivity(intent)
        }
    }

    override fun onAdStarted() {}

    override fun onLoaded(videoId: String) {}

    override fun onLoading() {}

    override fun onVideoEnded() {}

    override fun onVideoStarted() {
        StatusBarUtil.hide(this)
    }

    // Audio Managing
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            AudioUtil.adjustMusicVolume(applicationContext, true, showAudioUi)
            StatusBarUtil.hide(this)
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioUtil.adjustMusicVolume(applicationContext, false, showAudioUi)
            StatusBarUtil.hide(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // Animation
    override fun onBackPressed() {
        super.onBackPressed()
        if (animEnter != 0 && animExit != 0)
            overridePendingTransition(animEnter, animExit)
    }

    companion object {

        private val RECOVERY_DIALOG_REQUEST = 1


        val EXTRA_VIDEO_ID = "video_id"

        val EXTRA_PLAYER_STYLE = "player_style"

        val EXTRA_ORIENTATION = "orientation"

        val EXTRA_SHOW_AUDIO_UI = "show_audio_ui"

        val EXTRA_HANDLE_ERROR = "handle_error"

        val EXTRA_ANIM_ENTER = "anim_enter"
        val EXTRA_ANIM_EXIT = "anim_exit"

        @SuppressLint("InlinedApi")
        private val PORTRAIT_ORIENTATION = if (Build.VERSION.SDK_INT < 9)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        @SuppressLint("InlinedApi")
        private val LANDSCAPE_ORIENTATION = if (Build.VERSION.SDK_INT < 9)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }
}