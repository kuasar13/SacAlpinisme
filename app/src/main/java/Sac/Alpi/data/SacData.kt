package Sac.Alpi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "sacs")
data class SacEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "sac_element_join",
    primaryKeys = ["sacId", "elementId"],
    foreignKeys = [
        ForeignKey(entity = SacEntity::class, parentColumns = ["id"], childColumns = ["sacId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ElementEntity::class, parentColumns = ["id"], childColumns = ["elementId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class SacElementJoin(
    val sacId: Long,
    val elementId: Long
)

// Relation: Sac with its Elements
data class SacWithElements(
    @Embedded val sac: SacEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SacElementJoin::class,
            parentColumn = "sacId",
            entityColumn = "elementId"
        )
    ) val elements: List<ElementEntity>
)

@Dao
interface SacDao {
    @Insert fun insertSac(sac: SacEntity): Long
    @Update fun updateSac(sac: SacEntity)
    @Delete fun deleteSac(sac: SacEntity)

    @Transaction
    @Query("SELECT * FROM sacs ORDER BY createdAt DESC")
    fun getAllSacsWithElements(): Flow<List<SacWithElements>>

    @Transaction
    @Query("SELECT * FROM sacs WHERE id = :sacId")
    fun getSacWithElements(sacId: Long): Flow<SacWithElements>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addElementToSac(join: SacElementJoin)
    @Delete fun removeElementFromSac(join: SacElementJoin)
}