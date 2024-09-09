package io.github.paletteLens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.paletteLens.presentation.components.SelectedImageViewModel
import io.github.paletteLens.presentation.theme.AppTheme
import io.github.paletteLens.presentation.ui.ExtractColorScreen
import io.github.paletteLens.presentation.ui.ExtractedPalettesScreen
import io.github.paletteLens.presentation.ui.ExtractedResultScreen
import io.github.paletteLens.presentation.ui.Route
import io.github.paletteLens.presentation.ui.SignInScreen
import io.github.paletteLens.presentation.ui.SignUpScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 0)
        }
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val routes =
                    listOf(
                        Route("Extrair Paleta", "extract-palette", Icons.Default.Camera),
                        Route("Minhas Paletas", "extracted-palettes", Icons.Default.Palette),
                    )
                var selectedNavItem by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
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
                        composable("extract-palette") {
                            val sharedImageViewModel: SelectedImageViewModel by viewModels()
                            ExtractColorScreen(sharedImageViewModel = sharedImageViewModel)
                        }
                        composable("extract-palette-result") {
                            ExtractedResultScreen()
                        }
                        composable("extracted-palettes") {
                            ExtractedPalettesScreen()
                        }
                        composable("sign-in") {
                            SignInScreen()
                        }
                        composable("sign-up") {
                            SignUpScreen()
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
