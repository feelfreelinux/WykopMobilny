package io.github.feelfreelinux.wykopmobilny.objects

import java.io.Serializable

data class WykopApiData(val user: User?, val accountKey: String, val secret: String, val appkey: String, val userToken : String?) : Serializable