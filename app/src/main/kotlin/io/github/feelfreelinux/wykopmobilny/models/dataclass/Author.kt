package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize data class Author(val nick : String,
                             val avatarUrl : String,
                             val group: Int,
                             val sex : String) : Parcelable