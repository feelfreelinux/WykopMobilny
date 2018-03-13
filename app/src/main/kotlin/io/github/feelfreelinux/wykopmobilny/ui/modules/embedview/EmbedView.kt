package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Coub
import java.net.URL

interface EmbedView : BaseView {
    fun playUrl(url : URL)
    fun playCoub(coub: Coub)
    fun exitAndOpenYoutubeActivity()
}