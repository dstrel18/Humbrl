package com.example.humblr.data.profile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsDto(
    val children: List<Children>
) {
    @JsonClass(generateAdapter = true)
    data class Children(
        val id: String, val name: String
    )
}