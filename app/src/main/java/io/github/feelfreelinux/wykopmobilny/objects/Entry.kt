package io.github.feelfreelinux.wykopmobilny.objects

import java.util.*

class Entry(val id : Int, val author : User, val votes_count : Int, val comments_count : Int, val body : String, val date : Date, val embed: Embed)