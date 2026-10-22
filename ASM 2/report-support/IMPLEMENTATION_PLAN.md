# Implementation Plan: COS30017 Assignment 2 — Rent a Car with Intent

## 1. Overview & Objectives

This document maps the assignment requirements and rubric criteria for COS30017 Assignment 2 ("Rent a Car with Intent") to the actual technical implementation within the Android project repository.

The goal is a proof-of-concept car rental Android application demonstrating explicit `Intent` messaging, `Parcelable` data passing, Activity Result API result handling, state management with in-memory persistence, and robust input validation.

---

## 2. Requirements-to-Implementation Mapping

| Requirement Category | Specific Assignment Requirement | Implementation Details | Location in Codebase |
| :--- | :--- | :--- | :--- |
| **Activity 1: Catalogue** | Single car display with Next navigation | Display one available car at a time in a styled Material3 `Card`. Next button cycles through available vehicles using modular indexing. | `MainActivity.kt` (`CatalogueScreen`) |
| **Dataset & Images** | 5 distinct sample cars with realistic data & free local images | 5 vector drawables (`car_toyota_camry`, `car_tesla_model3`, `car_hyundai_tucson`, `car_bmw_3series`, `car_honda_civic`) with name, model, year, rating, kms, daily cost. Royalty-free, locally bundled. | `CarRentalRepository.kt`, `res/drawable/*.xml` |
| **Search & Sort** | Native search by name/model & 3-way sorting | `OutlinedTextField` for search query filtering; `DropdownMenu` supporting Rating (high-low), Year (new-old), Cost (low-high). | `CarRentalDomain.kt`, `MainActivity.kt` |
| **State & Sections** | Balance display, Favourites, Rented Cars, Light/Dark mode | 500 initial credits in top bar badge; Heart icon toggle & horizontal favourites list; Rented vehicles section with cost/days; Native `Switch` for theme. | `CarRentalRepository.kt`, `MainActivity.kt` |
| **Activity 2: Rental** | Explicit Intent, Parcelable passing & Activity Result API | `RentalActivity` launched via `ActivityResultContracts.StartActivityForResult()`, passing `Car` Parcelable object. Returns result extras on Save/Cancel. | `RentalActivity.kt`, `MainActivity.kt` |
| **Duration & Cost** | Native `Slider` widget for 1–7 days & dynamic cost calculation | Material3 `Slider` (1 to 7 step increments). `totalCost = dailyCost * rentalDays` calculated instantly on state change. | `RentalActivity.kt` (`RentalScreen`) |
| **Validation Rules** | Duration 1–7 days, <= 400 credit limit, <= current balance | Pure domain validation function `validateRental()`. Displays actionable error banner on failure without closing screen or deducting balance. | `CarRentalDomain.kt`, `RentalActivity.kt` |
| **In-Memory Storage** | Pure in-memory state, no DB/Preferences/Files | Repository uses Kotlin `StateFlow` in memory. Process restart resets state. Favourites remain independent of car availability. Supports returning rented vehicles (`returnRentedCar`). | `CarRentalRepository.kt` |
| **Architecture & Quality** | Domain separation, `@Parcelize`, 2+ reusable styles | Clean separation between domain rules (`CarRentalDomain`), state repo (`CarRentalRepository`), ViewModel, and Compose UI. XML styles in `styles.xml`. | `domain/`, `data/`, `model/`, `res/values/styles.xml` |

---

## 3. Vehicle Dataset & Image Licensing Documentation

Per assignment requirements, all vehicle graphics are locally bundled, offline, royalty-free Android Vector Drawables:
- `car_toyota_camry.xml`: Custom vector illustration (Public Domain / CC0 equivalent).
- `car_tesla_model3.xml`: Custom vector illustration (Public Domain / CC0 equivalent).
- `car_hyundai_tucson.xml`: Custom vector illustration (Public Domain / CC0 equivalent).
- `car_bmw_3series.xml`: Custom vector illustration (Public Domain / CC0 equivalent).
- `car_honda_civic.xml`: Custom vector illustration (Public Domain / CC0 equivalent).

No remote runtime HTTP dependencies, no commercial manufacturer photography, and no copyright violations are present.

---

## 4. Ordered Milestone Tasks

```
[Milestone 1] Model & Domain Core
  ├── Create Car data class (@Parcelize & Parcelable)
  ├── Create RentalBooking & SortOption models
  ├── Implement CarRentalDomain (validation, calculation, filtering, sorting)
  └── Implement CarRentalRepository (in-memory StateFlow manager, returnRentedCar)

[Milestone 2] UI Resources & Themes
  ├── Create 5 custom SVG/Vector Car Drawables
  ├── Define user-facing strings in res/values/strings.xml
  ├── Create reusable XML styles in res/values/styles.xml
  └── Implement reusable StarRatingBar composable widget

[Milestone 3] Activity 2: Rental Details Screen
  ├── Implement RentalActivity layout & Scaffold
  ├── Integrate native Slider widget (1 to 7 days)
  ├── Implement dynamic cost calculator & error banner
  ├── Integrate BackHandler for hardware/gesture back cancellation
  └── Implement Save (RESULT_OK) & Cancel (RESULT_CANCELED) handlers

[Milestone 4] Activity 1: Catalogue Main Screen
  ├── Implement MainActivity top bar, balance badge, Light/Dark mode switch
  ├── Implement Search bar & 3-way Sorting dropdown
  ├── Implement single car card, Next button, Rent button, Favourite heart toggle
  ├── Integrate ActivityResultLauncher for opening RentalActivity
  ├── Implement Favourites row & Currently Rented Vehicles section
  └── Add Return Car functionality to reverse committed bookings

[Milestone 5] Verification & Automated Testing
  ├── Unit tests (22 tests in CarRentalDomainTest & CarRentalRepositoryTest)
  ├── Instrumentation UI tests (CarRentalUITest)
  └── Gradle build verification (:app:assembleDebug)
```
