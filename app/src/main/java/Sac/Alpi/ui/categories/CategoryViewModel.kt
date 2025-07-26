package Sac.Alpi.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import Sac.Alpi.data.AppDatabase
import Sac.Alpi.data.CategoryEntity
import Sac.Alpi.data.ElementEntity

class CategoryViewModel(database: AppDatabase) : ViewModel() {
    private val categoryDao = database.categoryDao()
    private val elementDao  = database.elementDao()

    // Flux des catégories
    val categories: Flow<List<CategoryEntity>> =
        categoryDao.getAllCategories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Ajout, renommage, suppression, déplacement catégories
    fun addCategory(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val idx = categories.stateIn(viewModelScope).value.size
        categoryDao.insertCategory(CategoryEntity(name = name, orderIndex = idx))
    }

    fun renameCategory(cat: CategoryEntity, newName: String) = viewModelScope.launch(Dispatchers.IO) {
        categoryDao.updateCategory(cat.copy(name = newName))
    }

    fun deleteCategory(cat: CategoryEntity) = viewModelScope.launch(Dispatchers.IO) {
        categoryDao.deleteCategory(cat)
        // Réindexation
        categories.stateIn(viewModelScope).value
            .filter { it.id != cat.id }
            .forEachIndexed { i, c -> categoryDao.updateCategory(c.copy(orderIndex = i)) }
    }

    fun moveCategory(cat: CategoryEntity, up: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val list = categories.stateIn(viewModelScope).value.toMutableList()
        val idx = list.indexOfFirst { it.id == cat.id }
        val tgt = if (up) idx - 1 else idx + 1
        if (tgt in list.indices) {
            val other = list[tgt]
            categoryDao.updateCategory(cat.copy(orderIndex = tgt))
            categoryDao.updateCategory(other.copy(orderIndex = idx))
        }
    }

    // Éléments: add, rename, delete
    fun addElement(categoryId: Long, name: String, weight: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            elementDao.insertElement(ElementEntity(categoryId = categoryId, name = name, weight = weight))
        }

    fun renameElement(el: ElementEntity, newName: String, newWeight: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            elementDao.updateElement(el.copy(name = newName, weight = newWeight))
        }

    fun deleteElement(el: ElementEntity) = viewModelScope.launch(Dispatchers.IO) {
        elementDao.deleteElement(el)
    }

    fun getElements(categoryId: Long): Flow<List<ElementEntity>> =
        elementDao.getElementsForCategory(categoryId)
}
