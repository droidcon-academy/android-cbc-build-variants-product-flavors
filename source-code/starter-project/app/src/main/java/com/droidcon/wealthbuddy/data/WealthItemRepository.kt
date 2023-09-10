package com.droidcon.wealthbuddy.data

import com.droidcon.wealthbuddy.data.local.database.WealthItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val RESULT_SIZE = 3

interface WealthItemRepository {
    val wealthItems: Flow<List<WealthDataItem>>

    suspend fun add(wealthDataItem: WealthDataItem)
}

class DefaultWealthItemRepository @Inject constructor(
    private val wealthItemDao: WealthItemDao
) : WealthItemRepository {

    override val wealthItems: Flow<List<WealthDataItem>> =
        wealthItemDao.getWealthItems(RESULT_SIZE)
            .map { items ->
                items.map {
                    WealthDataItem.from(it)
                }
            }

    override suspend fun add(wealthDataItem: WealthDataItem) {
        wealthItemDao.insertWealthItem(wealthDataItem.toWealthItemEntity())
    }
}
