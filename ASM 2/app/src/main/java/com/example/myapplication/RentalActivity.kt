package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.CarRentalDomain
import com.example.myapplication.domain.ValidationResult
import com.example.myapplication.model.Car
import com.example.myapplication.ui.components.StarRatingBar
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Locale

/**
 * Activity 2: Rental Details & Booking Confirmation.
 * Receives selected car as Parcelable via explicit Intent.
 * Uses native [Slider] for duration selection, calculates total cost dynamically,
 * validates rules, and returns booking result via Activity Result API.
 */
class RentalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_CAR, Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_CAR)
        }

        val creditBalance = intent.getIntExtra(EXTRA_CREDIT_BALANCE, 500)
        val isDarkMode = intent.getBooleanExtra(EXTRA_IS_DARK_MODE, false)

        if (car == null) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        setContent {
            MyApplicationTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RentalScreen(
                        car = car,
                        creditBalance = creditBalance,
                        onSaveBooking = { days, totalCost ->
                            val resultIntent = Intent().apply {
                                putExtra(EXTRA_BOOKED_CAR_ID, car.id)
                                putExtra(EXTRA_RENTAL_DAYS, days)
                                putExtra(EXTRA_TOTAL_COST, totalCost)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        },
                        onCancel = {
                            setResult(RESULT_CANCELED)
                            finish()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_CAR = "extra_car"
        const val EXTRA_CREDIT_BALANCE = "extra_credit_balance"
        const val EXTRA_IS_DARK_MODE = "extra_is_dark_mode"
        const val EXTRA_BOOKED_CAR_ID = "extra_booked_car_id"
        const val EXTRA_RENTAL_DAYS = "extra_rental_days"
        const val EXTRA_TOTAL_COST = "extra_total_cost"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalScreen(
    car: Car,
    creditBalance: Int,
    onSaveBooking: (rentalDays: Int, totalCost: Int) -> Unit,
    onCancel: () -> Unit
) {
    // Intercept system Back button / gesture to guarantee cancellation
    BackHandler(onBack = onCancel)

    var rentalDaysFloat by remember { mutableFloatStateOf(1f) }
    val rentalDays = rentalDaysFloat.toInt()
    val totalCost = CarRentalDomain.calculateTotalCost(car.dailyCost, rentalDays)

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.rental_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Car Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = car.imageResId),
                        contentDescription = car.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = car.model,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StarRatingBar(rating = car.rating)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.year_format, car.year),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(R.string.kms_format, car.kmsDriven),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.cost_format, car.dailyCost),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Duration & Pricing Card (with Native Slider Widget)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.duration_label, rentalDays),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Native Slider widget for selecting rental duration from 1 to 7 days
                    Slider(
                        value = rentalDaysFloat,
                        onValueChange = {
                            rentalDaysFloat = it
                            errorMessage = null // Clear error when duration changes
                        },
                        valueRange = 1f..7f,
                        steps = 5, // Creates discrete steps at 1, 2, 3, 4, 5, 6, 7 days
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "1 Day",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "7 Days",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dynamic Cost & Balance Overview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Balance:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(R.string.balance_format, creditBalance),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Total Rental Cost:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(R.string.total_cost_format, totalCost),
                                style = MaterialTheme.typography.headlineSmall,
                                color = if (totalCost > 400 || totalCost > creditBalance) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Validation Error Banner
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.cancel_booking))
                }

                Button(
                    onClick = {
                        val validation = CarRentalDomain.validateRental(
                            car = car,
                            rentalDays = rentalDays,
                            currentBalance = creditBalance
                        )
                        when (validation) {
                            is ValidationResult.Success -> {
                                errorMessage = null
                                onSaveBooking(rentalDays, totalCost)
                            }
                            is ValidationResult.Error -> {
                                errorMessage = validation.message
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.confirm_booking))
                }
            }
        }
    }
}
