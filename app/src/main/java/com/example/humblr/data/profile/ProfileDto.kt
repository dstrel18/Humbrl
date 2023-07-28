package com.example.humblr.data.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProfileDto(
    var name: String?,
    var id: String?,
    @Json(name = "icon_img")
    var icon_img: String?,
    @Json(name = "subreddit")
    var more_infos: UserDataSubDto?,
    var total_karma: Int?,
    var is_friend: Boolean?,
    var snoovatar_img: String?,
    var comment_karma : Int?,

) {
    @JsonClass(generateAdapter = true)
    class UserDataSubDto(
        var subscribers: Int = 0,
        @Json(name = "title")
        var title: String?,
    )
}
