package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class PatronBadge(
        @JsonProperty("color") val hexColor: String,
        @JsonProperty("text") val text: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hexColor)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PatronBadge> {
        override fun createFromParcel(parcel: Parcel): PatronBadge {
            return PatronBadge(parcel)
        }

        override fun newArray(size: Int): Array<PatronBadge?> {
            return arrayOfNulls(size)
        }
    }
}