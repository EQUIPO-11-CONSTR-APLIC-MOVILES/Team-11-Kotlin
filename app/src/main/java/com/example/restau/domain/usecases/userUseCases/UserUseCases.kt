package com.example.restau.domain.usecases.userUseCases

class UserUseCases(
    val getUserObject: GetUserObject,
    val sendLike: SendLike,
    val saveTags: SaveTags,
    val setUserInfo: SetUserInfo,
    val updateUserInfo: UpdateUserInfo,
    val updateReviewsAuthorInfo: UpdateReviewsAuthorInfo,
    val uploadUserImage: UploadUserImage
)