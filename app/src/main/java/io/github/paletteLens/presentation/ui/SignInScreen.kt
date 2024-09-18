package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.github.paletteLens.exceptions.AuthenticationException

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val errorState by viewModel.errorState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Fazer login")
        }
        Spacer(modifier = modifier.padding(32.dp))
        TextField(
            label = { Text(text = "Email") },
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            isError = errorState is AuthenticationException.InvalidEmailException,
        )
        Spacer(modifier = modifier.height(16.dp))
        TextField(
            label = { Text(text = "Senha") },
            value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            isError = errorState is AuthenticationException.InvalidPasswordException,
        )
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.signInWithEmailAndPassword(email, password)
            },
            enabled = !loadingState && email.isNotBlank() && password.length >= 6,
        ) {
            Text("Entrar")
        }
        ClickableText(
            text = AnnotatedString("Não possui uma conta? Cadastre-se"),
            onClick = { viewModel.goToSignUp() },
        )
    }
}
