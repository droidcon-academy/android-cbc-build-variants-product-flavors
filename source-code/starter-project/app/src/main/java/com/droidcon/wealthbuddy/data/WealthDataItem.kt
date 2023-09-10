package com.droidcon.wealthbuddy.data

import com.droidcon.wealthbuddy.data.local.database.WealthItem

data class WealthDataItem(
    val target: Double,
    val tenureInMonths: Int,
    val annualROI: Float,
    val monthlyInstallment: Double,
    val totalAmountPaid: Double,
    val currency: String = "USD"
) {
    companion object {
        fun from(wealthItem: WealthItem) = WealthDataItem(
            target = wealthItem.targetAmount,
            tenureInMonths = wealthItem.tenureInMonths,
            annualROI = wealthItem.annualRateOfInterest,
            monthlyInstallment = wealthItem.monthlyAmount,
            totalAmountPaid = wealthItem.totalAmountPaid
        )
    }
}

fun WealthDataItem.toWealthItemEntity() = WealthItem(
    targetAmount = target,
    tenureInMonths =  tenureInMonths,
    annualRateOfInterest =  annualROI,
    monthlyAmount = monthlyInstallment,
    totalAmountPaid = totalAmountPaid
)