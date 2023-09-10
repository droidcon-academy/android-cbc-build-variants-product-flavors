package com.droidcon.wealthbuddy.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.droidcon.wealthbuddy.data.WealthItemRepository
import com.droidcon.wealthbuddy.data.DefaultWealthItemRepository
import com.droidcon.wealthbuddy.data.WealthDataItem
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsWealthItemRepository(
        wealthItemRepository: DefaultWealthItemRepository
    ): WealthItemRepository
}

class FakeWealthItemRepository @Inject constructor() : WealthItemRepository {
    override val wealthItems: Flow<List<WealthDataItem>> = flowOf(fakeWealthItems)

    override suspend fun add(wealthDataItem: WealthDataItem) {
        throw NotImplementedError()
    }
}

val fakeWealthItems = listOf(
    WealthDataItem(
        target = 8000.0,
        annualROI = 6.0f,
        tenureInMonths = 24,
        monthlyInstallment = 373.33,
        totalAmountPaid = 373.33 * 24
    ),
    WealthDataItem(
        target = 10000.0,
        annualROI = 7.0f,
        tenureInMonths = 48,
        monthlyInstallment = 266.67,
        totalAmountPaid = 266.67 * 48
    ),
    WealthDataItem(
        target = 20000.0,
        annualROI = 4.0f,
        tenureInMonths = 36,
        monthlyInstallment = 622.22,
        totalAmountPaid = 622.22 * 36
    )
)
