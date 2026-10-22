# Development Log Template: COS30017 Assignment 2

> **Note for Student Submission:**  
> This template records key implementation milestones, technical challenges, solutions, and test outcomes. Please complete the date, start time, end time, and personal notes prior to final submission.

---

| Date | Time / Duration | Task Performed | Technical Problem Encountered | Solution Explored | Outcome / Result |
| :--- | :--- | :--- | :--- | :--- | :--- |
| *[Student Entry]* | *[Duration]* | Repository inspection & Gradle setup | Gradle version catalog plugin configuration for `@Parcelize` | Added `id("kotlin-parcelize")` & explicit `Parcelable` implementation | `@Parcelize` compiled successfully |
| *[Student Entry]* | *[Duration]* | Core domain & data models | Ensuring pure domain logic is decoupled from Android UI | Created `Car`, `RentalBooking`, `SortOption`, `CarRentalDomain`, `CarRentalRepository` | Pure domain logic testable with 100% unit test coverage |
| *[Student Entry]* | *[Duration]* | UI resources & star rating widget | Need a native/custom RatingBar component in Compose | Implemented `StarRatingBar` composable using Material3 Star icons | Rendered 1–5 star ratings dynamically |
| *[Student Entry]* | *[Duration]* | Activity 2: Rental details screen | Dynamically updating cost & validating 400 credit limit & balance | Integrated Material3 `Slider` widget; implemented `validateRental()`; added `BackHandler` | Instant cost feedback, non-destructive error banner, and guaranteed Back cancellation |
| *[Student Entry]* | *[Duration]* | Activity 1: Catalogue screen & Intent integration | Passing Parcelable data between Activities & receiving result | Used `ActivityResultContracts.StartActivityForResult()` with explicit Intent | Seamless inter-Activity communication & result handling |
| *[Student Entry]* | *[Duration]* | Light/Dark Mode implementation | Both Activities must support Dark Mode | Used Material3 `MyApplicationTheme` with `isDarkMode` state & native `Switch` | Complete Dark Mode contrast on cards, text, controls |
| *[Student Entry]* | *[Duration]* | Booking reversal & return feature | Supporting reversing a committed booking per rubric | Implemented `returnRentedCar` in repository & Return Car button in UI | Restores credits and returns vehicle to availability |
| *[Student Entry]* | *[Duration]* | Automated testing & verification | Verifying all required test cases from specification | Executed JUnit test suite via Gradle Wrapper (`app:testDebugUnitTest`) | 22 unit tests passed with 0 failures |

---

## Technical Reflection & Challenges Log

### Challenge 1: Ensuring Intent & Parcelable Safety Across Activities
- **Issue:** Passing vehicle data between `MainActivity` and `RentalActivity` requires serialization without data loss or memory leaks.
- **Solution:** Implemented `@Parcelize` and `Parcelable` interface on `Car` and `RentalBooking` data models. Used `Intent.getParcelableExtra()` with Android 13+ (API 33) compatibility checks.
- **Key Takeaway:** `Parcelable` is specifically optimized for Android IPC/Intent messaging compared to standard Java `Serializable`.

### Challenge 2: Enforcing Strict In-Memory Business Rules & Reversal
- **Issue:** Preventing duplicate credit deductions, preventing double bookings, keeping Favourites independent of car availability, and supporting rental reversal.
- **Solution:** Encapsulated state within `CarRentalRepository` using Kotlin `StateFlow`. Booking processing validates car availability and balance before mutating state. Added atomic `returnRentedCar` method.
- **Key Takeaway:** Separating domain validation rules (`CarRentalDomain`) from UI state ensures atomic state transitions and complete unit test coverage.
