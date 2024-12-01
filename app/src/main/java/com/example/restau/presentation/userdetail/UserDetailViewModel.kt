package com.example.restau.presentation.userdetail

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.authUseCases.AuthUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val authUseCases: AuthUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val application: Application,
): AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(UserDetailState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    var userName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(true)
        private set

    var userUpdatedSuccesfully by mutableStateOf(false)
        private set

    var userReviewAuthorUpdatedSuccesfully by mutableStateOf(false)
        private set

    var showErrorUpdateDialog by mutableStateOf(false)
        private set

    var showUserUpdateDialog by mutableStateOf(false)
        private set

    var showLogOutDialog by mutableStateOf(false)
        private set

    var isUpdating by mutableStateOf(false)

    var selectedImageUrl by mutableStateOf<String?>(null)
        private set

    var showFallback by mutableStateOf(false)
        private set

    var showNoConnectionDialog by mutableStateOf(false)
        private set

    private var startTime by mutableStateOf(Date())

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("review_creation_screen", timer, currentUser.documentId)
        }
    }

    fun onEvent(event: UserDetailEvent) {
        when (event) {
            is UserDetailEvent.UpdateUserEvent -> {
                updateUser(event.user)
            }

            UserDetailEvent.ScreenLaunched -> {
                getUser()
            }

            is UserDetailEvent.ChangeUserNameEvent -> {
                onUserNameChange(event.userName)
            }

            is UserDetailEvent.ShowUserUpdateDialog -> {
                onShowUserUpdateDialogChange(event.show)
            }

            is UserDetailEvent.UpdateReviewAuthorEvent -> {
                updateReviewAuthor(event.documentId, event.authorName, event.authorPFP)
            }

            is UserDetailEvent.ShowUserUpdateErrorDialog -> {
                onShowErrorUpdateDialogChange(event.show)
            }

            is UserDetailEvent.UploadProfileImage -> {
                uploadProfileImage(event.uri)
            }

            UserDetailEvent.LogOutEvent -> {
                logOut()
            }

            is UserDetailEvent.ShowLogOutDialog -> {
                onShowLogOutDialogChange(event.show)
            }

            is UserDetailEvent.ShowNoConnectionDialog -> {
                onShowNoConnectionDialogChange(event.show)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userUseCases.getUserObject()
            val emptyUser = User()
            if (!isConnected.value && currentUser == emptyUser) {
                showFallback = true
            } else {
                showFallback = false
                selectedImageUrl = currentUser.profilePic
            }
            isLoading = false
        }
    }

    private fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userUpdatedSuccesfully = userUseCases.updateUserInfo(user)

            state = if (userUpdatedSuccesfully) {
                state.copy(
                    isUserUpdatedSuccessfully = true
                )
            } else {
                state.copy(
                    isUserUpdatedSuccessfully = false
                )
            }
        }
    }

    private fun onUserNameChange(newName: String) {
        userName = newName
    }

    private fun onShowUserUpdateDialogChange(show: Boolean) {
        showUserUpdateDialog = show
    }

    private fun onShowErrorUpdateDialogChange(show: Boolean) {
        showErrorUpdateDialog = show
    }

    private fun onShowLogOutDialogChange(show: Boolean) {
        showLogOutDialog = show
    }

    private fun onShowNoConnectionDialogChange(show: Boolean) {
        showNoConnectionDialog = show
    }


    private fun updateReviewAuthor(documentId: String, authorName: String, authorPFP: String){
        viewModelScope.launch(Dispatchers.IO) {

            userReviewAuthorUpdatedSuccesfully = userUseCases.updateReviewsAuthorInfo(documentId, authorName, authorPFP)

            state = if (userReviewAuthorUpdatedSuccesfully) {
                state.copy(
                    isReviewAuthorUpdatedSuccessfully = true
                )
            } else {
                state.copy(
                    isReviewAuthorUpdatedSuccessfully = false
                )
            }
        }
    }

    private fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val newProfileUrl = userUseCases.uploadUserImage(currentUser, uri)
            if (newProfileUrl != null) {
                selectedImageUrl = newProfileUrl
            } else {
                state = state.copy(isUserUpdatedSuccessfully = false)
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val logOut = authUseCases.executeLogOut()
            state = if (logOut) {
                state.copy(
                    isLogOut = true
                )
            } else {
                state.copy(
                    isLogOut = false
                )
            }
        }
    }



}