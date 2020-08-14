package com.yinkaolu.githubapp.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GithubRepos: ArrayList<GithubRepo>()

/**
 * Class representing Github Repository Data from Github API
 */
class GithubRepo(
    val name: String,
    val description: String? = "",
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("stargazers_count")
    val stargazers: Int,
    val forks: Int
) : Parcelable {
    /**
     * The Date object converted from the data string returned from Github API
     */
    val updatedDate: Date?
        get() {
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA)
            return format.parse(updatedAt)
        }

    // ====== Parcelable Implementation
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )
    
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(updatedAt)
        parcel.writeInt(stargazers)
        parcel.writeInt(forks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GithubRepo> {
        override fun createFromParcel(parcel: Parcel): GithubRepo {
            return GithubRepo(parcel)
        }

        override fun newArray(size: Int): Array<GithubRepo?> {
            return arrayOfNulls(size)
        }
    }
}