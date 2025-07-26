package Sac.Alpi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CategoryEntity::class, ElementEntity::class, SacEntity::class, SacElementJoin::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun elementDao(): ElementDao
    abstract fun sacDao(): SacDao
}
