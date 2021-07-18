package io.github.wykopmobilny.utils.shortcuts

import android.content.Intent
import androidx.fragment.app.Fragment
import io.github.wykopmobilny.ui.modules.links.hits.HitsFragment
import io.github.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.wykopmobilny.ui.modules.mywykop.MyWykopFragment
import io.github.wykopmobilny.ui.modules.search.SearchFragment

class ShortcutsDispatcher {

    fun dispatchIntent(
        intent: Intent,
        startFragment: (fragment: Fragment) -> (Unit),
        startActivity: () -> (Unit),
        isUserAuthorized: Boolean,
    ) {
        when (intent.action) {
            "hot" -> startFragment.invoke(HotFragment.newInstance())
            "search" -> startFragment.invoke(SearchFragment.newInstance())
            "mywykop" -> if (isUserAuthorized) {
                startFragment.invoke(MyWykopFragment.newInstance())
            } else {
                startActivity.invoke()
            }
            "hits" -> startFragment.invoke(HitsFragment.newInstance())
        }
    }
}
