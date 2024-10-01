package com.example.restau.presentation.signup

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun SignUpScreen(
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

        SignUpForm(navController = navController, authCheck = authCheck)

        SignInText(navController)
    }
}

@Composable
fun SignUpForm(
    signUpVM: SignUpViewModel = hiltViewModel(),
    navController: NavHostController,
    authCheck: suspend () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Create Account",
            color = Color(0xFF2F2F2F),
            fontFamily = Poppins,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 170.dp, 0.dp, 20.dp)
        )

        NameTextField(signUpVM.state.name) { signUpVM.onEvent(SignUpEvent.NameChange(name = it)) }

        EmailTextField(signUpVM.state.email) { signUpVM.onEvent(SignUpEvent.EmailChange(email = it)) }

        PasswordTextField(signUpVM.state.password, { signUpVM.onEvent(SignUpEvent.PasswordChange(password = it)) }, signUpVM)

        if(signUpVM.state.errSignUp != ""){
            ErrorText(signUpVM.state.errSignUp)
        }

        Button(
            onClick = {
                signUpVM.onEvent(SignUpEvent.SignUp(signUpVM.state.name, signUpVM.state.email, signUpVM.state.password, { signedSuccess(navController) },  { authCheck() }))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Sign Up",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SignInText(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(40.dp, 0.dp, 40.dp, 60.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Already have an account?",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Sign In",
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Right,
            modifier = Modifier.clickable { navController.navigate(Route.SignInScreen.route)  }
        )
    }
}

@Composable
fun NameTextField(name: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = Poppins
        ),
        singleLine = true,
        onValueChange = {
            if (it.length <= 100) onNameChange(it)
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
                Icons.Outlined.AccountCircle,
                contentDescription = "name",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text(text = "Name", fontSize = 16.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .wrapContentHeight()
    )
}

@Composable
fun EmailTextField(email: String, onEmailChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = Poppins
        ),
        isError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches(),
        singleLine = true,
        onValueChange = {
            if (it.length <= 100) onEmailChange(it)
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
fun PasswordTextField(password: String, onPasswordChange: (String) -> Unit, signUpVM: SignUpViewModel) {
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        value = password,
        onValueChange = {
            if (it.length <= 100) onPasswordChange(it)
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
                Icons.Outlined.Lock,
                contentDescription = "password",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text("Password") },
        visualTransformation = if (signUpVM.state.showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                if (signUpVM.state.showPassword) {
                    Icons.Outlined.Visibility
                } else {
                    Icons.Outlined.VisibilityOff
                },
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Toggle password visibility",
                modifier = Modifier.clickable { signUpVM.onEvent(SignUpEvent.ShowPasswordChange(showPassword = !signUpVM.state.showPassword))})
        }
    )
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

fun signedSuccess(navController: NavController) {
    navController.navigate(Route.HomeScreen.route) {
        popUpTo(navController.graph.startDestinationId){
            inclusive = true
        }
    }
}