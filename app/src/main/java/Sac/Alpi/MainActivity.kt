package Sac.Alpi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import kotlinx.coroutines.delay
import Sac.Alpi.data.AppDatabase
import Sac.Alpi.di.DatabaseModule
import Sac.Alpi.ui.categories.CategoriesScreen
import Sac.Alpi.ui.categories.CategoryViewModel
import Sac.Alpi.ui.creersac.CreerSacNomScreen
import Sac.Alpi.ui.creersac.CreerSacDetailScreen
import Sac.Alpi.ui.creersac.MesSacsScreen
import Sac.Alpi.ui.viewmodel.SacViewModel
import Sac.Alpi.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseModule.provideDatabase(this)
        val categoryVM = CategoryViewModel(db)
        val sacVM = SacViewModel(db)
        setContent {
            SacAlpiTheme {
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) { delay(4000); showSplash = false }
                if (showSplash) {
                    SplashScreen()
                } else {
                    val nav = rememberNavController()
                    NavHost(navController = nav, startDestination = Screen.Main.route) {
                        composable(Screen.Main.route) { MainScreen(nav) }
                        composable(Screen.CreerSacNom.route) {
                            CreerSacNomScreen(navController = nav, sacViewModel = sacVM)
                        }
                        composable(
                            route = Screen.CreerSacDetail.route,
                            arguments = listOf(navArgument("sacId") {
                                type = NavType.LongType
                            })
                        ) { back ->
                            val id = back.arguments?.getLong("sacId") ?: return@composable
                            CreerSacDetailScreen(navController = nav, sacId = id, sacViewModel = sacVM)
                        }
                        composable(Screen.MesSacs.route) {
                            MesSacsScreen(navController = nav, sacViewModel = sacVM)
                        }
                        composable(Screen.Categories.route) {
                            CategoriesScreen(viewModel = categoryVM)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd))),
        contentAlignment = Alignment.Center
    ) {
        Text("Splash…", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun MainScreen(navController: androidx.navigation.NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd)))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Button(onClick = { navController.navigate(Screen.CreerSacNom.route) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TitleColor)
        ) { Text("Créer Sac") }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.MesSacs.route) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TitleColor)
        ) { Text("Mes Sacs") }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Screen.Categories.route) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TitleColor)
        ) { Text("Catégories") }
        Spacer(Modifier.weight(1f))
    }
}
