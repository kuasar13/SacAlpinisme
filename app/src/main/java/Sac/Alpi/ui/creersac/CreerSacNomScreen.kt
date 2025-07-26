package Sac.Alpi.ui.creersac

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import Sac.Alpi.Screen
import Sac.Alpi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreerSacNomScreen(navController: NavController, sacViewModel: SacViewModel) {
    var name by remember { mutableStateOf("") }
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd)))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du sac") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (name.isNotBlank()) {
                    val newId = sacViewModel.createSac(name)
                    navController.navigate(Screen.CreerSacDetail.createRoute(newId))
                }
            }) {
                Text("Suivant")
            }
        }
    }
}
