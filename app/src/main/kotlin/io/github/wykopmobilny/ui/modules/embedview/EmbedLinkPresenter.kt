package io.github.wykopmobilny.ui.modules.embedview

import io.github.wykopmobilny.api.embed.ExternalApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.reactivex.Single
import java.net.URI

class EmbedLinkPresenter(
    val schedulers: Schedulers,
    private val embedApi: ExternalApi,
) : BasePresenter<EmbedView>() {

    companion object {
        const val GFYCAT_MATCHER = "gfycat.com"
        const val COUB_MATCHER = "coub.com"
        const val STREAMABLE_MATCHER = "streamable.com"
        const val YOUTUBE_MATCHER = "youtube.com"
        const val MOBILE_YOUTUBE_MATCHER = "m.youtube.com"
        const val SIMPLE_YOUTUBE_MATCHER = "youtu.be"
    }

    lateinit var linkDomain: String
    lateinit var mp4Url: String

    fun playUrl(url: String) {
        linkDomain = if (!url.contains(GFYCAT_MATCHER)) {
            url.getDomainName()
        } else {
            GFYCAT_MATCHER
        }
        when (linkDomain) {
            GFYCAT_MATCHER -> {
                val id = url.formatGfycat()
                embedApi.getGfycat(id)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe(
                        {
                            mp4Url = it.gfyItem.mp4Url
                            view?.playUrl(it.gfyItem.webmUrl)
                        },
                        { view?.showErrorDialog(it) },
                    )
            }

            COUB_MATCHER -> {
                val id = url.removeSuffix("/").substringAfterLast("/view/")
                embedApi.getCoub(id)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe(
                        {
                            mp4Url = it.fileVersions.mobile.mp4!!
                            view?.playUrl(mp4Url)
                        },
                        { view?.showErrorDialog(it) },
                    )
            }

            STREAMABLE_MATCHER -> {
                val id = url.removeSuffix("/").substringAfterLast("/")
                embedApi.getStreamableUrl(id)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe(
                        {
                            mp4Url = it.toString()
                            view?.playUrl(mp4Url)
                        },
                        { view?.showErrorDialog(it) },
                    )
            }

            MOBILE_YOUTUBE_MATCHER, SIMPLE_YOUTUBE_MATCHER, YOUTUBE_MATCHER -> view?.exitAndOpenYoutubeActivity()
        }
    }

    private fun String.formatGfycat(): String {
        return this
            .replace(".gif", "")
            .replace(".mp4", "")
            .replace(".webm", "")
            .replace("-size_restricted", "")
            .removeSuffix("/").substringAfterLast("/")
    }

    private fun String.getDomainName(): String {
        val uri = URI(this)
        val domain = uri.host
        return if (domain.startsWith("www.")) domain.substring(4) else domain
    }
}
