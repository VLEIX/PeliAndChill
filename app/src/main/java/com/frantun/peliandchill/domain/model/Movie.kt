package com.frantun.peliandchill.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date
import kotlinx.parcelize.Parcelize

@Entity(tableName = "movies")
@Parcelize
data class Movie(
    @PrimaryKey
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    var type: String?,
    var timeStamp: Date?
) : Parcelable
