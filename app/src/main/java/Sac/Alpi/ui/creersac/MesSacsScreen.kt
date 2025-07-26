package Sac.Alpi.ui.creersac

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import Sac.Alpi.ui.viewmodel.SacViewModel
import Sac.Alpi.ui.theme.*

@Composable
fun MesSacsScreen(navController: NavController, sacViewModel: SacViewModel) {
    val allSacs by sacViewModel.getAllSacs().collectAsState(initial = emptyList())
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(allSacs) { sac ->
            Card { Column(Modifier.padding(16.dp)) {
                Text(sac.sac.name, style = MaterialTheme.typography.titleMedium)
                Text("Poids : ${sac.totalWeight} g", style = MaterialTheme.typography.bodyMedium)
            } }
        }
    }
}
