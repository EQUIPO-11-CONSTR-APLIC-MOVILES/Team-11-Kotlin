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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.restau.R
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SignInScreen() {
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

        SignInForm()

        SignUpText()
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
fun SignUpText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(40.dp, 0.dp, 40.dp, 60.dp),
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
            modifier = Modifier.clickable { /*TODO*/ }
        )
    }
}

@Composable
fun SignInForm(viewModel: EmailViewModel = viewModel()) {
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

        UserTextField()

        ValidatingInputTextField(
            email = viewModel.email,
            updateState = { input -> viewModel.updateEmail(input) },
            validatorHasErrors = viewModel.emailHasErrors
        )

        PasswordTextField()

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Sign in",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), // Make the text fill the button's width
                textAlign = TextAlign.Center, // Center-align the text
                fontSize = 18.sp // Set font size for the text
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp, 50.dp, 5.dp, 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color(0xFF2F2F2F))
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 10.dp),
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF2F2F2F)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color(0xFF2F2F2F))
        }

        GoogleButton()
    }
}


class EmailViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    val emailHasErrors by derivedStateOf {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    fun updateEmail(input: String) {
        email = input
    }
}

@Composable
fun ValidatingInputTextField(
    email: String,
    updateState: (String) -> Unit,
    validatorHasErrors: Boolean
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp),
        leadingIcon = {
            Icon(
                Icons.Outlined.Email,
                tint = Color(0xFF2F2F2F),
                contentDescription = "email"
            )
        },
        value = email,
        onValueChange = updateState,
        label = { Text("Email") },
        isError = validatorHasErrors,
        supportingText = {
            if (validatorHasErrors) {
                Text("Incorrect email format.")
            }
        }
    )
}


@Composable
fun UserTextField() {
    var user by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = user,
        onValueChange = { user = it },
        leadingIcon = {
            Icon(
                Icons.Outlined.AccountCircle,
                contentDescription = "email",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
    )
}

@Composable
fun PasswordTextField() {
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                contentDescription = "password",
                tint = Color(0xFF2F2F2F)
            )
        },
        label = { Text("Password") },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                if (showPassword) {
                    Icons.Outlined.Visibility
                } else {
                    Icons.Outlined.VisibilityOff
                },
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Toggle password visibility",
                modifier = Modifier.clickable { showPassword = !showPassword })
        }
    )
}


@Preview
@Composable
fun TestPreview() {
    RestaUTheme {
        SignInScreen()

    }
}
