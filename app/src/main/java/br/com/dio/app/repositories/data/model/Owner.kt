package br.com.dio.app.repositories.data.model

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import java.util.*

data class Owner (
    val login: String,
    @SerializedName("avatar_url")
    val avatarURL: String,
    val name: String,
    val company: String,
    val location: String,
    val email: String,
    val hireable: Boolean,
    val bio: String,
    @SerializedName("twitter_username")
    val twitterUsername: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    val followers:Int,
    val following:Int,
    val createAt:Date
)