package io.github.feelfreelinux.wykopmobilny.objects

import java.io.Serializable

 class WykopApiData(
        val login: String,
        val accountKey: String,
        val secret: String,
        val appkey: String,
        val userToken : String?) : Serializable