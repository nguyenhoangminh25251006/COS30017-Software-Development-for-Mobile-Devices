package com.example.myapplication

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Car
import com.example.myapplication.model.RentalBooking
import com.example.myapplication.model.SortOption
import com.example.myapplication.ui.CarRentalViewModel
import com.example.myapplication.ui.components.AppLocaleProvider
import com.example.myapplication.ui.components.OutlinedBadge
import com.example.myapplication.ui.components.SectionTitle
import com.example.myapplication.ui.components.StarRatingBar
import com.example.myapplication.ui.components.outlineBorder
import com.example.myapplication.ui.theme.AccentFavorite
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Activity 1: Car Rental Dashboard.
 * Clean, modern editorial automotive UI with horizontal swipeable vehicle cards,
 * real-time search & sorting, favourites tracking, active bookings management,
 * adaptive portrait/landscape layout, and bilingual language switching (English / Vietnamese).
 */
class MainActivity : ComponentActivity() {

    private val viewModel: CarRentalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val appLanguage by viewModel.appLanguage.collectAsState()
            val localizedToast by viewModel.localizedToast.collectAsState()

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

            AppLocaleProvider(language = appLanguage) {
                // LocalContext here is already localized, so toasts follow the in-app language
                val context = LocalContext.current
                LaunchedEffect(localizedToast) {
                    localizedToast?.let { toast ->
                        val text = context.getString(toast.resId, *toast.formatArgs.toTypedArray())
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                        viewModel.clearToast()
                    }
                }

                MyApplicationTheme(darkTheme = isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CatalogueScreen(
                            viewModel = viewModel,
                            isDarkMode = isDarkMode,
                            appLanguage = appLanguage,
                            onRentClick = { car ->
                                val intent = Intent(this, RentalActivity::class.java).apply {
                                    putExtra(RentalActivity.EXTRA_CAR, car)
                                    putExtra(
                                        RentalActivity.EXTRA_CREDIT_BALANCE,
                                        viewModel.creditBalance.value
                                    )
                                    putExtra(RentalActivity.EXTRA_IS_DARK_MODE, isDarkMode)
                                    putExtra(RentalActivity.EXTRA_LANGUAGE, appLanguage)
                                }
                                rentalLauncher.launch(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueScreen(
    viewModel: CarRentalViewModel,
    isDarkMode: Boolean,
    appLanguage: String,
    onRentClick: (Car) -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val creditBalance by viewModel.creditBalance.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val favourites by viewModel.favourites.collectAsState()
    val rentedBookings by viewModel.rentedBookings.collectAsState()
    val availableCars by viewModel.availableCars.collectAsState()
    val favouriteCars by viewModel.favouriteCars.collectAsState()
    val currentCarIndex by viewModel.currentCarIndex.collectAsState()

    // Persist UI controls across orientation changes
    var showLanguageMenu by rememberSaveable { mutableStateOf(false) }

    // Sections shared by the portrait and landscape layouts
    val creditCard: @Composable (Modifier) -> Unit = { modifier ->
        AccountCreditBalanceCard(
            creditBalance = creditBalance,
            availableCarsCount = availableCars.size,
            modifier = modifier
        )
    }
    val searchControls: @Composable (Modifier) -> Unit = { modifier ->
        SearchAndSortControls(
            searchQuery = searchQuery,
            onSearchQueryChange = viewModel::setSearchQuery,
            onClearSearch = { viewModel.setSearchQuery("") },
            sortOption = sortOption,
            onSortOptionChange = viewModel::setSortOption,
            modifier = modifier
        )
    }
    val popularCars: @Composable () -> Unit = {
        PopularCarsSection(
            availableCars = availableCars,
            currentCarIndex = currentCarIndex,
            favourites = favourites,
            isLandscape = isLandscape,
            sortOption = sortOption,
            searchQuery = searchQuery,
            onCarIndexChange = viewModel::setCurrentCarIndex,
            onToggleFavourite = viewModel::toggleFavourite,
            onRentClick = onRentClick,
            onClearSearch = { viewModel.setSearchQuery("") }
        )
    }
    val favouritesSection: @Composable (Modifier) -> Unit = { modifier ->
        FavouritesSection(
            favouriteCars = favouriteCars,
            rentedBookings = rentedBookings,
            onRentClick = onRentClick,
            modifier = modifier
        )
    }
    val rentedSection: @Composable (Modifier) -> Unit = { modifier ->
        RentedCarsSection(
            rentedBookings = rentedBookings,
            onReturnClick = viewModel::returnRentedCar,
            modifier = modifier
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    // Language Switcher Dropdown (EN / VI)
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = outlineBorder(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showLanguageMenu = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = stringResource(R.string.language_title),
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = appLanguage.uppercase(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false }
                        ) {
                            listOf(
                                "en" to R.string.language_en,
                                "vi" to R.string.language_vi
                            ).forEach { (code, labelRes) ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(labelRes)) },
                                    onClick = {
                                        viewModel.setAppLanguage(code)
                                        showLanguageMenu = false
                                    },
                                    trailingIcon = if (appLanguage == code) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null
                                )
                            }
                        }
                    }

                    // Minimalist Dark / Light Mode Switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = stringResource(R.string.theme_toggle_desc),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 6.dp)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = if (isLandscape) 20.dp else 16.dp, vertical = 6.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(if (isLandscape) 16.dp else 18.dp)
        ) {
            if (isLandscape) {
                // Adaptive Landscape Layout: side-by-side header & dual-pane bottom dashboard
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    creditCard(Modifier.weight(0.42f))
                    searchControls(Modifier.weight(0.58f))
                }
                popularCars()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    favouritesSection(Modifier.weight(1f))
                    rentedSection(Modifier.weight(1f))
                }
            } else {
                // Portrait Layout: single column vertical flow
                creditCard(Modifier)
                searchControls(Modifier)
                popularCars()
                favouritesSection(Modifier)
                rentedSection(Modifier)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Reusable Account Credit Balance Card.
 */
@Composable
fun AccountCreditBalanceCard(
    creditBalance: Int,
    availableCarsCount: Int,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.available_credits_label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$creditBalance",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            OutlinedBadge {
                Text(
                    text = stringResource(R.string.cars_ready_format, availableCarsCount),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Reusable Search Field and Sort Filter Chips.
 */
@Composable
fun SearchAndSortControls(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    sortOption: SortOption,
    onSortOptionChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Minimalist Monochromatic Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.search_hint),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(14.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true
        )

        // Monochromatic Quick Sort Filter Chips (one per SortOption)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sort_prefix),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SortOption.entries.forEach { option ->
                val selected = sortOption == option
                FilterChip(
                    selected = selected,
                    onClick = { onSortOptionChange(option) },
                    label = {
                        Text(
                            stringResource(option.labelRes),
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        enabled = true,
                        selected = selected
                    )
                )
            }
        }
    }
}

/**
 * Reusable Horizontal Swipeable Popular Vehicles Section.
 * Features clean swipeable vehicle cards and counter badge, supporting adaptive sizing for landscape mode.
 */
@Composable
fun PopularCarsSection(
    availableCars: List<Car>,
    currentCarIndex: Int,
    favourites: Set<String>,
    isLandscape: Boolean,
    sortOption: SortOption,
    searchQuery: String,
    onCarIndexChange: (Int) -> Unit,
    onToggleFavourite: (String) -> Unit,
    onRentClick: (Car) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (availableCars.isEmpty()) {
            SectionTitle(text = stringResource(R.string.section_popular), fontSize = 20.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = outlineBorder()
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            stringResource(R.string.no_search_results)
                        } else {
                            stringResource(R.string.no_available_cars)
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (searchQuery.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onClearSearch,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(stringResource(R.string.clear_search))
                        }
                    }
                }
            }
        } else {
            // A new sort or search recreates the list state, so the carousel starts from the first car.
            // On rotation the keys are unchanged and the state starts at the saved car index.
            key(sortOption, searchQuery) {
                val popularListState = rememberLazyListState(
                    initialFirstVisibleItemIndex = currentCarIndex.coerceIn(0, availableCars.lastIndex)
                )

                // Keep view model in sync with user scrolling
                LaunchedEffect(popularListState) {
                    snapshotFlow { popularListState.firstVisibleItemIndex }
                        .collect { onCarIndexChange(it) }
                }

                // Header with Section Title and Vehicle Counter Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(text = stringResource(R.string.section_popular), fontSize = 20.sp)

                    OutlinedBadge {
                        Text(
                            text = "${(popularListState.firstVisibleItemIndex + 1).coerceAtMost(availableCars.size)} / ${availableCars.size}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Full-width Horizontal Swipeable vehicle cards
                LazyRow(
                    state = popularListState,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(end = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(availableCars, key = { it.id }) { car ->
                        PopularCarCard(
                            car = car,
                            isFavourite = car.id in favourites,
                            isLandscape = isLandscape,
                            onToggleFavourite = { onToggleFavourite(car.id) },
                            onRentClick = { onRentClick(car) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reusable Favourites Section.
 */
@Composable
fun FavouritesSection(
    favouriteCars: List<Car>,
    rentedBookings: List<RentalBooking>,
    onRentClick: (Car) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.favourites_count_title, favouriteCars.size))

        Spacer(modifier = Modifier.height(10.dp))

        if (favouriteCars.isEmpty()) {
            Text(
                text = stringResource(R.string.no_favourites),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(favouriteCars, key = { it.id }) { favCar ->
                    val isRented = rentedBookings.any { it.car.id == favCar.id }
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .clickable(enabled = !isRented) { onRentClick(favCar) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = outlineBorder()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Image(
                                painter = painterResource(id = favCar.imageResId),
                                contentDescription = favCar.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(95.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = favCar.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isRented) {
                                    stringResource(R.string.currently_rented)
                                } else {
                                    stringResource(R.string.cost_format, favCar.dailyCost)
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                ),
                                color = if (isRented) AccentFavorite else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable Currently Rented Cars Section.
 */
@Composable
fun RentedCarsSection(
    rentedBookings: List<RentalBooking>,
    onReturnClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.rented_count_title, rentedBookings.size))

        Spacer(modifier = Modifier.height(10.dp))

        if (rentedBookings.isEmpty()) {
            Text(
                text = stringResource(R.string.no_rented_cars),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rentedBookings.forEach { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = outlineBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = booking.car.imageResId),
                                contentDescription = booking.car.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = booking.car.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.rental_days_format, booking.rentalDays),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.credits_total_format, booking.totalCost),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            OutlinedButton(
                                onClick = { onReturnClick(booking.car.id) },
                                shape = RoundedCornerShape(8.dp),
                                border = outlineBorder(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(
                                    stringResource(R.string.return_button),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modern Popular vehicle card matching editorial automotive reference design.
 * Features large vehicle image (adaptive height for portrait/landscape), heart favourite toggle,
 * vehicle specs (model, year, kilometres), rating stars, and pill 'Rent' action button.
 */
@Composable
fun PopularCarCard(
    car: Car,
    isFavourite: Boolean,
    isLandscape: Boolean = false,
    onToggleFavourite: () -> Unit,
    onRentClick: () -> Unit
) {
    val cardWidth = if (isLandscape) 300.dp else 280.dp
    val imageHeight = if (isLandscape) 140.dp else 180.dp

    Card(
        modifier = Modifier
            .width(cardWidth)
            .clickable { onRentClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = outlineBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Vehicle Image Box with top-right floating Heart Icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = car.imageResId),
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Heart Favourite Toggle Button (translucent backdrop follows the theme)
                IconButton(
                    onClick = onToggleFavourite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favourite_desc),
                        tint = if (isFavourite) AccentFavorite else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Card Body Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Title and Daily Cost
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        val costStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.cost_credits_slash, car.dailyCost),
                            style = costStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.day_unit),
                            style = costStyle,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Vehicle Specifications: Model, Year, Kilometres
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    CarSpecRow(Icons.Default.DirectionsCar, stringResource(R.string.model_format, car.model))
                    CarSpecRow(Icons.Default.CalendarToday, stringResource(R.string.year_format, car.year))
                    CarSpecRow(Icons.Default.Speed, stringResource(R.string.kms_label_format, car.kmsDriven))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Row: Star Rating & Pill Rent Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.rating_prefix),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        StarRatingBar(rating = car.rating, starSize = 13.dp)
                    }

                    Button(
                        onClick = onRentClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.rent_action),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single icon + label line in the vehicle card specification list.
 */
@Composable
private fun CarSpecRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
