package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AddLinkPreviewImage
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.addlink_details_fragment.*
import kotlinx.android.synthetic.main.addlink_preview_image.*
import kotlinx.android.synthetic.main.addlink_preview_image.view.*
import javax.inject.Inject

class AddLinkDetailsFragment : BaseFragment(), AddLinkDetailsFragmentView {
    @Inject lateinit var presenter : AddLinkDetailsFragmentPresenter
    @Inject lateinit var navigator : NavigatorApi

    var imageKey : String = ""
    val draftInformation by lazy { (activity as AddlinkActivity).draft.data }
    companion object {
        fun newInstance() : AddLinkDetailsFragment {
            return AddLinkDetailsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addlink_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        draftInformation?.apply {
            input_description.setText(description)
            input_link_title.setText(title)
            presenter.getImages(key)
        }

        submit.setOnClickListener {
            validate()
        }
    }

    override fun openLinkScreen(link: Link) {
        navigator.openLinkDetailsActivity(activity!!, link)
        activity!!.finish()
    }

    override fun showImages(images: List<AddLinkPreviewImage>) {
        images.forEach {
            val view = layoutInflater.inflate(R.layout.addlink_preview_image, imagesList, false)
            view.previewImage.loadImage(it.sourceUrl)
            imagesList.addView(view)
            imageKey = it.key
            view.setOnClickListener {
                (0 until imagesList.childCount).forEach {
                    imagesList.getChildAt(it).tick.isVisible = false
                }
                view.tick.isVisible = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        presenter.dispose()
    }

    fun validate() {
        if (input_description.text.isEmpty()) {
            input_description_layout.error = getString(R.string.addlink_no_description)
            return
        }

        if (input_link_title.text.isEmpty()) {
            input_link_title_layout.error = getString(R.string.add_link_no_title)
            return
        }

        if (input_tags.text.isEmpty()) {
            input_tags_layout.error = getString(R.string.add_link_no_tags)
            return
        }

        presenter.publishLink(draftInformation!!.key, input_link_title.text.toString(), draftInformation!!.sourceUrl, input_description.text.toString(), input_tags.text.toString(), plus18_checkbox.isChecked, imageKey!!)
    }

    override fun showImagesLoading(visibility : Boolean) {
        imagesLoadingView.isVisible = visibility
    }

    override fun showLinkUploading(visibility: Boolean) {
        previewTitle.isVisible = !visibility
        input_tags.isVisible = !visibility
        input_link_title.isVisible = !visibility
        input_description.isVisible = !visibility
        progressBar.isVisible = visibility
        progressBarTitle.isVisible = visibility
        imagesLoadingView.isVisible = !visibility
    }
}