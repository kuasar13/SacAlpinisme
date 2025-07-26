package Sac.Alpi.ui.creersac

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import Sac.Alpi.ui.theme.*
import Sac.Alpi.ui.viewmodel.SacViewModel

@Composable
fun CreerSacDetailScreen(
    navController: NavController,
    sacId: Long,
    sacViewModel: SacViewModel
) {
    val sacWithEl by sacViewModel.getSacWithElements(sacId).collectAsState(initial = null)
    sacWithEl?.let { sac ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd)))
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(sac.sac.name, style = MaterialTheme.typography.titleLarge, color = TitleColor)
                IconButton(onClick = { /* rename logic */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Renommer")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Poids total : ${sac.totalWeight} g (${sac.totalWeight / 1000.0} kg)",
                color = FooterColor
            )
            Spacer(Modifier.height(16.dp))
            // TODO: ton UI de sélection ici…
            Button(onClick = { /* save & back */ }, Modifier.fillMaxWidth()) {
                Text("Enregistrer")
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = TitleColor)
    }
}
