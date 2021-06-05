package io.github.feelfreelinux.wykopmobilny.utils.shortcuts

import android.content.Intent
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits.HitsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SearchFragment

class ShortcutsDispatcher {

    fun dispatchIntent(
        intent: Intent,
        startFragment: (fragment: Fragment) -> (Unit),
        startActivity: () -> (Unit),
        isUserAuthorized: Boolean
    ) {
        when (intent.action) {
            HotFragment::class.java.canonicalName -> startFragment.invoke(HotFragment.newInstance())
            SearchFragment::class.java.canonicalName -> startFragment.invoke(SearchFragment.newInstance())
            MyWykopFragment::class.java.canonicalName -> if (isUserAuthorized) {
                startFragment.invoke(MyWykopFragment.newInstance())
            } else {
                startActivity.invoke()
            }
            HitsFragment::class.java.canonicalName -> startFragment.invoke(HitsFragment.newInstance())
        }
    }
}
