package com.example.restau.presentation.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restau.R
import com.example.restau.ui.theme.RestaUTheme

@Composable
fun SignInScreen(viewModel: EmailViewModel = viewModel()){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Image(
            painter = painterResource(id = R.drawable.restau_1_),
            contentDescription = "RestaU logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 65.dp)
                .size(100.dp)
        )

        Text("Login")

        ValidatingInputTextField( email = viewModel.email,
            updateState = { input -> viewModel.updateEmail(input) },
            validatorHasErrors = viewModel.emailHasErrors)
    }

}


class EmailViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    val emailHasErrors by derivedStateOf {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
            .padding(10.dp),
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

@Preview
@Composable
fun TestPreview(){
    RestaUTheme {
        SignInScreen()

    }
}
