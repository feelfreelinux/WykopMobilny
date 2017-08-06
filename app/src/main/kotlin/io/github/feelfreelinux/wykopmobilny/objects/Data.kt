package io.github.feelfreelinux.wykopmobilny.objects

import java.io.Serializable
import java.util.*

data class Embed(
        val type : String,
        val preview : String,
        val url : String,
        val plus18 : Boolean,
        val source : String) : Serializable