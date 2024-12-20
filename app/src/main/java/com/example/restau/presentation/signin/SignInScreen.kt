package com.example.restau.presentation.signin

import android.util.Patterns
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.restau.R
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins


@Composable
fun SignInScreen(
    navController: NavHostController,
    authCheck: suspend () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.restau_1_),
            contentDescription = "RestaU logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 65.dp)
                .size(100.dp)
        )

        SignInForm(navController = navController, authCheck = authCheck)

    }
}

@Composable
fun GoogleButton() {
    Button(
        onClick = { /* Handle onClick */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Gray,
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(56.dp)
            .wrapContentWidth()
            .background(Color.White)
            .shadow(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Sign-In Icon",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Sign in with Google",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SignUpText(isLoading: Boolean, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 50.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Don't have an account?",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Sign Up",
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Right,
            modifier = Modifier.clickable {
                if (!isLoading) {
                    navController.navigate(Route.SignUpScreen.route)
                }
            }
        )
    }
}

@Composable
fun SignInForm(
    signInVM: SignInViewModel = hiltViewModel(),
    navController: NavHostController,
    authCheck: suspend () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login",
            color = Color(0xFF2F2F2F),
            fontFamily = Poppins,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 170.dp, 0.dp, 20.dp)
        )

        EmailTextField(signInVM.state.isLoading, signInVM.state.email) { signInVM.onEvent(SignInEvent.EmailChange(email = it)) }

        PasswordTextField(signInVM.state.isLoading, signInVM.state.password, { signInVM.onEvent(SignInEvent.PasswordChange(password = it)) }, signInVM)

        if(signInVM.state.errSignIn != ""){
            ErrorText(signInVM.state.errSignIn)
        }

        Button(
            onClick = {
                signInVM.onEvent(SignInEvent.SignIn(signInVM.state.email, signInVM.state.password, { signedSuccess(navController);  signInVM.onEvent(SignInEvent.FeatureInteraction("auth_signin_feature"))}, {  authCheck() }) )
            },
            enabled = !signInVM.state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Sign In",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
        OtherSignUp()

        SignUpText(signInVM.state.isLoading, navController)

    }

    if (signInVM.state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0x88ffffff)
                )
        )
    }
}

@Composable
fun OtherSignUp() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 30.dp, 5.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color(0xFF2F2F2F)
        )
        Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 10.dp),
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF2F2F2F)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color(0xFF2F2F2F)
        )
    }

    GoogleButton()
}

@Composable
fun ErrorText(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
    ) {
        Icon(
            Icons.Outlined.ErrorOutline,
            contentDescription = "Error Icon",
            tint = Color(0xFFB00020),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = message,
            color = Color(0xFFB00020),
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun EmailTextField(isLoading: Boolean, email: String, onEmailChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = Poppins
        ),
        enabled = !isLoading,
        isError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches(),
        singleLine = true,
        onValueChange = {
            if (it.length <= 100 && !it.contains("'") && !it.contains('"') && !it.contains("=")) onEmailChange(it)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2F2F2F),
            unfocusedBorderColor = Color(0xFF2F2F2F),
            cursorColor = Color(0xFF2F2F2F),
            focusedLabelColor = Color(0xFF2F2F2F),
            unfocusedLabelColor = Color(0xFF2F2F2F)
        ),
        leadingIcon = {
            Icon(
                Icons.Outlined.Email,
                contentDescription = "email",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text(text = "Email", fontSize = 16.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .wrapContentHeight()
    )
}

@Composable
fun PasswordTextField(isLoading: Boolean, password: String, onPasswordChange: (String) -> Unit, signInVM: SignInViewModel) {
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        value = password,
        onValueChange = {
            if (it.length <= 100 && !it.contains("'") && !it.contains('"') && !it.contains("=")) onPasswordChange(it)
        },
        enabled = !isLoading,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2F2F2F),
            unfocusedBorderColor = Color(0xFF2F2F2F),
            cursorColor = Color(0xFF2F2F2F),
            focusedLabelColor = Color(0xFF2F2F2F),
            unfocusedLabelColor = Color(0xFF2F2F2F)
        ),
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                contentDescription = "password",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text("Password") },
        visualTransformation = if (signInVM.state.showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                if (signInVM.state.showPassword) {
                    Icons.Outlined.Visibility
                } else {
                    Icons.Outlined.VisibilityOff
                },
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Toggle password visibility",
                modifier = Modifier.clickable { signInVM.onEvent(SignInEvent.ShowPasswordChange(!signInVM.state.showPassword)) })
        }
    )
}

fun signedSuccess(navController: NavController) {
    navController.navigate(Route.HomeScreen.route) {
        popUpTo(navController.graph.startDestinationId){
            inclusive = true
        }
    }
}

