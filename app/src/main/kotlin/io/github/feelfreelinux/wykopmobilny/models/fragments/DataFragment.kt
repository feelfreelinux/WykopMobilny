package io.github.feelfreelinux.wykopmobilny.models.fragments

import android.os.Bundle

fun <T : Any> androidx.fragment.app.FragmentManager.getDataFragmentInstance(tag: String): DataFragment<T> {
    var retainedFragment = findFragmentByTag(tag)
    if (retainedFragment == null) {
        retainedFragment = DataFragment<T>()
        beginTransaction().add(retainedFragment, tag).commit()
    }
    return retainedFragment as DataFragment<T>
}

fun androidx.fragment.app.FragmentManager.removeDataFragment(fragment: androidx.fragment.app.Fragment) {
    beginTransaction().remove(fragment).commit()
}

class DataFragment<T : Any> : androidx.fragment.app.Fragment() {
    var data: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}