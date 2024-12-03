package com.example.restau.presentation.userdetail

import android.net.Uri
import com.example.restau.domain.model.User


sealed class UserDetailEvent() {
    data class UpdateUserEvent(val user: User) : UserDetailEvent()
    data class UpdateReviewAuthorEvent(val documentId: String, val authorName: String, val authorPFP: String) : UserDetailEvent()
    data class UploadProfileImage(val uri: Uri) : UserDetailEvent()
    data class ChangeUserNameEvent(val userName: String): UserDetailEvent()
    data class ShowUserUpdateDialog(val show: Boolean): UserDetailEvent()
    data class ShowUserUpdateErrorDialog(val show: Boolean): UserDetailEvent()
    data class ShowNoConnectionDialog(val show: Boolean): UserDetailEvent()
    data class ShowLogOutDialog(val show: Boolean): UserDetailEvent()
    data object LogOutEvent: UserDetailEvent()
    data object ScreenLaunched: UserDetailEvent()
}