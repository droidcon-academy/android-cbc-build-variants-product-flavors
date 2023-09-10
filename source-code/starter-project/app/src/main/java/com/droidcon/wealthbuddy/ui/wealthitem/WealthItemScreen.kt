@file:OptIn(ExperimentalComposeUiApi::class)

package com.droidcon.wealthbuddy.ui.wealthitem

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidcon.wealthbuddy.BuildConfig
import com.droidcon.wealthbuddy.R
import com.droidcon.wealthbuddy.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt

@Composable
fun WealthItemScreen(
    modifier: Modifier = Modifier,
    viewModel: WealthItemViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is WealthItemUiState.Success) {
        WealthItemScreenContent(
            items = (items as WealthItemUiState.Success).data,
            onCalculate = { amount, roi, tenure ->
                viewModel.addWealthItem(amount, roi, tenure)
            },
            modifier = modifier
        )
    }
}

@Composable
internal fun WealthItemScreenContent(
    items: List<WealthUIItem>,
    onCalculate: (amount: Double, roi: Double, tenureInYears: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SIEntryArea(
            onCalculate = onCalculate,
            modifier = Modifier.weight(1f)
        )
        PastCalculations(
            items = items,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        )
        FooterArea(modifier = Modifier)
    }
}

@Composable
fun FooterArea(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
    ) {
        val context = LocalContext.current
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            onClick = {
                launchURL(context, BuildConfig.connect_url)
            }
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.title_contact_us)
            )
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            onClick = {
                launchURL(context, BuildConfig.apply_url)
            }
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.title_bank_apply)
            )
        }
    }
}

