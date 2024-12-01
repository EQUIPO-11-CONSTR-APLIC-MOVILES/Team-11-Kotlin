package com.example.restau.presentation.userdetail


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restau.domain.model.User
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.SoftRed

@Composable
fun UserDetailScreen(
    navController: NavController,
    authCheck: suspend () -> Unit,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {

    val state = userDetailViewModel.state
    val user = userDetailViewModel.currentUser
    val userName = userDetailViewModel.userName
    val ppicture = "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Fbee.png?alt=media&token=7a934c3e-a0bd-4557-b64d-200aec680bf2"
    val showUserUpdateDialog = userDetailViewModel.showUserUpdateDialog
    val showErrorUpdateDialog = userDetailViewModel.showErrorUpdateDialog
    val showLogOutDialog = userDetailViewModel.showLogOutDialog
    val showNoConnectionDialog = userDetailViewModel.showNoConnectionDialog
    val selectedImageUrl = userDetailViewModel.selectedImageUrl

    val isConnected by userDetailViewModel.isConnected.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                userDetailViewModel.onEvent(UserDetailEvent.UploadProfileImage(it))
            }
        }
    )

    LaunchedEffect(isConnected) {
        userDetailViewModel.onEvent(UserDetailEvent.ScreenLaunched)
        userDetailViewModel.onEvent(UserDetailEvent.ChangeUserNameEvent(user.name))
    }

    LaunchedEffect(Unit) {
        userDetailViewModel.onEvent(UserDetailEvent.ScreenLaunched)
    }

    LaunchedEffect(user) {
        userDetailViewModel.onEvent(UserDetailEvent.ChangeUserNameEvent(user.name))
    }

    LaunchedEffect(state.isUserUpdatedSuccessfully, state.isReviewAuthorUpdatedSuccessfully) {
        if (state.isUserUpdatedSuccessfully && state.isReviewAuthorUpdatedSuccessfully) {
            navController.popBackStack()
        } else {
            // If fails
        }
    }

    LaunchedEffect(state.isLogOut) {
        if (state.isLogOut) {
            authCheck()
            navController.navigate(Route.SignInScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // Manejo fallo de logout
        }
    }


    LaunchedEffect(state.isReviewAuthorUpdatedSuccessfully) {
        if (state.isReviewAuthorUpdatedSuccessfully) {
            userDetailViewModel.isUpdating = false
            userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateDialog(false))
        }
    }

    if (userDetailViewModel.isLoading) {
        LoadingCircle()
    }

    else if (userDetailViewModel.showFallback){
        NoConnection()
    }

    else {

        if (showUserUpdateDialog) {
            AlertDialog(
                onDismissRequest = {
                    if (!userDetailViewModel.isUpdating) {
                        userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateDialog(false))
                    }
                },
                confirmButton = {
                    if (!userDetailViewModel.isUpdating) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(
                                onClick = {
                                    userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateDialog(false))
                                },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(width = 120.dp, height = 50.dp)
                            ) {
                                Text(
                                    "Cancel",
                                    fontSize = 16.sp,
                                    color = SoftRed,
                                    fontFamily = Poppins
                                )
                            }

                            TextButton(
                                onClick = {
                                    if (isConnected){

                                        val upUser = User(
                                            documentId = user.documentId,
                                            name = userName,
                                            profilePic = selectedImageUrl ?: ppicture
                                        )
                                        userDetailViewModel.onEvent(UserDetailEvent.UpdateUserEvent(upUser))
                                        userDetailViewModel.onEvent(UserDetailEvent.UpdateReviewAuthorEvent(user.documentId, userName, selectedImageUrl ?: ppicture))
                                        userDetailViewModel.isUpdating = true
                                    } else{
                                        userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateDialog(false))
                                        userDetailViewModel.onEvent(UserDetailEvent.ShowNoConnectionDialog(true))
                                    }
                                },
                                modifier = Modifier
                                    .size(width = 120.dp, height = 50.dp)
                                    .background(SoftRed, shape = MaterialTheme.shapes.medium)
                            ) {
                                Text(
                                    "Yes",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontFamily = Poppins
                                )
                            }
                        }
                    }
                },
                title = {
                    if (!userDetailViewModel.isUpdating) {
                        Text(
                            text = "Are you sure you want to update your user data?",
                            fontFamily = Poppins
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (userDetailViewModel.isUpdating) {
                            Text(
                                text = "Please wait a moment...",
                                fontFamily = Poppins,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            LoadingCircle(
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            Text(
                                text = "This action will update your user data and review author details.",
                                fontSize = 16.sp,
                                fontFamily = Poppins
                            )
                        }
                    }
                }
                ,
                shape = MaterialTheme.shapes.medium
            )
        }


        else if (showErrorUpdateDialog) {
            AlertDialog(
                onDismissRequest = { userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateErrorDialog(false))},
                confirmButton = {
                    TextButton(onClick = { userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateErrorDialog(false))
                    },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width = 120.dp, height = 50.dp)
                            .background(SoftRed, shape = MaterialTheme.shapes.medium)
                    ) {
                        Text(
                            "OK",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                },
                title = { TitleErrorText(userName = userName) },
                text = { ErrorText(userName = userName) },
                shape = MaterialTheme.shapes.medium
            )
        }

        else if (showLogOutDialog) {
            AlertDialog(
                onDismissRequest = {
                    if (!userDetailViewModel.isUpdating) {
                        userDetailViewModel.onEvent(UserDetailEvent.ShowLogOutDialog(false))
                    }
                },
                confirmButton = {
                    if (!userDetailViewModel.isUpdating) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(
                                onClick = {
                                    // Acción para cancelar el logout
                                    userDetailViewModel.onEvent(UserDetailEvent.ShowLogOutDialog(false))
                                },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(width = 120.dp, height = 50.dp)
                            ) {
                                Text(
                                    "Cancel",
                                    fontSize = 16.sp,
                                    color = SoftRed,
                                    fontFamily = Poppins
                                )
                            }

                            TextButton(
                                onClick = {
                                    userDetailViewModel.onEvent(UserDetailEvent.LogOutEvent)
                                    userDetailViewModel.onEvent(UserDetailEvent.ShowLogOutDialog(false))
                                },
                                modifier = Modifier
                                    .size(width = 120.dp, height = 50.dp)
                                    .background(SoftRed, shape = MaterialTheme.shapes.medium)
                            ) {
                                Text(
                                    "Yes",
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontFamily = Poppins
                                )
                            }
                        }
                    }
                },
                title = {
                    if (!userDetailViewModel.isUpdating) {
                        Text(
                            text = "Log Out",
                            fontFamily = Poppins,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out?",
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                shape = MaterialTheme.shapes.medium
            )
        }

        else if (showNoConnectionDialog){
            AlertDialog(
                onDismissRequest = { userDetailViewModel.onEvent(UserDetailEvent.ShowNoConnectionDialog(false))},
                confirmButton = {
                    TextButton(onClick = { userDetailViewModel.onEvent(UserDetailEvent.ShowNoConnectionDialog(false))
                    },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width = 120.dp, height = 50.dp)
                            .background(SoftRed, shape = MaterialTheme.shapes.medium)
                    ) {
                        Text(
                            "OK",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                },
                title = { Text(text = "No internet connection", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins) },
                text = { Text(text = "Please check your internet connection and try again", fontSize = 16.sp, fontFamily = Poppins) },
                shape = MaterialTheme.shapes.medium
            )
        }



        Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {}
                },
                hasBackButton = true,
                action = TopBarAction.NoAction,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 32.dp, end = 32.dp, top = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                ProfileHeader(
                    onSaveClick = {
                        if (userName.trim().isNotEmpty() && (userName != user.name || selectedImageUrl != user.profilePic)){
                            userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateDialog(true))
                        }
                        else{
                            userDetailViewModel.onEvent(UserDetailEvent.ShowUserUpdateErrorDialog(true))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                AsyncImage(
                    model = selectedImageUrl,
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(175.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(Color.LightGray)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Click the image to change it", fontFamily = Poppins, color = Color.Gray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = user.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                )

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = {
                        if (it.length <= 30) {
                            userDetailViewModel.onEvent(UserDetailEvent.ChangeUserNameEvent(it))
                        }
                    },
                    label = {
                        Text(
                            text = "Name",
                            fontFamily = Poppins
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoftRed,
                        focusedLabelColor = SoftRed,
                        cursorColor = SoftRed
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = "user icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
            }

            Button(
                onClick = {
                    userDetailViewModel.onEvent(UserDetailEvent.ShowLogOutDialog(true))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftRed
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .size(width = 350.dp, height = 50.dp)
            ) {
                Text(
                    text = "Log out",
                    fontFamily = Poppins,
                    fontSize = 16.sp
                )
            }
        }
    }
}
}



    //LaunchedEffect(state.isReviewAddedSuccessfully) {
    //    if (state.isReviewAddedSuccessfully) {
    //        navController.popBackStack()
    //    } else {
    //        // If fails
    //    }
    //}



@Composable
fun ProfileHeader(onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título "Your Profile"
        Text(
            text = "Your Profile",
            fontFamily = Poppins,
            fontSize = 20.sp
        )

        // Botón "Save"
        Text(
            text = "Save",
            color = SoftRed,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable {
                    onSaveClick()
                }
        )
    }
}

@Composable
fun ErrorText(userName: String) {
    if (userName.trim().isEmpty()) {
        Text("User name cannot be empty", fontSize = 16.sp, fontFamily = Poppins)
    } else {
        Text("You need to change at least one atribute", fontSize = 16.sp, fontFamily = Poppins)
    }
}

@Composable
fun TitleErrorText(userName: String) {
    if (userName.trim().isEmpty()) {
        Text(text = "Be Careful!", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
    }
    else{
        Text(text = "Check again!", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins)
    }
}