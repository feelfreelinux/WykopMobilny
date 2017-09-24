package io.github.feelfreelinux.wykopmobilny.ui.elements.author_header_view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.author_header_layout.view.*

class AuthorHeaderView : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.author_header_layout, this)
    }

    fun setAuthorData(author : Author, date : String) {
        author.apply {
            userNameTextView.apply {
                text = nick
                setTextColor(getGroupColor(group))
            }

            avatarImageView.loadImage(avatarUrl)
            val prettyDate = date.toPrettyDate()
            entryDateTextView.text = prettyDate

            app?.let {
                entryDateTextView.text = context.getString(R.string.date_with_user_app, prettyDate, app)
            }

            genderStripImageView.setBackgroundResource(getGenderStripResource(sex))
        }
    }

}