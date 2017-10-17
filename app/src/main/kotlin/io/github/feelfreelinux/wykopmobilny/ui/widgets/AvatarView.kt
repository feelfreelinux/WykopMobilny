package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.main.avatar_view.view.*

class AvatarView : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.avatar_view, this)
    }

    fun setAuthor(author : Author) {
        author.apply {
            avatarImageView.loadImage(avatarUrl)
            genderStripImageView.setBackgroundResource(getGenderStripResource(sex))
            if(!(sex.equals("male") || sex.equals("famale"))) {
                genderStripImageView.visibility = View.GONE
            }
        }
    }
}