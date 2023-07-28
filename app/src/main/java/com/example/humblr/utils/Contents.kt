package com.example.humblr.utils

import com.example.humblr.BuildConfig


const val TAG_T = "tag_t"

const val CLIENT_ID = "udnhZw5jdD8am_F4sE1cqg"
const val CLIENT_SECRET = ""
const val RESPONSE_TYPE = "code"
const val STATE = "my_state"
const val REDIRECT_URI = "http://humbrlmi/streltsov"
const val DURATION = "permanent"
const val SCOPE =
    "identity edit flair history modconfig" + " modflair modlog modposts modwiki " + "mysubreddits privatemessages read report " + "save submit subscribe vote wikiedit wikiread"

const val CALL =
    "https://www.reddit.com/api/v1/authorize.compact" + "?client_id=" + CLIENT_ID + "&response_type=" + RESPONSE_TYPE + "&state=" + STATE + "&redirect_uri=" + REDIRECT_URI + "&duration=" + DURATION + "&scope=" + SCOPE
const val BASE_URL = "https://oauth.reddit.com/"


const val CODE = "code"
const val POPULAR = "popular"
const val NEW = "new"
const val PAGE_SIZE_SUBREDDITS = 10
const val FINISHED = "finished"
const val ONBOARDING = "onBoarding"
const val TOKEN_SAVE = "TokenSave"
const val TOKEN = "Token"
const val LINKS = "links"
const val COMMENTS = "comments"

