package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.addlink_fragment.*
import javax.inject.Inject

class AddlinkUrlInputFragment : BaseFragment(), AddLinkUrlInputFragmentView {
    @Inject lateinit var presenter : AddLinkUrlInputPresenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addlink_fragment, container, false)
    }

    companion object {
        val EXTRA_URL = "ADDLINK_URL"
        fun newInstance(url : String = "") : AddlinkUrlInputFragment {
            val fragment = AddlinkUrlInputFragment()
            val data = Bundle()
            data.putString(EXTRA_URL, url)
            fragment.arguments = data
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        add_link.setOnClickListener {
            if (linkUrl.text.isNotEmpty() && URLUtil.isValidUrl(linkUrl.text.toString())) {
                presenter.createDraft(linkUrl.text.toString())
            } else {
                link_url_layout.error = getString(R.string.invalid_url)
            }
        }
        linkUrl.setText(arguments!!.getString(EXTRA_URL, ""))
    }

    override fun setLinkDraft(draft: NewLinkResponse) {
        (activity as AddlinkActivity).openDuplicatesActivity(draft)
    }

    override fun showDuplicatesLoading(visibility : Boolean) {
        linkIcon.isVisible = !visibility
        iconTitle.isVisible = !visibility
        description.isVisible = !visibility
        link_url_layout.isVisible = !visibility
        add_link.isVisible = !visibility
        progressBar.isVisible = visibility
        progressBarTitle.isVisible = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}