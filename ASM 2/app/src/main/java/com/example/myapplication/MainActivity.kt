package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Car
import com.example.myapplication.model.SortOption
import com.example.myapplication.ui.CarRentalViewModel
import com.example.myapplication.ui.components.StarRatingBar
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Activity 1: Car Catalogue & Dashboard.
 * Displays available vehicles one at a time with Search, Sorting, Favourites, Rented Cars,
 * Dark/Light Mode switch, and explicit Intent launch to [RentalActivity].
 */
class MainActivity : ComponentActivity() {

    private val viewModel: CarRentalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val userMessage by viewModel.userMessage.collectAsState()

            // Toast feedback for user messages
            LaunchedEffect(userMessage) {
                userMessage?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    viewModel.clearUserMessage()
                }
            }

            // Activity Result API launcher for opening RentalActivity
            val rentalLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    val bookedCarId = data?.getStringExtra(RentalActivity.EXTRA_BOOKED_CAR_ID)
                    val rentalDays = data?.getIntExtra(RentalActivity.EXTRA_RENTAL_DAYS, 0) ?: 0
                    if (bookedCarId != null && rentalDays > 0) {
                        viewModel.processBookingResult(bookedCarId, rentalDays)
                    }
                } else {
                    viewModel.notifyBookingCancelled()
                }
            }

            MyApplicationTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatalogueScreen(
                        viewModel = viewModel,
                        onRentClick = { car ->
                            val intent = Intent(this, RentalActivity::class.java).apply {
                                putExtra(RentalActivity.EXTRA_CAR, car)
                                putExtra(
                                    RentalActivity.EXTRA_CREDIT_BALANCE,
                                    viewModel.creditBalance.value
                                )
                                putExtra(RentalActivity.EXTRA_IS_DARK_MODE, isDarkMode)
                            }
                            rentalLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueScreen(
    viewModel: CarRentalViewModel,
    onRentClick: (Car) -> Unit
) {
    val creditBalance by viewModel.creditBalance.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val favourites by viewModel.favourites.collectAsState()
    val rentedBookings by viewModel.rentedBookings.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val currentCar = viewModel.getCurrentCar()
    val favouriteCars = viewModel.getFavouriteCars()

    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    // Credit Balance Badge
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(stringResource(R.string.balance_format, creditBalance))
                            }
                        },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Spacer(modifier = Modifier.size(24.dp))
                    }

                    // Native Switch for Light/Dark Mode Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Theme Toggle",
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 4.dp)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search & Sort Bar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(R.string.search_hint)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true
                )

                // Sort Selector Button & Dropdown Menu
                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort Vehicles",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.label,
                                        fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    viewModel.setSortOption(option)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Current Active Vehicle Catalogue Card (Shows 1 available car at a time)
            if (currentCar != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Vehicle Image & Favourite Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Image(
                                painter = painterResource(id = currentCar.imageResId),
                                contentDescription = currentCar.name,
                                modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                            )

                            // Heart Icon Favourite Toggle Button
                            val isFav = favourites.contains(currentCar.id)
                            IconButton(
                                onClick = { viewModel.toggleFavourite(currentCar.id) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                        shape = MaterialTheme.shapes.small
                                    )
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favourite",
                                    tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Car Name & Model
                        Text(
                            text = currentCar.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentCar.model,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // RatingBar Widget (1 to 5 stars)
                        StarRatingBar(rating = currentCar.rating)

                        Spacer(modifier = Modifier.height(8.dp))

                        // Vehicle Attributes: Year & Kilometres Driven
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.year_format, currentCar.year),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = stringResource(R.string.kms_format, currentCar.kmsDriven),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Daily Cost
                        Text(
                            text = stringResource(R.string.cost_format, currentCar.dailyCost),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Next & Rent Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Next Button (Cycles through available vehicles)
                            OutlinedButton(
                                onClick = { viewModel.nextCar() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.next_button))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = null
                                )
                            }

                            // Rent Button (Opens RentalActivity)
                            Button(
                                onClick = { onRentClick(currentCar) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(stringResource(R.string.rent_button))
                            }
                        }
                    }
                }
            } else {
                // Empty State Cards
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (searchQuery.isNotEmpty()) {
                                stringResource(R.string.no_search_results)
                            } else {
                                stringResource(R.string.no_available_cars)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.setSearchQuery("") }) {
                                Text("Clear Search")
                            }
                        }
                    }
                }
            }

            // Distinct Favourites Section
            Column {
                Text(
                    text = stringResource(R.string.favourites_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (favouriteCars.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_favourites),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(favouriteCars) { favCar ->
                            val isRented = rentedBookings.any { it.car.id == favCar.id }
                            Card(
                                modifier = Modifier
                                    .width(160.dp)
                                    .clickable {
                                        if (!isRented) {
                                            onRentClick(favCar)
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isRented) {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Image(
                                        painter = painterResource(id = favCar.imageResId),
                                        contentDescription = favCar.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = favCar.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isRented) "Currently Rented" else stringResource(
                                            R.string.cost_format,
                                            favCar.dailyCost
                                        ),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isRented) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Currently Rented Cars Section
            Column {
                Text(
                    text = stringResource(R.string.rented_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (rentedBookings.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_rented_cars),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        rentedBookings.forEach { booking ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = booking.car.imageResId),
                                        contentDescription = booking.car.name,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${booking.car.name} (${booking.car.model})",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = stringResource(
                                                R.string.rental_days_format,
                                                booking.rentalDays
                                            ),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${booking.totalCost} Credits",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        OutlinedButton(
                                            onClick = { viewModel.returnRentedCar(booking.car.id) },
                                            modifier = Modifier.padding(top = 4.dp)
                                        ) {
                                            Text("Return", style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
