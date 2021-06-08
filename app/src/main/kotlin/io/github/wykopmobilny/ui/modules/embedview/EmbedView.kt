package io.github.wykopmobilny.ui.modules.embedview

import io.github.wykopmobilny.base.BaseView

interface EmbedView : BaseView {
    fun playUrl(url: String)
    fun exitAndOpenYoutubeActivity()
    fun checkEmbedSettings()
}
