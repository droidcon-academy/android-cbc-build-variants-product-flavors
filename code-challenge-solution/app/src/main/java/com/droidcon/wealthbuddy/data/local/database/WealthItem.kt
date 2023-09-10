package com.droidcon.wealthbuddy.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "wealth_items")
data class WealthItem(
    val targetAmount: Double,
    val tenureInMonths: Int,
    val annualRateOfInterest: Float,
    val monthlyAmount: Double,
    val totalAmountPaid: Double
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface WealthItemDao {
    @Query("SELECT * FROM wealth_items ORDER BY uid DESC LIMIT :size")
    fun getWealthItems(size: Int): Flow<List<WealthItem>>

    @Insert
    suspend fun insertWealthItem(item: WealthItem)
}
