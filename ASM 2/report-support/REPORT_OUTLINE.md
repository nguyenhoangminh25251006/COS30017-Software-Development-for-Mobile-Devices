# Report Outline & Structure Guide (~10–13 Pages)

This outline provides a comprehensive page-by-page structure for writing the final assignment report for COS30017 Assignment 2 ("Rent a Car with Intent"), targeting the Excellent / High Distinction rubric level.

---

## Document Structure & Page Allocation Summary

| Section | Title | Target Length |
| :---: | :--- | :---: |
| **Cover Page** | Title, Student Name, ID, Course Details | 1 Page |
| **1** | Introduction, Problem Statement & User Stories | 1.5 Pages |
| **2** | System Architecture & In-Memory Data Model | 1.5 Pages |
| **3** | Activity Communication: Explicit Intents & Parcelable | 2 Pages |
| **4** | UI/UX Design, Custom Widgets & Layout Alternatives | 2 Pages |
| **5** | Business Rules, Validation & Error Handling | 1.5 Pages |
| **6** | Testing Strategy, Execution & Verification Evidence | 2 Pages |
| **7** | Reflection, Challenges & Future Work | 1 Page |
| **Appendix**| GenAI Disclosure & Prompt Artifacts | 1 Page |
| **Total** | | **~12–13 Pages** |

---

## Detailed Section-by-Section Outline

### Cover Page
- Project Title: **DriveEase Car Rental — Proof-of-Concept Android Application**
- Course: COS30017 — Software Development for Mobile Devices
- Student Name, Student ID, Submission Date.

---

### Section 1: Introduction, User Stories & Use Cases
#### 1.1 Overview
- High-level overview of DriveEase car rental proof-of-concept application.
- Purpose: Demonstrate Android inter-Activity communication using explicit `Intent`s, `Parcelable` data passing, Activity Result API, native Android widgets, and in-memory business logic validation.

#### 1.2 User Stories & Use Cases
- **User Story 1 (Browse & Filter Catalogue):**
  - *As a* customer looking to rent a vehicle, *I want to* search vehicles by name/model, sort them by rating/year/cost, and cycle through available cars, *so that I can* quickly find a vehicle that suits my budget and preferences.
  - *Use Case 1:* Primary flow for searching "Tesla", applying "Daily Cost: Lowest to Highest" sorting, and toggling a vehicle as favourite.
- **User Story 2 (Configure & Book Rental):**
  - *As a* registered user with credits, *I want to* select a car, adjust rental duration using a slider, view dynamic total cost calculations, and confirm my booking, *so that I can* reserve a car without exceeding my available balance or the 400 credit limit.
  - *Use Case 2:* Primary flow for clicking "Rent", adjusting duration slider from 1 to 3 days, observing dynamic cost update from 150 to 450 credits, receiving a validation error when exceeding 400 credits, adjusting back to 2 days, and confirming the booking.

---

### Section 2: Architecture & In-Memory Data Model
#### 2.1 Architecture Overview
- Discussion of Android MVVM-like domain-driven architecture separating UI (`CatalogueScreen`, `RentalScreen`), State (`CarRentalViewModel`), Data Repository (`CarRentalRepository`), and Domain Rules (`CarRentalDomain`).
- Rationale for pure in-memory data management (no `SharedPreferences`/`Room`/`DataStore`) as specified by assignment rules. Explain how process lifecycle affects state.

#### 2.2 Data Models
- Explanation of `Car` data class attributes: `id`, `name`, `model`, `year`, `rating`, `kmsDriven`, `dailyCost`, `imageResId`.
- Explanation of `RentalBooking` data class: `car`, `rentalDays`, `totalCost`.
- Explanation of `SortOption` enum: `RATING_DESC`, `YEAR_DESC`, `COST_ASC`.

---

### Section 3: Activity Communication & Intent Architecture
#### 3.1 Explicit Intent Components
- Detailed technical explanation of explicit `Intent` in Android:
  - Component Name: `Intent(context, RentalActivity::class.java)`
  - Extras Bundle: Carrying `Car` (`EXTRA_CAR`), initial credit balance (`EXTRA_CREDIT_BALANCE`), and theme setting (`EXTRA_IS_DARK_MODE`).
  - Action & Category implicit vs. explicit comparison.

#### 3.2 Parcelable Serialization
- Detailed technical explanation of `@Parcelize` and `Parcelable` in Kotlin:
  - Why `Parcelable` is specifically engineered for Android IPC memory efficiency compared to standard Java `Serializable` (avoids reflection overhead).
  - Explanation of `writeToParcel(dest, flags)` and `createFromParcel(parcel)` mechanics.
  - Android 13 (API 33) type-safe `getParcelableExtra(key, Class)` compatibility handling.

