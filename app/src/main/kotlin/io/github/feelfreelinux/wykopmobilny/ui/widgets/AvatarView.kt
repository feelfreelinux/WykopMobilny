package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.databinding.AvatarViewBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.loadImage

class AvatarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding = AvatarViewBinding.inflate(layoutInflater, this, true)

    fun setAuthor(author: Author) {
        author.apply {
            binding.avatarImageView.loadImage(avatarUrl)
            binding.genderStripImageView.setBackgroundResource(getGenderStripResource(sex))
        }
    }
}
