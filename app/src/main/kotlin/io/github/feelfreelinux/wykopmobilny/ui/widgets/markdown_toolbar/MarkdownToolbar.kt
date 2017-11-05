package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.formatDialogCallback
import io.github.feelfreelinux.wykopmobilny.ui.widgets.FloatingImageView
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.getMimeType
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.queryFileName
import kotlinx.android.synthetic.main.markdown_toolbar.view.*

interface MarkdownToolbarListener {
    var selectionPosition : Int
    var textBody : String
    fun openGalleryImageChooser()
}

class MarkdownToolbar : LinearLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var markdownListener : MarkdownToolbarListener? = null

    private val markdownDialogs by lazy { MarkdownDialogs(context) }

    private var formatText: formatDialogCallback = {
        markdownListener?.apply {
            val prefix = textBody.substring(0, selectionPosition)
            textBody = prefix + it + textBody.substring(selectionPosition, textBody.length)
            selectionPosition = prefix.length + it.length
        }
    }

    var floatingImageView : FloatingImageView? = null

    var photoUrl : String?
        get() = floatingImageView?.photoUrl
        set(value) { if(value != null) { floatingImageView?.loadPhotoUrl(value) } else floatingImageView?.removeImage() }

    var photo : Uri?
        get() = floatingImageView?.photo
        set(value) {
            floatingImageView?.setImage(value)
        }

    init {
        View.inflate(context, R.layout.markdown_toolbar, this)

        // Create callbacks
        markdownDialogs.apply {
            format_bold.setOnClickListener { showMarkdownBoldDialog(formatText) }
            format_quote.setOnClickListener { showMarkdownQuoteDialog(formatText) }
            format_italic.setOnClickListener { showMarkdownItalicDialog(formatText) }
            insert_link.setOnClickListener { showMarkdownLinkDialog(formatText) }
            insert_code.setOnClickListener { showMarkdownSourceCodeDialog(formatText) }
            insert_spoiler.setOnClickListener { showMarkdownSpoilerDialog(formatText) }
            insert_emoticon.setOnClickListener { showLennyfaceDialog(formatText) }
            insert_photo.setOnClickListener { showUploadPhotoDialog(
                    { insertImageFromUrl(it) },
                    { markdownListener?.openGalleryImageChooser() })
            }

        }
    }

    fun insertImageFromUrl(url : String) {
        floatingImageView?.loadPhotoUrl(url)
    }

    fun getPhotoTypedInputStream(): TypedInputStream? {
        photo?.apply {
            val contentResolver = getActivityContext()!!.contentResolver
            photo?.queryFileName(contentResolver)?.apply {
                return TypedInputStream(
                        this,
                        getMimeType(contentResolver),
                        contentResolver.openInputStream(photo))
            }
        }
        return null
    }

    fun hasUserEditedContent() : Boolean {
        return (photo != null ||
                !floatingImageView?.photoUrl.isNullOrEmpty() ||
                (markdownListener != null && markdownListener?.textBody!!.isNotEmpty()))
    }

}