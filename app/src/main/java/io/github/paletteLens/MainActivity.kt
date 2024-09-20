package io.github.paletteLens

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import io.github.paletteLens.domain.model.UserState
import io.github.paletteLens.presentation.components.SelectedImageViewModel
import io.github.paletteLens.presentation.theme.AppTheme
import io.github.paletteLens.presentation.ui.ExtractColorScreen
import io.github.paletteLens.presentation.ui.ExtractedPalettesScreen
import io.github.paletteLens.presentation.ui.ExtractedPalettesViewModel
import io.github.paletteLens.presentation.ui.ExtractedResultScreen
import io.github.paletteLens.presentation.ui.Route
import io.github.paletteLens.presentation.ui.SignInScreen
import io.github.paletteLens.presentation.ui.SignInViewModel
import io.github.paletteLens.presentation.ui.SignUpScreen
import io.github.paletteLens.presentation.ui.SignUpViewModel
import io.github.paletteLens.service.ConnectivityBroadcastReceiver
import io.github.paletteLens.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    ComponentActivity() {
    @Inject
    lateinit var authService: AuthService
    private val connectivityState = MutableStateFlow<Boolean?>(null)
    private val connectivityBroadcastReceiver = ConnectivityBroadcastReceiver(connectivityState)

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(
            connectivityBroadcastReceiver,
            IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"),
        )
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 0)
        }
        enableEdgeToEdge()

        setContent {
            AppTheme(darkTheme = false) {
                val connectivityStateValue by connectivityState.collectAsState()
                val navController = rememberNavController()
                val routes =
                    listOf(
                        Route("Extrair Paleta", "extract-palette", Icons.Default.Camera),
                        Route("Minhas Paletas", "extracted-palettes", Icons.Default.Palette),
                    )
                var selectedNavItem by remember { mutableIntStateOf(0) }

                var showUserMenu by remember {
                    mutableStateOf(false)
                }

                val coroutineScope = rememberCoroutineScope()
                val userState by authService.user.collectAsState()

                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = "Palette Lens", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                            colors =
                                TopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                ),
                            actions = {
                                when {
                                    userState is UserState.Loaded ->
                                        IconButton(onClick = { showUserMenu = !showUserMenu }) {
                                            Icon(
                                                Icons.Default.AccountCircle,
                                                contentDescription = "User Menu",
                                            )
                                            DropdownMenu(expanded = showUserMenu, onDismissRequest = { showUserMenu = false }) {
                                                DropdownMenuItem(
                                                    text = { Text("Sair") },
                                                    trailingIcon = {
                                                        Icon(
                                                            Icons.AutoMirrored.Filled.Logout,
                                                            contentDescription = "Logout",
                                                        )
                                                    },
                                                    onClick = {
                                                        coroutineScope.launch {
                                                            authService.signOut()
                                                        }
                                                    },
                                                )
                                            }
                                        }
                                    else -> {
                                    }
                                }
                            },
                        )
                    },
                    bottomBar = {
                        BottomNavigation(
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        ) {
                            routes.forEachIndexed { num, route ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            route.icon,
                                            contentDescription = route.name,
                                            tint = MaterialTheme.colorScheme.tertiary,
                                        )
                                    },
                                    label = { Text(route.name) },
                                    selected = selectedNavItem == num,
                                    // TODO: Fix selected and unselected colors not being applied
                                    selectedContentColor = MaterialTheme.colorScheme.primary,
                                    unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                                    onClick = {
                                        selectedNavItem = num
                                        navController.navigate(route.content) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "extract-palette",
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        val sharedImageViewModel: SelectedImageViewModel by viewModels()

                        composable("extract-palette") {
                            ExtractColorScreen(
                                connectivityStatus = connectivityStateValue,
                                currentUser = userState,
                                loginRedirect = {
                                    navController.navigate("sign-in") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                sharedImageViewModel = sharedImageViewModel,
                                goToResult = { navController.navigate("extract-palette-result") },
                            )
                        }
                        composable("extract-palette-result") {
                            ExtractedResultScreen(sharedImageViewModel = sharedImageViewModel)
                        }
                        composable("extracted-palettes") {
                            val extractedPalettesViewModel: ExtractedPalettesViewModel by viewModels()
                            ExtractedPalettesScreen(extractedPalettesViewModel = extractedPalettesViewModel)
                        }
                        composable("sign-up") {
                            val signUpViewModel by viewModels<SignUpViewModel>()
                            SignUpScreen(
                                viewModel = signUpViewModel,
                                onSuccesfulSignUpRedirect = { navController.navigate("extract-palette") },
                            )
                        }
                        composable("sign-in") {
                            val signInViewModel: SignInViewModel by viewModels(
                                extrasProducer = {
                                    defaultViewModelCreationExtras.withCreationCallback<SignInViewModel.Factory> { factory ->
                                        factory.create(
                                            successfulLoginNavigate = {
                                                navController.navigate("extract-palette")
                                            },
                                            createAccountNavigate = {
                                                navController.navigate("sign-up") {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                        )
                                    }
                                },
                            )
                            SignInScreen(viewModel = signInViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val PERMISSIONS =
            arrayOf(
                android.Manifest.permission.CAMERA,
            )
    }
}