#### 3.3 Activity Result API
- Explanation of `ActivityResultContracts.StartActivityForResult()`:
  - Replacing deprecated `startActivityForResult()` / `onActivityResult()`.
  - How `RentalActivity` returns `Activity.RESULT_OK` with result extras (`EXTRA_BOOKED_CAR_ID`, `EXTRA_RENTAL_DAYS`, `EXTRA_TOTAL_COST`) or `Activity.RESULT_CANCELED`.
  - How `MainActivity` processes the returned result atomically without duplicate credit deductions.

---

### Section 4: UI/UX Quality, Native Widgets & Layout Alternatives
#### 4.1 Native Widget Implementation
- Discussion of widget choices satisfying requirements:
  - **RatingBar Widget:** Custom reusable `StarRatingBar` composable displaying 1 to 5 stars with gold fill and numerical rating label.
  - **Slider Widget:** Native Material3 `Slider` (1 to 7 discrete day steps) for duration selection.
  - **Switch Widget:** Native Material3 `Switch` for Light/Dark Mode toggle.

#### 4.2 Reusable Styles & Dark Mode
- Explanation of reusable XML styles in `styles.xml`: `CardHeaderTextStyle`, `PrimaryActionButtonStyle`, `VehicleBadgeStyle`.
- Demonstration of Dark/Light theme contrast on both Activities for accessibility and legibility.

#### 4.3 Alternative Layout Wireframes & Design Justification
- **Layout Alternative A (Current Single-Vehicle Detailed Focus):**
  - Single large vehicle card with Next button navigation, detailed attributes, rating bar, heart favourite toggle, and prominent Rent button.
  - *Trade-offs:* High visual detail and focus per vehicle; requires sequential stepping through cars.
- **Layout Alternative B (Compact Grid Catalogue Overview):**
  - Multi-column grid displaying thumbnail cards of all available vehicles simultaneously with quick-rent buttons.
  - *Trade-offs:* Faster comparison across all vehicles; reduced visual real estate per vehicle details.
- *Justification:* Single-vehicle focus was selected to align directly with course specification requiring explicit Next button stepping.

---

### Section 5: Business Rules & Validation Engine
#### 5.1 Business Rules Engine
- Initial balance: 500 credits.
- Duration rule: 1 to 7 days (`MIN_RENTAL_DAYS`..`MAX_RENTAL_DAYS`).
- Individual rental cap: Maximum 400 credits per rental (`MAX_RENTAL_COST`).
- Credit balance rule: Total rental cost <= user's current credit balance.
- Duplicate booking prevention: Same vehicle cannot be rented twice.
- Reversal of committed booking: Returning a rented car restores credits and vehicle availability.

#### 5.2 Error Handling & User Feedback
- Demonstration of non-destructive validation on `RentalActivity`: when validation fails, error banner is displayed on screen, keeping the user on `RentalActivity` so they can adjust duration or cancel.
- Toast feedback on `MainActivity` for confirmed booking or cancellation notification.

---

### Section 6: Testing Strategy & Verification Evidence
#### 6.1 Unit Testing Suite
- Overview of 22 JUnit unit tests in `CarRentalDomainTest` and `CarRentalRepositoryTest`.
- Table mapping test cases to specification requirements (cost calculation, 1–7 days boundaries, 400 credits cap, balance checks, duplicate prevention, cancellation safety, booking reversal, sorting immutability).

#### 6.2 UI & Integration Testing Suite
- Overview of instrumentation tests in `CarRentalUITest` verifying app launch, Next navigation, search filtering, and favourite selection.

#### 6.3 Screenshot Map & Captions
- *Screenshot 1:* `MainActivity` Light Mode showing Toyota Camry, 500 Credits, Search bar, and Favourites section.
- *Screenshot 2:* `MainActivity` Dark Mode demonstrating theme toggle contrast.
- *Screenshot 3:* `MainActivity` displaying filtered search result for "Tesla".
- *Screenshot 4:* `RentalActivity` displaying Tesla Model 3, Slider set to 3 days, total cost 225 credits.
- *Screenshot 5:* `RentalActivity` displaying 400 credit limit validation error banner when duration exceeds maximum cost.
- *Screenshot 6:* `MainActivity` showing updated credit balance (275 credits) and Rented Vehicles section following successful booking.

---

### Section 7: Reflection, Explorations & Conclusion
#### 7.1 Reflection Prompts for Student
- Discuss personal learning outcomes regarding Android explicit Intents and `Parcelable` data passing.
- Reflections on Jetpack Compose state management with Kotlin `StateFlow`.
- Reflections on Android testing methodologies (unit tests vs. UI instrumentation tests).

---

### Appendix: GenAI Disclosure
- Truthful disclosure statement regarding Generative AI assistance during development (see `GENAI_DISCLOSURE.md`).
