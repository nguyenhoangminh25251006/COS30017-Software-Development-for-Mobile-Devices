package com.example.myapplication

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.CarRentalDomain
import com.example.myapplication.domain.ValidationResult
import com.example.myapplication.model.Car
import com.example.myapplication.ui.components.AppLocaleProvider
import com.example.myapplication.ui.components.OutlinedBadge
import com.example.myapplication.ui.components.StarRatingBar
import com.example.myapplication.ui.components.outlineBorder
import com.example.myapplication.ui.theme.AccentError
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt

/**
 * Activity 2: Rental Details & Booking Confirmation.
 * Receives selected car as Parcelable via explicit Intent.
 * Features native Slider, dynamic cost calculation, strict credit rule validation,
 * bilingual localization, adaptive portrait/landscape orientation layouts,
 * and seamless return of booking result via Activity Result API.
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

        val creditBalance = intent.getIntExtra(EXTRA_CREDIT_BALANCE, CarRentalDomain.INITIAL_CREDIT_BALANCE)
        val isDarkMode = intent.getBooleanExtra(EXTRA_IS_DARK_MODE, false)
        val appLanguage = intent.getStringExtra(EXTRA_LANGUAGE) ?: "en"

        if (car == null) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        setContent {
            AppLocaleProvider(language = appLanguage) {
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
    }

    companion object {
        const val EXTRA_CAR = "extra_car"
        const val EXTRA_CREDIT_BALANCE = "extra_credit_balance"
        const val EXTRA_IS_DARK_MODE = "extra_is_dark_mode"
        const val EXTRA_LANGUAGE = "extra_language"
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

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Use rememberSaveable to persist user input across screen rotation / configuration change
    var rentalDays by rememberSaveable { mutableIntStateOf(CarRentalDomain.MIN_RENTAL_DAYS) }
    val totalCost = CarRentalDomain.calculateTotalCost(car.dailyCost, rentalDays)

    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val handleConfirmBooking: () -> Unit = {
        when (val validation = CarRentalDomain.validateRental(car, rentalDays, creditBalance)) {
            is ValidationResult.Success -> {
                errorMessage = null
                onSaveBooking(rentalDays, totalCost)
            }
            is ValidationResult.Error -> {
                errorMessage = context.getString(
                    validation.messageResId,
                    *validation.formatArgs.toTypedArray()
                )
            }
        }
    }

    // Slider, cost breakdown, error banner and buttons are shared by both orientations
    val bookingControls: @Composable () -> Unit = {
        DurationPricingCard(
            rentalDays = rentalDays,
            onDaysChange = {
                rentalDays = it
                errorMessage = null
            },
            creditBalance = creditBalance,
            totalCost = totalCost
        )

        errorMessage?.let { error ->
            ValidationErrorBanner(error = error)
        }

        RentalActionButtons(
            onCancel = onCancel,
            onConfirm = handleConfirmBooking
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.rental_details_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        if (isLandscape) {
            // Adaptive 2-Column Dual-Pane Layout for Landscape Orientation
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Left Column: Selected Vehicle Summary
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    VehicleSummaryCard(car = car, imageHeight = 140.dp)
                }

                // Right Column: Rental Duration Slider, Cost Breakdown, Error, & Buttons
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    bookingControls()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            // Portrait Layout: Single Column vertical flow
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VehicleSummaryCard(car = car, imageHeight = 200.dp)
                bookingControls()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Reusable Vehicle Summary Card supporting adaptive image heights for portrait & landscape.
 */
@Composable
fun VehicleSummaryCard(
    car: Car,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = outlineBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = car.imageResId),
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = car.model,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedBadge {
                    Text(
                        text = "${car.year}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            StarRatingBar(rating = car.rating)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.kms_format, car.kmsDriven),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.cost_format, car.dailyCost),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Reusable Duration & Pricing Card containing the native Slider widget and dynamic balance calculations.
 */
@Composable
fun DurationPricingCard(
    rentalDays: Int,
    onDaysChange: (Int) -> Unit,
    creditBalance: Int,
    totalCost: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = outlineBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.rental_duration_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = if (rentalDays == 1) {
                            stringResource(R.string.duration_day_format, 1)
                        } else {
                            stringResource(R.string.duration_days_format, rentalDays)
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Native Slider widget for selecting rental duration from 1 to 7 days
            Slider(
                value = rentalDays.toFloat(),
                onValueChange = { onDaysChange(it.roundToInt()) },
                valueRange = CarRentalDomain.MIN_RENTAL_DAYS.toFloat()..CarRentalDomain.MAX_RENTAL_DAYS.toFloat(),
                // Discrete stops at every whole day (1..7 → 5 intermediate steps)
                steps = CarRentalDomain.MAX_RENTAL_DAYS - CarRentalDomain.MIN_RENTAL_DAYS - 1,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.slider_min_days),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.slider_max_days),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Cost & Balance Overview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.current_balance_label),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.credits_suffix_format, creditBalance),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_cost_label),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.credits_suffix_format, totalCost),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = if (totalCost > CarRentalDomain.MAX_RENTAL_COST || totalCost > creditBalance) {
                            AccentError
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

/**
 * Reusable banner displaying credit rule validation failures.
 */
@Composable
fun ValidationErrorBanner(
    error: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AccentError.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = AccentError,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            )
        }
    }
}

/**
 * Reusable pair of action buttons (Cancel & Confirm Booking).
 */
@Composable
fun RentalActionButtons(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = outlineBorder(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                stringResource(R.string.cancel_booking),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
        }

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                stringResource(R.string.confirm_booking),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
    }
}
