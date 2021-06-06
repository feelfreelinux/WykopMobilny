package io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import io.github.wykopmobilny.api.responses.AddLinkPreviewImage
import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Link

interface AddLinkDetailsFragmentView : BaseView {
    fun showImages(images: List<AddLinkPreviewImage>)
    fun openLinkScreen(link: Link)
    fun showImagesLoading(visibility: Boolean)
    fun showLinkUploading(visibility: Boolean)
}