@Composable
fun SIEntryArea(
    onCalculate: (amount: Double, roi: Double, tenureInYears: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var amount by remember { mutableStateOf(Triple("", "", false)) }
    var roi by remember { mutableStateOf(Triple("", "", false)) }
    var tenureYears by remember { mutableStateOf(Triple("", "", false)) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
        color = Color.Transparent
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(modifier = modifier) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                text = stringResource(id = R.string.welcome_message)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                maxLines = 1,
                value = amount.first,
                isError = amount.third,
                supportingText = {
                    if (amount.third) {
                        Text(
                            text = amount.second,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { amount = amount.copy(first = it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = stringResource(id = R.string.enter_amount))
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                value = roi.first,
                maxLines = 1,
                isError = roi.third,
                supportingText = {
                    if (roi.third) {
                        Text(
                            text = roi.second,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { roi = roi.copy(first = it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = stringResource(id = R.string.enter_roi))
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                value = tenureYears.first,
                maxLines = 1,
                isError = tenureYears.third,
                supportingText = {
                    if (tenureYears.third) {
                        Text(
                            text = tenureYears.second,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = { tenureYears = tenureYears.copy(first = it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = stringResource(id = R.string.enter_tenure))
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val negativeValueMessage = stringResource(R.string.negative_value_message)
                val highTenureMessage = stringResource(R.string.high_tenure_message)
                val highRoiMessage = stringResource(R.string.high_rate_of_interest_message)
                val highAmountMessage = stringResource(R.string.high_amount_message)
                val invalidValueMessage = stringResource(R.string.invalid_input_message)

                Button(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(all = 8.dp),
                    onClick = {
                        try {
                            val amountValue =
                                if (amount.first.trim()
                                        .isNotEmpty()
                                ) amount.first.toDouble() else 0.0
                            val roiValue =
                                if (roi.first.trim().isNotEmpty()) roi.first.toFloat() else 0.0f
                            val tenureValue =
                                if (tenureYears.first.trim()
                                        .isNotEmpty()
                                ) tenureYears.first.toDouble().roundToInt() else 0
                            if (amountValue <= 0 || roiValue <= 0 || tenureValue <= 0)
                                showToast(context, negativeValueMessage)
                            else if (tenureValue > 30)
                                tenureYears =
                                    tenureYears.copy(third = true, second = highTenureMessage)
                            else if (roiValue > 50)
                                roi = roi.copy(third = true, second = highRoiMessage)
                            else if (amount.first.length > 9)
                                amount = amount.copy(second = highAmountMessage, third = true)
                            else {
                                amount = amount.copy(third = false)
                                tenureYears = tenureYears.copy(third = false)
                                roi = roi.copy(third = false)
                                onCalculate(amountValue, roiValue.toDouble(), tenureValue)
                            }
                            keyboardController?.hide()
                        } catch (e: Exception) {
                            showToast(context, invalidValueMessage)
                        }
                    }
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = "Calculate"
                    )
                }
            }
        }
    }
}

@Composable
private fun PastCalculations(
    items: List<WealthUIItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp
            )
    ) {
        for (item in items) {
            CalculationCard(item = item)
        }
    }
}

@Composable
private fun CalculationCard(item: WealthUIItem, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(
                R.string.calc_details,
                item.amount,
                item.totalAmountPaid,
                item.monthlyInstallment,
                item.tenureInYears * 12
            )
        )
    }
}

private fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

private fun launchURL(context: Context, url: String) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    } catch (e: Exception) {
        showToast(context, e.message ?: "Unable to open browser")
    }
}

// Previews
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        PastCalculations(
            items = listOf(
                WealthUIItem(
                    amount = "8,000.00",
                    roi = "6.0",
                    tenureInYears = 2,
                    monthlyInstallment = 373.33.toString(),
                    totalAmountPaid = "${373.33 * 24}"
                ),
                WealthUIItem(
                    amount = "10,000.00",
                    roi = "7.0",
                    tenureInYears = 4,
                    monthlyInstallment = 266.67.toString(),
                    totalAmountPaid = "${266.67 * 48}"
                ),
                WealthUIItem(
                    amount = "20,000.00",
                    roi = "4.0",
                    tenureInYears = 3,
                    monthlyInstallment = 622.22.toString(),
                    totalAmountPaid = "${622.22 * 36}"
                )
            )
        )
    }
}

@Preview(
    name = "Light Mode Preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WealthItemScreenContent(
            items = listOf(
                WealthUIItem(
                    amount = "8,000.00",
                    roi = "6.0",
                    tenureInYears = 2,
                    monthlyInstallment = 373.33.toString(),
                    totalAmountPaid = "${373.33 * 24}"
                ),
                WealthUIItem(
                    amount = "10,000.00",
                    roi = "7.0",
                    tenureInYears = 4,
                    monthlyInstallment = 266.67.toString(),
                    totalAmountPaid = "${266.67 * 48}"
                ),
                WealthUIItem(
                    amount = "20,000.00",
                    roi = "4.0",
                    tenureInYears = 3,
                    monthlyInstallment = 622.22.toString(),
                    totalAmountPaid = 22399.92.toString()
                )
            ),
            onCalculate = { _, _, _ -> }
        )
    }
}

@Preview(
    name = "Dark Mode Preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DarkPortraitPreview() {
    MyApplicationTheme {
        WealthItemScreenContent(
            items = listOf(
                WealthUIItem(
                    amount = "8,000.00",
                    roi = "6.0",
                    tenureInYears = 2,
                    monthlyInstallment = 373.33.toString(),
                    totalAmountPaid = "${373.33 * 24}"
                ),
                WealthUIItem(
                    amount = "10,000.00",
                    roi = "7.0",
                    tenureInYears = 4,
                    monthlyInstallment = 266.67.toString(),
                    totalAmountPaid = "${266.67 * 48}"
                ),
                WealthUIItem(
                    amount = "20,000.00",
                    roi = "4.0",
                    tenureInYears = 3,
                    monthlyInstallment = 622.22.toString(),
                    totalAmountPaid = "${22399.92}"
                )
            ),
            onCalculate = { _, _, _ -> }
        )
    }
}
