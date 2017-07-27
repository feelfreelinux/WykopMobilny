package io.github.feelfreelinux.wykopmobilny.projectors

import android.support.v4.app.Fragment
import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.MikroblogHotList
import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.utils.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.card_wpis.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

class NavigationActions(val context : NavigationActivity) : NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_mikroblog -> 
                openFragment(MikroblogHotList.newInstance(context.wam.getData()))
        }
        item.isChecked = true
        context.drawer_layout.closeDrawers()
        return true
    }

    fun setupNavigation() {
        context.drawer_layout.addDrawerListener(context.actionBarToggle)
        context.navigationView.setNavigationItemSelectedListener(this)
        setupHeader()
        openInitialFragment()
    }

    fun setupHeader() {
        val navHeader = context.navigationView.getHeaderView(0)
        val user = context.wam.user!!
        Picasso.with(context).load(user.avatarUrl).into(navHeader.img_profile)
        navHeader.user_login.text = user.nick
        navHeader.user_login.setTextColor(getGroupColor(user.role))
    }

    fun openFragment(fragment : Fragment) {
        context.supportFragmentManager.beginTransaction().replace(R.id.contentView, fragment).addToBackStack(fragment.tag).commit()
    }

    fun openInitialFragment() {
        context.supportFragmentManager.beginTransaction().replace(R.id.contentView,
                MikroblogHotList.newInstance(context.wam.getData())).commit()
    }
}