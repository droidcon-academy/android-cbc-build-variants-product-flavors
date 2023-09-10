package com.droidcon.wealthbuddy.domain

import com.droidcon.wealthbuddy.data.WealthDataItem
import com.droidcon.wealthbuddy.data.WealthItemRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateAndSaveUseCase @Inject constructor(
    private val repository: WealthItemRepository
) {
    suspend operator fun invoke(
        amount: Double,
        roi: Double,
        tenureInYears: Int,
        isInvestment: Boolean = false
    ) {
        val wealthDataItem = if (isInvestment) {
            val principalAmount =
                (amount * 100.0 * 100.0 / (100 + roi * tenureInYears)).roundToInt() / 100.0
            val monthlyInstallment =
                (100 * principalAmount / (12.0 * tenureInYears)).roundToInt() / 100.0
            WealthDataItem(
                target = amount,
                tenureInMonths = tenureInYears * 12,
                annualROI = roi.toFloat(),
                monthlyInstallment = monthlyInstallment,
                totalAmountPaid = principalAmount,
            )
        } else {
            val simpleInterest = (amount * roi * tenureInYears).roundToInt() / 100.0
            val totalAmount = ((amount + simpleInterest) * 100).roundToInt() / 100.0
            val monthlyInstallment = (100 * totalAmount / (tenureInYears * 12)).roundToInt() / 100.0
            WealthDataItem(
                target = amount,
                tenureInMonths = tenureInYears * 12,
                annualROI = roi.toFloat(),
                monthlyInstallment = monthlyInstallment,
                totalAmountPaid = totalAmount,
            )
        }

        repository.add(wealthDataItem)
    }
}
