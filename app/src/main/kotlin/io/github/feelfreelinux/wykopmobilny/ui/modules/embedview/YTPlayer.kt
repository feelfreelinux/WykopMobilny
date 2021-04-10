package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import io.github.feelfreelinux.wykopmobilny.GOOGLE_KEY
import io.github.feelfreelinux.wykopmobilny.utils.youtubeTimestampToMsOrNull

import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.ErrorReason
import com.google.android.youtube.player.YouTubePlayerView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast

import java.net.URLDecoder


object YouTubeUrlParser {
    private val consentRegex = "consent.youtube.com/.+[?&]continue=([a-z0-9-_.~%]+[^&\\n])".toRegex(RegexOption.IGNORE_CASE)
    private val videoRegex = "(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-z0-9_-]{11})".toRegex(RegexOption.IGNORE_CASE)
    private val timestampRegex = "t=([^#&\\n\\r]+)".toRegex(RegexOption.IGNORE_CASE)

    fun getVideoId(videoUrl: String): String? {
        val unwrappedUrl = unwrapConsentYoutubeUrl(videoUrl)
        return findInUrl(videoRegex, unwrappedUrl)
    }

    fun getTimestamp(videoUrl: String) : String? {
        return findInUrl(timestampRegex, videoUrl)
    }

    fun getVideoUrl(videoId: String): String {
        return "http://youtu.be/$videoId"
    }

    fun isVideoUrl(url: String): Boolean {
        return videoRegex.find(unwrapConsentYoutubeUrl(url)) != null
    }

    private fun findInUrl(regex : Regex, url : String) : String? {
        val match = regex.find(url)
        return match?.groupValues?.get(1)
    }

    private fun unwrapConsentYoutubeUrl(url: String): String {
        val match = consentRegex.find(url) ?: return url
        return URLDecoder.decode(match.groupValues[1], Charsets.UTF_8.name())
    }
}

object StatusBarUtil {

    fun hide(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
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
}

enum class Orientation {
    AUTO, AUTO_START_WITH_LANDSCAPE, ONLY_LANDSCAPE, ONLY_PORTRAIT
}

class YTPlayer : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener, YouTubePlayer.PlayerStateChangeListener {

    private var googleApiKey: String? = null
    private var videoId: String? = null
    private var timestampMs : Int? = null

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

        intent.getStringExtra(EXTRA_TIMESTAMP)?.let { t ->
            timestampMs = t.youtubeTimestampToMsOrNull()
        }

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
            player.loadVideo(videoId, timestampMs ?: 0)
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
            Orientation.AUTO, Orientation.AUTO_START_WITH_LANDSCAPE -> requestedOrientation = if (fullScreen)
                LANDSCAPE_ORIENTATION
            else
                PORTRAIT_ORIENTATION
            Orientation.ONLY_LANDSCAPE, Orientation.ONLY_PORTRAIT -> {
            }
        }
    }

    // YouTubePlayer.PlayerStateChangeListener
    override fun onError(reason: ErrorReason) {
        Log.e("onError", "onError : " + reason.name)
        if (handleError && ErrorReason.NOT_PLAYABLE == reason) {
            val videoUri = Uri.parse(YouTubeUrlParser.getVideoUrl(videoId!!))
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            val list = packageManager.queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY)

            if (list.isEmpty())
                intent = Intent(Intent.ACTION_VIEW, videoUri)

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
        const val EXTRA_VIDEO_ID = "video_id"
        const val EXTRA_TIMESTAMP = "timestamp"
        const val EXTRA_SHOW_AUDIO_UI = "show_audio_ui"
        const val EXTRA_HANDLE_ERROR = "handle_error"
        const val EXTRA_ANIM_ENTER = "anim_enter"
        const val EXTRA_ANIM_EXIT = "anim_exit"

        private const val RECOVERY_DIALOG_REQUEST = 1
        private const val PORTRAIT_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        private const val LANDSCAPE_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }
}
