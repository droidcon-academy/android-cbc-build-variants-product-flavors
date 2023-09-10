package com.droidcon.wealthbuddy.domain

import com.droidcon.wealthbuddy.data.WealthItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWealthDataItemsUseCase @Inject constructor(
    private val repository: WealthItemRepository
) {
    operator fun invoke(): Flow<List<WealthDomainItem>> {
        return repository.wealthItems.map {
            it.map { dataItem -> WealthDomainItem.from(dataItem) }
        }
    }
}
