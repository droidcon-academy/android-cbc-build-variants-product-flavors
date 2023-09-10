package com.droidcon.wealthbuddy.ui.wealthitem

import com.droidcon.wealthbuddy.domain.WealthDomainItem
import java.text.DecimalFormat
import java.text.NumberFormat

data class WealthUIItem(
    val amount: String,
    val tenureInYears: Int,
    val roi: String,
    val monthlyInstallment: String,
    val totalAmountPaid: String,
    val currency: String = "USD"
) {

    companion object {

        private val formatter: NumberFormat = DecimalFormat("###,###,##0.00")

        fun from(item: WealthDomainItem) = WealthUIItem(
            amount = formatter.format(item.target),
            tenureInYears = item.tenureInMonths.floorDiv(12),
            roi = String.format("%.2f", item.annualROI),
            monthlyInstallment = formatter.format(item.monthlyInstallment),
            totalAmountPaid = formatter.format(item.totalAmountPaid),
            currency = item.currency
        )
    }
}
