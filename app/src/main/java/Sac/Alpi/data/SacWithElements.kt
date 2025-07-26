package Sac.Alpi.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SacWithElements(
    @Embedded val sac: SacEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(SacElementJoin::class)
    )
    val elements: List<ElementEntity>
) {
    val totalWeight: Long
        get() = elements.sumOf { it.weight.toLong() }
}
