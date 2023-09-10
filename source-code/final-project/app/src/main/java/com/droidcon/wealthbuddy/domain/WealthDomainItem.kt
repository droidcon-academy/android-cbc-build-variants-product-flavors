package com.droidcon.wealthbuddy.domain

import com.droidcon.wealthbuddy.data.WealthDataItem

data class WealthDomainItem(
    val target: Double,
    val tenureInMonths: Int,
    val annualROI: Float,
    val monthlyInstallment: Double,
    val totalAmountPaid: Double,
    val currency: String = "USD"
) {
    companion object {
        fun from(item: WealthDataItem) = WealthDomainItem(
            target = item.target,
            tenureInMonths = item.tenureInMonths,
            annualROI = item.annualROI,
            monthlyInstallment = item.monthlyInstallment,
            totalAmountPaid = item.totalAmountPaid,
            currency = item.currency
        )
    }
}