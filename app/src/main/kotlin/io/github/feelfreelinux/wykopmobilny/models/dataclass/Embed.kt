package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize data class Embed(val type : String,
                            val preview : String,
                            val url : String,
                            val plus18 : Boolean,
                            val source : String?,
                            val isAnimated : Boolean,
                            val size : String,
                            var isResize : Boolean = false) : Parcelable