package io.github.feelfreelinux.wykopmobilny.ui.widgets.markdown_toolbar

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.EditTextFormatDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.formatDialogCallback
import io.github.feelfreelinux.wykopmobilny.ui.widgets.FloatingImageView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.InputToolbar
import io.github.feelfreelinux.wykopmobilny.utils.CameraUtils
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import kotlinx.android.synthetic.main.imagechooser_bottomsheet.view.*
import kotlinx.android.synthetic.main.markdown_toolbar.view.*

interface MarkdownToolbarListener {
    var selectionStart : Int
    var selectionEnd : Int
    var textBody : String
    fun setSelection(start : Int, end : Int)
    fun openGalleryImageChooser()
    fun openCamera(uri : Uri)
}

class MarkdownToolbar : LinearLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var markdownListener : MarkdownToolbarListener? = null
    private val markdownDialogs by lazy { MarkdownDialogs(context) }
    var remoteImageInserted : () -> Unit = {}

    private var formatText: formatDialogCallback = {
        markdownListener?.apply {
            val prefix = textBody.substring(0, selectionStart)
            textBody = prefix + it + textBody.substring(selectionStart, textBody.length)
            selectionStart = prefix.length + it.length
        }
    }

    fun insertFormat(prefix : String, suffix : String) {
        markdownListener?.apply {
            if (selectionEnd > selectionStart) {
                val bodyPrefix = textBody.substring (0, selectionStart)
                val bodySuffix = textBody.substring(selectionEnd, textBody.length)
                val selectedText = textBody.substring(selectionStart, selectionEnd)
                textBody = bodyPrefix + prefix + selectedText + suffix + bodySuffix
                setSelection(bodyPrefix.length + prefix.length, bodyPrefix.length + prefix.length + selectedText.length)
            } else {
                val bodyPrefix = textBody.substring (0, selectionStart)
                val bodySuffix = textBody.substring(selectionStart, textBody.length)
                val selectedText = "tekst"
                textBody = bodyPrefix + prefix + selectedText + suffix + bodySuffix
                setSelection(bodyPrefix.length + prefix.length, bodyPrefix.length + prefix.length + selectedText.length)}
        }
    }

    var containsAdultContent = false

    var floatingImageView : FloatingImageView? = null

    var photoUrl : String?
        get() = floatingImageView?.photoUrl
        set(value) { if(value != null) {
            remoteImageInserted()
            floatingImageView?.loadPhotoUrl(value)
        } else floatingImageView?.removeImage() }

    var photo : Uri?
        get() = floatingImageView?.photo
        set(value) {
            floatingImageView?.setImage(value)
        }

    init {
        View.inflate(context, R.layout.markdown_toolbar, this)

        // Create callbacks
        markdownDialogs.apply {
            format_bold.setOnClickListener { insertFormat("**", "**") }
            format_quote.setOnClickListener { insertFormat("\n>", "") }
            format_italic.setOnClickListener { insertFormat("__", "__") }
            insert_link.setOnClickListener { insertFormat("[", "](www.wykop.pl)") }
            insert_code.setOnClickListener { insertFormat("`", "`") }
            insert_spoiler.setOnClickListener { insertFormat("\n!", "") }
            insert_emoticon.setOnClickListener { showLennyfaceDialog(formatText) }
            insert_photo.setOnClickListener { showUploadPhotoBottomsheet() }

        }
    }

    fun showUploadPhotoBottomsheet() {
        val activityContext = getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.imagechooser_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            insert_gallery.setOnClickListener {
                markdownListener?.openGalleryImageChooser()
                dialog.dismiss()
            }

            insert_camera.setOnClickListener {
                val cameraUri = CameraUtils.createPictureUri(context)
                markdownListener?.openCamera(cameraUri!!)
                dialog.dismiss()
            }

            insert_url.setOnClickListener {
                EditTextFormatDialog(R.string.insert_photo_url, context, { insertImageFromUrl(it) }).show()
                dialog.dismiss()
            }

            mark_nsfw_checkbox.isChecked = containsAdultContent
            mark_nsfw_checkbox.setOnCheckedChangeListener { _, isChecked ->
                containsAdultContent = isChecked
            }

            mark_nsfw.setOnClickListener {
                mark_nsfw_checkbox.performClick()
            }
        }

        val mBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    fun insertImageFromUrl(url : String) {
        floatingImageView?.loadPhotoUrl(url)
    }

    fun getWykopImageFile(): WykopImageFile? {
        photo?.apply {
            return WykopImageFile(photo!!, context)
        }
        return null
    }

    fun hasUserEditedContent() : Boolean {
        return (photo != null ||
                !floatingImageView?.photoUrl.isNullOrEmpty() ||
                (markdownListener != null && markdownListener?.textBody!!.isNotEmpty()))
    }

}