package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent

import java.util.regex.Pattern


private val youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
private val videoIdRegex = arrayOf("\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9_\\-]*)")

class YoutubeActivity {
    /*override fun onFullscreen(fullScreen: Boolean) {
        if (fullScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        }
    }*/

    companion object {
        const val REQUEST_CODE_INITIALIZATION_ERROR = 172
        const val EXTRA_URL = "URLEXTRA"

        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, YTPlayer::class.java)
            intent.putExtra(YTPlayer.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(url))
            return intent
        }

        private fun extractVideoIdFromUrl(url: String): String? {
            val youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url)

            for (regex in videoIdRegex) {
                val compiledPattern = Pattern.compile(regex)
                val matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain)

                if (matcher.find()) {
                    return matcher.group(1)
                }
            }

            return null
        }


        fun youTubeLinkWithoutProtocolAndDomain(fullurl: String): String {
            val url = fullurl.replace("m.", "")
            val compiledPattern = Pattern.compile(youTubeUrlRegEx)
            val matcher = compiledPattern.matcher(url)

            return if (matcher.find()) {
                url.replace(matcher.group(), "")
            } else url
        }
    }
/*
    val autoRotation by lazy {
        Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
    }


    private val extraURL by lazy { intent.getStringExtra(EXTRA_URL) }
    var player: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtubeplayer)
/*        //youtubePlayer.initialize(GOOGLE_KEY, this)

        startActivity(createVideoIntent(this, GOOGLE_KEY, extractVideoIdFromUrl(extraURL.replace("m.", "")), 0, true, true))
        finish()*/
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {
        if (result?.isUserRecoverableError == true) { // ¯\_(ツ)_/¯
            // Show error dialog to user, will try to initialize again after showing this
            result.getErrorDialog(this, REQUEST_CODE_INITIALIZATION_ERROR).show()
            return
        }

        Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, restored: Boolean) {
        if (!restored) {
            this.player = player
            player?.prepare()
            player?.loadVideo(extractVideoIdFromUrl(extraURL.replace("m.", "")))
        }
    }

    fun YouTubePlayer.prepare() {
        setOnFullscreenListener(this@YoutubeActivity)
        if (autoRotation) {
            addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        } else {
            addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        }
        setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.player?.setFullscreen(true)
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.player?.setFullscreen(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INITIALIZATION_ERROR) {
            youtubePlayer.initialize(GOOGLE_KEY, this)
        }
    }*/
}