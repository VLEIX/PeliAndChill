package com.frantun.peliandchill.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date
import kotlinx.parcelize.Parcelize

@Entity(tableName = "series")
@Parcelize
data class Series(
    @PrimaryKey
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    val popularity: Double,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val overview: String,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    var type: String?,
    var timeStamp: Date?
) : Parcelable
