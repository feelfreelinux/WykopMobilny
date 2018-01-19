package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize data class Link(
        val id : Int,
        val title : String,
        val description : String,
        val tags : String,
        val sourceUrl : String,
        var voteCount : Int,
        var buryCount : Int,
        var comments : List<LinkComment>,
        val commentsCount : Int,
        val relatedCount : Int,
        val author : Author?,
        val date : String,
        val preview : String?,
        val plus18 : Boolean,
        val canVote : Boolean,
        val isHot : Boolean,
        val status : String,
        var userVote : String?,
        var userFavorite : Boolean,
        val app : String?
) : Parcelable