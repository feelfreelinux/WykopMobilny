package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import java.net.URL

interface EmbedView : BaseView {
    fun playUrl(url : URL)
    fun exitAndOpenYoutubeActivity()
}