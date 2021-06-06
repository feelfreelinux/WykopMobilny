package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.databinding.FloatingImageViewLayoutBinding
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.loadImage

class FloatingImageView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding = FloatingImageViewLayoutBinding.inflate(layoutInflater, this)

    init {
        this.isVisible = false
        binding.deleteButton.setOnClickListener { removeImage() }
    }

    var photo: Uri? = null
    var photoUrl: String? = null

    fun removeImage() {
        photo = null
        photoUrl = null
        isVisible = false
    }

    fun loadPhotoUrl(photoUrl: String) {
        isVisible = true
        photo = null
        this.photoUrl = photoUrl
        binding.imageView.loadImage(photoUrl)
    }

    fun setImage(photo: Uri?) {
        this.photo = photo
        photo?.let {
            isVisible = true
            GlideApp.with(context).load(photo).into(binding.imageView)
        }
    }
}
