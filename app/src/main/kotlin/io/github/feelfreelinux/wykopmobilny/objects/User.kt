package io.github.feelfreelinux.wykopmobilny.objects

import java.io.Serializable

data class User(val avatarUrl : String, val role : Int, val nick : String, val gender : String) : Serializable