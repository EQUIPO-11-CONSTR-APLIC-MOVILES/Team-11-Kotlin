package com.example.restau.presentation.navigator

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.authUseCases.AuthUseCases
import com.example.restau.domain.usecases.locationUseCases.LocationUseCases
import com.example.restau.domain.usecases.pathUseCases.NavPathsUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val locationUseCases: LocationUseCases,
    private val navPathsUseCases: NavPathsUseCases
) : ViewModel() {

    var selected by mutableIntStateOf(0)
        private set

    var showSplash by mutableStateOf(true)
        private set

    var currentUser = MutableStateFlow<FirebaseUser?>(null)

    val alertMessage = MutableStateFlow<String?>(null)

    private var pathID = MutableStateFlow<String?>(null)

    private var path = mutableListOf<Int>()

    fun onEvent(event: NavigatorEvent) {
        when (event) {
            is NavigatorEvent.SelectedChange -> {
                viewModelScope.launch {
                    if (selected != event.selected) path.add(event.selected)
                    val pathIDValue = pathID.value
                    if (pathIDValue != null) {
                        navPathsUseCases.updatePath(path, pathIDValue)
                        path.clear()
                    }
                }
                selected = event.selected

            }

            is NavigatorEvent.OnStart -> {
                viewModelScope.launch {
                    authCheck()
                    nearRestaurants(event.context)
                    navPathsUseCases.createPath()?.let {
                        pathID.value = it
                    }
                }
            }
        }
    }

    private suspend fun getCurrentUser(): FirebaseUser? {
        return authUseCases.getCurrentUser()
    }

    suspend fun authCheck() {
        currentUser.value = getCurrentUser()
        Log.d("NavigatorViewModel", "authCheck: ${currentUser.value}")
        showSplash = false
    }


    private fun nearRestaurants(context: Context) {
        if (hasLocationPermission(context)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val id = currentUser.first { it != null }?.uid
                    val location = locationUseCases.getLocation.invoke().first()
                    val lat = location?.latitude.toString()
                    val lon = location?.longitude.toString()

                    val apiURL = "http://34.134.5.98:8000/nearbyxcuisine/"

                    val url = URL("$apiURL?&userID=$id&lat=$lat&lon=$lon")
                    val connection = url.openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"

                    val responseCode = connection.responseCode

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        var response = inputStream.bufferedReader().use { it.readText() }.toString()
                        response = response.replace("\"", "")
                        if (response != "null") {

                            alertMessage.value =
                                if (response.firstOrNull()?.lowercaseChar() in listOf(
                                        'a',
                                        'e',
                                        'i',
                                        'o',
                                        'u'
                                    )
                                ) "There's an $response restaurant near you" else "There's a $response restaurant near you"
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error during near restaurants", e)
                }
            }
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
}



