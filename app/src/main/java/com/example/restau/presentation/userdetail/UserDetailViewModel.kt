package com.example.restau.presentation.userdetail

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
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

    private var startTime by mutableStateOf(Date())

    init {
        getUser()
    }

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
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userUseCases.getUserObject()
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

}