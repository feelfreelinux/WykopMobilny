package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AddLinkPreviewImage

interface AddLinkDetailsFragmentView : BaseView {
    fun showImages(images : List<AddLinkPreviewImage>)
    fun openLinkScreen(link : Link)
    fun showImagesLoading(visibility : Boolean)
    fun showLinkUploading(visibility : Boolean)
}