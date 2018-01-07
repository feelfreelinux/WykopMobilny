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
        val voteCount : Int,
        val voteCountPlus : Int,
        val voteCountMinus : Int,
        val userVote : Int,
        val parentId : Int,
        val canVote : Boolean,
        val linkId : Int,
        val embed : Embed?,
        val app : String?
) : Parcelable