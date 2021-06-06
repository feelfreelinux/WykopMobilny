package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import androidx.core.view.isVisible
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.databinding.AddlinkFragmentBinding
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class AddlinkUrlInputFragment : BaseFragment(R.layout.addlink_fragment), AddLinkUrlInputFragmentView {

    companion object {
        const val EXTRA_URL = "ADDLINK_URL"

        fun newInstance(url: String = ""): AddlinkUrlInputFragment {
            val fragment = AddlinkUrlInputFragment()
            val data = Bundle()
            data.putString(EXTRA_URL, url)
            fragment.arguments = data
            return fragment
        }
    }

    @Inject
    lateinit var presenter: AddLinkUrlInputPresenter

    private val binding by viewBinding(AddlinkFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        binding.addLink.setOnClickListener {
            if (binding.linkUrl.text.isNotEmpty() && URLUtil.isValidUrl(binding.linkUrl.text.toString())) {
                presenter.createDraft(binding.linkUrl.text.toString())
            } else {
                binding.linkUrlLayout.error = getString(R.string.invalid_url)
            }
        }
        binding.linkUrl.setText(requireArguments().getString(EXTRA_URL, ""))
    }

    override fun setLinkDraft(draft: NewLinkResponse) =
        (activity as AddlinkActivity).openDuplicatesActivity(draft)

    override fun showDuplicatesLoading(visibility: Boolean) {
        binding.linkIcon.isVisible = !visibility
        binding.iconTitle.isVisible = !visibility
        binding.description.isVisible = !visibility
        binding.linkUrlLayout.isVisible = !visibility
        binding.addLink.isVisible = !visibility
        binding.progressBar.isVisible = visibility
        binding.progressBarTitle.isVisible = visibility
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }
}
