package io.github.paletteLens.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.github.paletteLens.exceptions.AuthenticationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel,
    onSuccesfulSignUpRedirect: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var matchingPasswords by remember { mutableStateOf(false) }

    val errorState by viewModel.errorState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val successState by viewModel.successState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.Center) {
            Text("Criar conta")
        }
        Spacer(modifier = modifier.height(32.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = errorState is AuthenticationException.InvalidEmailException,
        )
        Spacer(modifier = modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                matchingPasswords = password == confirmPassword
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errorState is AuthenticationException.InvalidPasswordException,
        )
        Spacer(modifier = modifier.height(16.dp))
        TextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                matchingPasswords = password == confirmPassword
            },
            label = { Text("Confirme a senha") },
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.signUpWithEmailAndPassword(email, password)
            },
            enabled = !loadingState && matchingPasswords,
        ) {
            Text("Criar conta")
        }

        when {
            successState -> {
                BasicAlertDialog(content = {
                    Card(modifier = modifier.requiredSize(300.dp, 70.dp)) {
                        Column(
                            modifier = modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Success Icon",
                            )
                            Text("Conta criada com sucesso")
                        }
                    }
                }, onDismissRequest = { onSuccesfulSignUpRedirect() })
            }
        }
    }
}
