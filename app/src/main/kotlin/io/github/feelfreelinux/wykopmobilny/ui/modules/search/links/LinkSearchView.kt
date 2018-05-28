package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksFragmentView

interface LinkSearchView : LinksFragmentView {
    var showSearchEmptyView : Boolean
}