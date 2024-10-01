package com.example.restau.domain.usecases

class UserUseCases(
    val getUserObject: GetUserObject,
    val sendLike: SendLike,
    val saveTags: SaveTags,
    val setUserInfo: SetUserInfo
)