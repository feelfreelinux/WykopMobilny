package io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails

import io.github.wykopmobilny.api.addlink.AddLinkApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite

class AddLinkDetailsFragmentPresenter(
    val schedulers: Schedulers,
    private val addLinkApi: AddLinkApi
) : BasePresenter<AddLinkDetailsFragmentView>() {

    fun getImages(key: String) {
        view?.showImagesLoading(true)
        addLinkApi.getImages(key)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.showImages(it)
                    view?.showImagesLoading(false)
                },
                {
                    view?.showErrorDialog(it)
                    view?.showImagesLoading(false)
                }
            )
            .intoComposite(compositeObservable)
    }

    fun publishLink(
        key: String,
        title: String,
        sourceUrl: String,
        description: String,
        tags: String,
        plus18: Boolean,
        imageKey: String
    ) {
        view?.showLinkUploading(true)
        addLinkApi.publishLink(key, title, description, tags, imageKey, sourceUrl, plus18)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.openLinkScreen(it)
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }
}
