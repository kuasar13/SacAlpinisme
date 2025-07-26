package Sac.Alpi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SacDao {
    @Insert fun insertSac(sac: SacEntity): Long
    @Insert fun insertJoin(join: SacElementJoin)
    @Query("SELECT * FROM sacs") fun allSacs(): Flow<List<SacEntity>>
    @Transaction
    @Query("SELECT * FROM sacs") fun getAllSacsWithElements(): Flow<List<SacWithElements>>
    @Transaction
    @Query("SELECT * FROM sacs WHERE id = :id") fun getSacWithElements(id: Long): Flow<SacWithElements>
}
