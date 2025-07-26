package Sac.Alpi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sacs")
data class SacEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
