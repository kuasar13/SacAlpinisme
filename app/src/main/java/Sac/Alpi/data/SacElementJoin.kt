package Sac.Alpi.data

import androidx.room.Entity

@Entity(primaryKeys = ["sacId","elementId"])
data class SacElementJoin(
    val sacId: Long,
    val elementId: Long
)
