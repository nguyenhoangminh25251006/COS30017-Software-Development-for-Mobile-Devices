# Test Plan & Results Log: COS30017 Assignment 2

## 1. Overview

This document details the test strategy, unit test suite, UI integration test suite, and manual verification results for the DriveEase Car Rental application.

---

## 2. Automated Unit Test Cases (`app/src/test/`)

| Test Case ID | Test Category | Preconditions | Test Steps | Expected Result | Actual Result & Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **UT-01** | Cost Calculation | Daily cost = 50 credits | Calculate total cost for 1 day and 7 days. | 1 day = 50; 7 days = 350. | **PASSED** |
| **UT-02** | Cost Calculation | Daily cost = 90 credits | Calculate total cost for 7 days. | 7 days = 630. | **PASSED** |
| **UT-03** | Boundary Validation | Balance = 500 | Validate rental for 1 day and 7 days. | `ValidationResult.Success` for both. | **PASSED** |
| **UT-04** | Boundary Validation | Balance = 500 | Validate rental for 0 days and 8 days. | `ValidationResult.Error` for both. | **PASSED** |
| **UT-05** | Credit Limit Rule | Daily cost = 80 credits | Validate 5 days (80 * 5 = 400 credits). | `ValidationResult.Success` (exactly 400 limit). | **PASSED** |
| **UT-06** | Credit Limit Rule | Daily cost = 90 credits | Validate 5 days (90 * 5 = 450 credits). | `ValidationResult.Error` (exceeds 400 limit). | **PASSED** |
| **UT-07** | Balance Check | Balance = 250 | Validate 4 days at 50 credits/day (200 total). | `ValidationResult.Success`. | **PASSED** |
| **UT-08** | Balance Check | Balance = 150 | Validate 4 days at 50 credits/day (200 total). | `ValidationResult.Error` ("Insufficient credit"). | **PASSED** |
| **UT-09** | Duplicate Check | Car already rented | Validate rental on a car marked as rented. | `ValidationResult.Error` ("Already rented"). | **PASSED** |
| **UT-10** | Sorting Order | 5 sample vehicles | Sort by Rating Descending (`RATING_DESC`). | Tesla (4.9) -> Toyota (4.8) -> BMW (4.7) -> Hyundai (4.5) -> Honda (4.3). | **PASSED** |
| **UT-11** | Sorting Order | 5 sample vehicles | Sort by Year Descending (`YEAR_DESC`). | BMW (2024) -> Tesla (2023) -> Toyota (2022) -> Hyundai (2021) -> Honda (2020). | **PASSED** |
| **UT-12** | Sorting Order | 5 sample vehicles | Sort by Daily Cost Ascending (`COST_ASC`). | Honda (35) -> Hyundai (45) -> Toyota (50) -> Tesla (75) -> BMW (90). | **PASSED** |
| **UT-13** | Immutability Check | 5 sample vehicles | Perform sorting operation on sample cars. | Canonical dataset list remains unmutated. | **PASSED** |
| **UT-14** | Search Filtering | 5 sample vehicles | Search for "Toyota", "Long Range", "bmw", "Ferrari". | Correct matching vehicles returned; empty list for "Ferrari". | **PASSED** |
| **UT-15** | State Manager | Initial repo creation | Inspect initial repository state. | Balance = 500, available cars = 5, rented = 0. | **PASSED** |
| **UT-16** | Booking Workflow | Balance = 500 | Process booking for Toyota Camry for 3 days (150 credits). | Balance = 350; available cars = 4; rented = 1. | **PASSED** |
| **UT-17** | Duplicate Prevention | Balance = 500 | Process booking twice for Toyota Camry. | First succeeds; second fails; balance deducted only once (350). | **PASSED** |
| **UT-18** | Cancellation Rule | Balance = 500 | Simulate opening rental screen and cancelling. | Balance remains 500; no credits deducted. | **PASSED** |
| **UT-19** | Favourites Rule | Vehicle in repository | Toggle favourite; rent vehicle. | Vehicle removed from available list BUT remains in favourites list. | **PASSED** |
| **UT-20** | Next Navigation | Available vehicles > 1 | Call `nextCar()`. | `currentCarIndex` advances from 0 to 1. | **PASSED** |
| **UT-21** | State Reset | Rented cars & balance updated | Call `resetState()`. | Balance restored to 500; favourites cleared; available = 5. | **PASSED** |
| **UT-22** | Return Rented Car | Car is rented; balance = 350 | Call `returnRentedCar(carId)`. | Car returned to catalogue; balance restored to 500; rented list empty. | **PASSED** |

---

## 3. Automated Instrumentation UI Test Cases (`app/src/androidTest/`)

| Test Case ID | Test Category | Preconditions | Test Steps | Expected Result | Actual Result & Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **UI-01** | App Launch | App launched on device/emulator | Observe initial UI screen elements. | Top bar displays title, credits badge (500), and top vehicle card. | **READY / VERIFIED CODE** |
| **UI-02** | Next Navigation | Available cars present | Tap "Next Car" button. | Current car updates to next vehicle in list. | **READY / VERIFIED CODE** |
| **UI-03** | Search Query | Catalogue screen visible | Type "Tesla" into search field. | Tesla Model 3 card displayed; Toyota card hidden. | **READY / VERIFIED CODE** |
| **UI-04** | Favourite Toggle | Catalogue screen visible | Tap Heart icon on current car card. | Heart icon filled; vehicle added to Favourites row. | **READY / VERIFIED CODE** |

*Note on UI Tests Execution:* The UI test cases are implemented using AndroidX Compose Test Rule (`app/src/androidTest/java/com/example/myapplication/CarRentalUITest.kt`). They require an active running emulator/device. When a physical or emulator device (`Pixel_9`) is attached, run `./gradlew connectedDebugAndroidTest` to execute them.

---

## 4. Execution Summary

- **Total Unit Tests Executed:** 22
- **Passed:** 22
- **Failed:** 0
- **Build Status:** SUCCESSFUL (`app:testDebugUnitTest` executed via Gradle Wrapper)
- **Compilation Status:** SUCCESSFUL (`app:assembleDebug` executed via Gradle Wrapper)
