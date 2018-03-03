package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface EmbedView : BaseView {
    fun playUrl(url : String)
    fun exitAndOpenYoutubeActivity()
}