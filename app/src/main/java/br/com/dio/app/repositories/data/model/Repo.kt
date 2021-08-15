package br.com.dio.app.repositories.data.model

import com.google.gson.annotations.SerializedName

data class Repo (
    val id: Long,
    val name: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    val language: String,
    @SerializedName("html_url")
    val htmlURL: String,
    val description: String
)