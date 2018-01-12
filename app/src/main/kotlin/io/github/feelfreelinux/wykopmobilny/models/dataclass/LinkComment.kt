package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize data class LinkComment(
        val id : Int,
        val author : Author,
        val date : String,
        val body : String?,
        val blocked : Boolean,
        val favorite : Boolean,
        var voteCount : Int,
        var voteCountPlus : Int,
        var voteCountMinus : Int,
        var userVote : Int,
        val parentId : Int,
        val canVote : Boolean,
        val linkId : Int,
        val embed : Embed?,
        val app : String?
) : Parcelable