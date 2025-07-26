package Sac.Alpi.ui.viewmodel

import Sac.Alpi.data.AppDatabase
import Sac.Alpi.data.SacElementJoin
import Sac.Alpi.data.SacEntity
import Sac.Alpi.data.SacWithElements
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SacViewModel(db: AppDatabase) : ViewModel() {
    private val dao = db.sacDao()
    fun createSac(name: String): Long = runBlocking { dao.insertSac(SacEntity(name = name)) }
    fun addElementToSac(sacId: Long, elementId: Long) {
        runBlocking { dao.insertJoin(SacElementJoin(sacId, elementId)) }
    }
    fun getAllSacs(): Flow<List<SacWithElements>> = dao.getAllSacsWithElements()
    fun getSacWithElements(id: Long): Flow<SacWithElements> = dao.getSacWithElements(id)
}
