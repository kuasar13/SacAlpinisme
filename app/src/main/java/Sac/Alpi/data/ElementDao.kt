package Sac.Alpi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {
    @Query("SELECT * FROM elements WHERE categoryId = :catId")
    fun getElementsForCategory(catId: Long): Flow<List<ElementEntity>>

    @Insert
    fun insertElement(el: ElementEntity): Long

    @Update
    fun updateElement(el: ElementEntity): Int

    @Delete
    fun deleteElement(el: ElementEntity): Int
}
