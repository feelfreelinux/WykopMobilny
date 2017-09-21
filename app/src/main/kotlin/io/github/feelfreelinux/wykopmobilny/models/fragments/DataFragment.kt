package io.github.feelfreelinux.wykopmobilny.models.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun <T : Any> FragmentManager.getDataFragmentInstance(tag : String): DataFragment<T> {
    var retainedFragment = findFragmentByTag(tag)
    if (retainedFragment == null) {
        retainedFragment = DataFragment<T>()
        beginTransaction().add(retainedFragment, tag).commit()
    }
    return retainedFragment as DataFragment<T>
}

fun FragmentManager.removeDataFragment(fragment : Fragment) {
    beginTransaction().remove(fragment).commit()
}
class DataFragment<T : Any> : Fragment() {
    var data : T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}