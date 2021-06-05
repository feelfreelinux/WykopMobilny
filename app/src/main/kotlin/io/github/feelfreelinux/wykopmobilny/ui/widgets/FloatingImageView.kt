package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.floating_image_view_layout.view.*

class FloatingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.floating_image_view_layout, this)
        isVisible = false
        deleteButton.setOnClickListener { removeImage() }
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
        imageView.loadImage(photoUrl)
    }

    fun setImage(photo: Uri?) {
        this.photo = photo
        photo?.let {
            isVisible = true
            GlideApp.with(context).load(photo).into(imageView)
        }
    }
}
