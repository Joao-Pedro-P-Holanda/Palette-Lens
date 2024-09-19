package io.github.paletteLens.service.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import io.github.paletteLens.domain.model.User
import io.github.paletteLens.domain.model.UserState
import io.github.paletteLens.exceptions.AuthenticationException
import kotlinx.coroutines.flow.MutableStateFlow

class AuthServiceFirebaseImp : AuthService() {
    private val auth: FirebaseAuth = Firebase.auth
    override var user: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)

    init {

        val listener =
            FirebaseAuth.AuthStateListener {
                    auth ->
                user.value = UserState.Loading
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    user.value =
                        UserState.Loaded(
                            User(
                                currentUser.uid,
                                currentUser.displayName ?: "",
                                currentUser.email ?: "",
                                currentUser.photoUrl.toString(),
                            ),
                        )
                } else {
                    user.value = UserState.None
                }
            }
        auth.addAuthStateListener(listener)
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("AuthServiceFirebaseImp", e.localizedMessage ?: "Nenhum usuário encontrado com este email")
            throw AuthenticationException.InvalidEmailException(
                e.localizedMessage ?: "Nenhum usuário encontrado com este email",
                e,
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("AuthServiceFirebaseImp", e.localizedMessage ?: "Senha Incorreta")
            throw AuthenticationException.InvalidPasswordException(
                e.localizedMessage ?: "Senha Incorreta",
                e,
            )
        } catch (e: FirebaseTooManyRequestsException) {
            Log.e("AuthServiceFirebaseImp", e.localizedMessage ?: "Muitas tentativas de login")
            throw AuthenticationException.TooManyRequestsException(
                e.localizedMessage ?: "Muitas tentativas",
                e,
            )
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (e: FirebaseAuthWeakPasswordException) {
            throw AuthenticationException.WeakPasswordException(
                e.localizedMessage ?: "Senha Fraca",
                e,
            )
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw AuthenticationException.InvalidEmailException(
                e.localizedMessage ?: "Email Inválido",
                e,
            )
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
