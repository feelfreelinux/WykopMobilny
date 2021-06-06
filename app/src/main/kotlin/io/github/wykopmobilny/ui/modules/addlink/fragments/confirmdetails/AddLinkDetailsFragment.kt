package io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.responses.AddLinkPreviewImage
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.AddlinkDetailsFragmentBinding
import io.github.wykopmobilny.databinding.AddlinkPreviewImageBinding
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.modules.NavigatorApi
import io.github.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.wykopmobilny.utils.loadImage
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class AddLinkDetailsFragment : BaseFragment(R.layout.addlink_details_fragment), AddLinkDetailsFragmentView {

    companion object {
        fun newInstance() = AddLinkDetailsFragment()
    }

    @Inject
    lateinit var presenter: AddLinkDetailsFragmentPresenter

    @Inject
    lateinit var navigator: NavigatorApi

    private val binding by viewBinding(AddlinkDetailsFragmentBinding::bind)

    private var imageKey: String = ""
    private val draftInformation by lazy { (activity as AddlinkActivity).draft.data }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        draftInformation?.apply {
            binding.inputDescription.setText(description)
            binding.inputLinkTitle.setText(title)
            presenter.getImages(key)
        }

        binding.submit.setOnClickListener { validate() }
    }

    override fun openLinkScreen(link: Link) {
        navigator.openLinkDetailsActivity(requireActivity(), link)
        requireActivity().finish()
    }

    override fun showImages(images: List<AddLinkPreviewImage>) {
        images.forEach {
            val view = AddlinkPreviewImageBinding.inflate(layoutInflater, binding.imagesList, false)
            view.previewImage.loadImage(it.sourceUrl)
            binding.imagesList.addView(view.root)
            imageKey = it.key
            view.root.setOnClickListener {
                binding.imagesList.children
                    .map(AddlinkPreviewImageBinding::bind)
                    .forEach { image -> image.tick.isVisible = false }
                view.tick.isVisible = true
            }
        }
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    private fun validate() {
        if (binding.inputDescription.text.isEmpty()) {
            binding.inputDescriptionLayout.error = getString(R.string.addlink_no_description)
            return
        }

        if (binding.inputLinkTitle.text.isEmpty()) {
            binding.inputLinkTitleLayout.error = getString(R.string.add_link_no_title)
            return
        }

        if (binding.inputTags.text.isEmpty()) {
            binding.inputTagsLayout.error = getString(R.string.add_link_no_tags)
            return
        }

        presenter.publishLink(
            draftInformation!!.key,
            binding.inputLinkTitle.text.toString(),
            draftInformation!!.sourceUrl,
            binding.inputDescription.text.toString(),
            binding.inputTags.text.toString(),
            binding.plus18Checkbox.isChecked,
            imageKey
        )
    }

    override fun showImagesLoading(visibility: Boolean) {
        binding.imagesLoadingView.isVisible = visibility
    }

    override fun showLinkUploading(visibility: Boolean) {
        binding.previewTitle.isVisible = !visibility
        binding.inputTags.isVisible = !visibility
        binding.inputLinkTitle.isVisible = !visibility
        binding.inputDescription.isVisible = !visibility
        binding.progressBar.isVisible = visibility
        binding.progressBarTitle.isVisible = visibility
        binding.imagesLoadingView.isVisible = !visibility
    }
}
