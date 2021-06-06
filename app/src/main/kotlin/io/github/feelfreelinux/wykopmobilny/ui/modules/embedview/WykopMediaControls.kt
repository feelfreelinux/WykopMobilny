package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.devbrackets.android.exomedia.ui.widget.VideoControls
import com.devbrackets.android.exomedia.util.TimeFormatUtil
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.MediaControlsBinding
import java.util.LinkedList

class WykopMediaControls : VideoControls {

    private lateinit var seekBar: SeekBar
    private lateinit var extraViewsContainer: LinearLayout
    private lateinit var controlsView: View

    private var userInteracting = false

    class FadeInListener(val view: View) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            view.isVisible = true
        }

        companion object {
            fun animateVisibility(view: View) {
                view.animate()
                    .setDuration(300)
                    .alpha(1f)
                    .setListener(FadeInListener(view))
            }
        }
    }

    class FadeOutListener(val view: View) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            view.isVisible = false
        }

        companion object {
            fun animateVisibility(view: View) {
                view.animate()
                    .setDuration(300)
                    .alpha(0.0f)
                    .setListener(FadeOutListener(view))
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayoutResource() = R.layout.media_controls

    override fun setPosition(position: Long) {
        currentTimeTextView.text = TimeFormatUtil.formatMs(position)
        seekBar.progress = position.toInt()
    }

    override fun setDuration(duration: Long) {
        if (duration != seekBar.max.toLong()) {
            endTimeTextView.text = TimeFormatUtil.formatMs(duration)
            seekBar.max = duration.toInt()
        }
    }

    override fun updateProgress(position: Long, duration: Long, bufferPercent: Int) {
        if (!userInteracting) {
            seekBar.secondaryProgress = (seekBar.max * (bufferPercent.toFloat() / 100)).toInt()
            seekBar.progress = position.toInt()
            currentTimeTextView.text = TimeFormatUtil.formatMs(position)
        }
    }

    override fun retrieveViews() {
        super.retrieveViews()
        val binding = MediaControlsBinding.bind(this)
        controlsView = binding.controlsLayout
        seekBar = binding.playerSeekbar
        extraViewsContainer = binding.test
    }

    override fun registerListeners() {
        super.registerListeners()
        seekBar.setOnSeekBarChangeListener(SeekBarChanged())
    }

    override fun addExtraView(view: View) {
        extraViewsContainer.addView(view)
    }

    override fun removeExtraView(view: View) {
        extraViewsContainer.removeView(view)
    }

    override fun getExtraViews(): List<View> {
        val childCount = extraViewsContainer.childCount
        if (childCount <= 0) {
            return super.getExtraViews()
        }

        // Retrieves the layouts children
        val children = LinkedList<View>()
        for (i in 0 until childCount) {
            children.add(extraViewsContainer.getChildAt(i))
        }

        return children
    }

    override fun hideDelayed(delay: Long) {
        hideDelay = delay

        if (delay < 0 || !canViewHide || isLoading) {
            return
        }

        // If the user is interacting with controls we don't want to start the delayed hide yet
        if (!userInteracting) {
            visibilityHandler.postDelayed({ animateVisibility(false) }, delay)
        }
    }

    override fun animateVisibility(toVisible: Boolean) {
        if (isVisible == toVisible) {
            return
        }

        if (!isLoading) {
            if (toVisible) {
                FadeInListener.animateVisibility(endTimeTextView)
                FadeInListener.animateVisibility(currentTimeTextView)
                FadeInListener.animateVisibility(seekBar)
                FadeInListener.animateVisibility(playPauseButton)
                controlsView.animate().setDuration(300).alpha(1f)
            } else {
                controlsView.animate().setDuration(300).alpha(0f)
                FadeOutListener.animateVisibility(endTimeTextView)
                FadeOutListener.animateVisibility(currentTimeTextView)
                FadeOutListener.animateVisibility(seekBar)
                FadeOutListener.animateVisibility(playPauseButton)
            }
        }

        isVisible = toVisible
        onVisibilityChanged()
    }

    override fun updateTextContainerVisibility() {
        if (!isVisible) {
            return
        }
    }

    override fun showLoading(initialLoad: Boolean) {
        if (isLoading) {
            return
        }

        isLoading = true
        loadingProgressBar.isVisible = false

        if (initialLoad) {
            endTimeTextView.isVisible = false
            currentTimeTextView.isVisible = false
            seekBar.isVisible = false
            playPauseButton.isVisible = false
        } else {
            playPauseButton.isEnabled = false
            previousButton.isEnabled = false
            nextButton.isEnabled = false
        }

        show()
    }

    override fun show() {
        super.show()
        controlsView.animate().setDuration(300).alpha(1f)
        endTimeTextView.isVisible = true
        currentTimeTextView.isVisible = true
        seekBar.isVisible = true
        playPauseButton.isVisible = true
    }

    override fun hide() {
        super.hide()
        animateVisibility(false)
    }

    override fun finishLoading() {
        if (!isLoading) {
            return
        }

        isLoading = false
        loadingProgressBar.isVisible = false

        playPauseButton.isEnabled = true
        previousButton.isEnabled = true
        nextButton.isEnabled = true
        updatePlaybackState(true)
        hide()
    }

    override fun updatePlaybackState(isPlaying: Boolean) {
        updatePlayPauseImage(isPlaying)
        progressPollRepeater.start()
    }

    /**
     * Listens to the seek bar change events and correctly handles the changes
     */
    private inner class SeekBarChanged : SeekBar.OnSeekBarChangeListener {
        private var seekToTime: Long = 0

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (!fromUser) {
                return
            }

            seekToTime = progress.toLong()
            if (currentTimeTextView != null) {
                currentTimeTextView.text = TimeFormatUtil.formatMs(seekToTime)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            userInteracting = true
            if (seekListener == null || !seekListener!!.onSeekStarted()) {
                internalListener.onSeekStarted()
            }
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            userInteracting = false
            if (seekListener == null || !seekListener!!.onSeekEnded(seekToTime)) {
                internalListener.onSeekEnded(seekToTime)
            }
        }
    }
}
