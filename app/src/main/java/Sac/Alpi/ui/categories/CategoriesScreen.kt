package Sac.Alpi.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Sac.Alpi.data.CategoryEntity
import Sac.Alpi.data.ElementEntity
import Sac.Alpi.ui.categories.CategoryViewModel
import Sac.Alpi.ui.theme.*

@Composable
fun CategoriesScreen(viewModel: CategoryViewModel) {
    // Category dialog states
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var categoryToRename by remember { mutableStateOf<CategoryEntity?>(null) }
    var categoryToDelete by remember { mutableStateOf<CategoryEntity?>(null) }
    var categoryToAddElement by remember { mutableStateOf<CategoryEntity?>(null) }
    // Element dialog states
    var elementToRename by remember { mutableStateOf<ElementEntity?>(null) }
    var elementToDelete by remember { mutableStateOf<ElementEntity?>(null) }

    // Category form fields
    var newCategoryName by remember { mutableStateOf("") }
    var renameCategoryName by remember { mutableStateOf("") }
    // Element form fields
    var newElementName by remember { mutableStateOf("") }
    var newElementWeight by remember { mutableStateOf("") }
    var renameElementName by remember { mutableStateOf("") }
    var renameElementWeight by remember { mutableStateOf("") }

    val categories by viewModel.categories.collectAsState(initial = emptyList())

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightBackgroundStart, LightBackgroundEnd)))
            .padding(16.dp)
    ) {
        Column {
            // Header
            Text(
                text = "Catégories",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TitleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
            // Add Category Button
            Button(
                onClick = { showAddCategoryDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TitleColor,
                    contentColor = LightBackgroundStart
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ajouter une catégorie", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Category List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 64.dp)  // ← ajoute 16.dp sous le contenu
            ) {
                items(categories, key = { it.id }) { cat ->
                    CategoryCard(
                        viewModel = viewModel,
                        category = cat,
                        onRenameCategory = { categoryToRename = it; renameCategoryName = it.name },
                        onDeleteCategory = { categoryToDelete = it },
                        onAddElement = { categoryToAddElement = it },
                        onMoveUp = { viewModel.moveCategory(it, true) },
                        onMoveDown = { viewModel.moveCategory(it, false) },
                        onRenameElement = { elementToRename = it; renameElementName = it.name; renameElementWeight = it.weight.toString() },
                        onDeleteElement = { elementToDelete = it }
                    )
                }
            }
        }

        // --- Dialog: Add Category ---
        if (showAddCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showAddCategoryDialog = false; newCategoryName = "" },
                title = { Text("Nouvelle catégorie") },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nom de la catégorie") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addCategory(newCategoryName.trim())
                        showAddCategoryDialog = false; newCategoryName = ""
                    }) { Text("Ajouter") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCategoryDialog = false; newCategoryName = "" }) { Text("Annuler") }
                }
            )
        }

        // --- Dialog: Rename Category ---
        categoryToRename?.let { cat ->
            AlertDialog(
                onDismissRequest = { categoryToRename = null; renameCategoryName = "" },
                title = { Text("Renommer catégorie") },
                text = {
                    OutlinedTextField(
                        value = renameCategoryName,
                        onValueChange = { renameCategoryName = it },
                        label = { Text("Nouveau nom") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.renameCategory(cat, renameCategoryName.trim())
                        categoryToRename = null; renameCategoryName = ""
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { categoryToRename = null; renameCategoryName = "" }) { Text("Annuler") }
                }
            )
        }

        // --- Dialog: Delete Category ---
        categoryToDelete?.let { cat ->
            AlertDialog(
                onDismissRequest = { categoryToDelete = null },
                title = { Text("Supprimer catégorie") },
                text = { Text("Supprimer '${cat.name}' ?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteCategory(cat)
                        categoryToDelete = null
                    }) { Text("Supprimer") }
                },
                dismissButton = { TextButton(onClick = { categoryToDelete = null }) { Text("Annuler") } }
            )
        }

        // --- Dialog: Add Element ---
        categoryToAddElement?.let { cat ->
            AlertDialog(
                onDismissRequest = { categoryToAddElement = null; newElementName = ""; newElementWeight = "" },
                title = { Text("Nouvel élément pour '${cat.name}'") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newElementName,
                            onValueChange = { newElementName = it },
                            label = { Text("Nom de l'élément") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newElementWeight,
                            onValueChange = { newElementWeight = it },
                            label = { Text("Poids (g)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addElement(cat.id, newElementName.trim(), newElementWeight.toDoubleOrNull() ?: 0.0)
                        categoryToAddElement = null; newElementName = ""; newElementWeight = ""
                    }) { Text("Ajouter") }
                },
                dismissButton = { TextButton(onClick = { categoryToAddElement = null; newElementName = ""; newElementWeight = "" }) { Text("Annuler") } }
            )
        }

        // --- Dialog: Rename Element ---
        elementToRename?.let { el ->
            AlertDialog(
                onDismissRequest = { elementToRename = null; renameElementName = ""; renameElementWeight = "" },
                title = { Text("Renommer élément") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = renameElementName,
                            onValueChange = { renameElementName = it },
                            label = { Text("Nom") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = renameElementWeight,
                            onValueChange = { renameElementWeight = it },
                            label = { Text("Poids (g)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.renameElement(el, renameElementName.trim(), renameElementWeight.toDoubleOrNull() ?: el.weight)
                        elementToRename = null; renameElementName = ""; renameElementWeight = ""
                    }) { Text("OK") }
                },
                dismissButton = { TextButton(onClick = { elementToRename = null; renameElementName = ""; renameElementWeight = "" }) { Text("Annuler") } }
            )
        }

        // --- Dialog: Delete Element ---
        elementToDelete?.let { el ->
            AlertDialog(
                onDismissRequest = { elementToDelete = null },
                title = { Text("Supprimer élément") },
                text = { Text("Supprimer '${el.name}' ?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteElement(el)
                        elementToDelete = null
                    }) { Text("Supprimer") }
                },
                dismissButton = { TextButton(onClick = { elementToDelete = null }) { Text("Annuler") } }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    viewModel: CategoryViewModel,
    category: CategoryEntity,
    onRenameCategory: (CategoryEntity) -> Unit,
    onDeleteCategory: (CategoryEntity) -> Unit,
    onAddElement: (CategoryEntity) -> Unit,
    onMoveUp: (CategoryEntity) -> Unit,
    onMoveDown: (CategoryEntity) -> Unit,
    onRenameElement: (ElementEntity) -> Unit,
    onDeleteElement: (ElementEntity) -> Unit
) {
    val elements by viewModel.getElements(category.id).collectAsState(initial = emptyList())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundTaupe)
    ) {
        Column {
            // Section titre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackgroundTaupe)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // Section boutons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBackgroundEnd)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onMoveUp(category) }) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Monter")
                }
                IconButton(onClick = { onMoveDown(category) }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descendre")
                }
                IconButton(onClick = { onRenameCategory(category) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Renommer catégorie")
                }
                IconButton(onClick = { onDeleteCategory(category) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer catégorie")
                }
                IconButton(onClick = { onAddElement(category) }) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter élément")
                }
            }
            // Section éléments
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBackgroundEnd)
                    .padding(16.dp)
            ) {
                elements.forEach { el ->
                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "- ${el.name} : ${el.weight} g",
                            color = ElementsColor  ,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options élément")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Renommer") },
                                onClick = { expanded = false; onRenameElement(el) }
                            )
                            DropdownMenuItem(
                                text = { Text("Supprimer") },
                                onClick = { expanded = false; onDeleteElement(el) }
                            )
                        }
                    }
                }
            }
        }
    }
}
