package com.example.humblr.data.profile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsListingDto(
    val kind: String,
    val data: FriendsDto
)