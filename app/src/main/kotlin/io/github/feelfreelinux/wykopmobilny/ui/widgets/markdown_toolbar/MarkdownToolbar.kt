package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.formatDialogCallback
import io.github.feelfreelinux.wykopmobilny.utils.getMimeType
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

    var mPhoto : Uri? = null

    var photo : Uri?
        get() = mPhoto
        set(value) {
            mPhoto = value
            photoUrl = null
        }

    var photoUrl : String? = null

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
        photo = null
        photoUrl = url
        // @TODO add imagepreview in some type of cloud icon
    }

    fun getPhotoTypedInputStream(): TypedInputStream? {
        photo?.apply {
            val contentResolver = context.contentResolver
            return TypedInputStream(photo?.queryFileName(contentResolver)!!,
                    getMimeType(contentResolver),
                    contentResolver.openInputStream(photo))
        }
        return null
    }

    fun hasUserEditedContent() : Boolean {
        return (photo != null ||
                !photoUrl.isNullOrEmpty() ||
                (markdownListener != null && markdownListener?.textBody!!.isNotEmpty()))
    }

}