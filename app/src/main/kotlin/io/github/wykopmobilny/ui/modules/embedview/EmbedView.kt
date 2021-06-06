package io.github.wykopmobilny.ui.modules.embedview

import io.github.wykopmobilny.api.responses.Coub
import io.github.wykopmobilny.base.BaseView
import java.net.URL

interface EmbedView : BaseView {
    fun playUrl(url: URL)
    fun playCoub(coub: Coub)
    fun exitAndOpenYoutubeActivity()
    fun checkEmbedSettings()
}
