package Sac.Alpi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY orderIndex")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert
    fun insertCategory(cat: CategoryEntity): Long

    @Update
    fun updateCategory(cat: CategoryEntity): Int

    @Delete
    fun deleteCategory(cat: CategoryEntity): Int
}
