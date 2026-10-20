# Generative AI Disclosure Statement

## 1. Overview & University Academic Integrity Compliance

This document provides a complete and transparent disclosure of the Generative Artificial Intelligence (GenAI) tools, models, and assistance utilized during the analysis, architecture, implementation, and testing of **COS30017 Assignment 2: Rent a Car with Intent (DriveEase Car Rental Proof-of-Concept)**.

In accordance with Swinburne University of Technology's Academic Integrity Policy and course assessment regulations:
- The student acknowledges the use of generative AI as an assistive development and pair-programming tool.
- AI was used for architectural guidance, Kotlin/Compose implementation, unit test structuring, and technical documentation drafting.
- All generated code and documentation have been critically reviewed, inspected, debugged, executed, and validated by the student on the local development environment.
- The student maintains full intellectual ownership, accountability, and comprehension of the submitted work.

---

## 2. Tools & Models Utilized

| Attribute | Details |
| :--- | :--- |
| **Tool / Interface** | Google Antigravity IDE / Pair-Programming Assistant |
| **Model** | Gemini 3.8 Flash |
| **Context & Scope** | Android Studio project codebase, Gradle build configuration, Kotlin 2.2.10, Jetpack Compose, Material 3, Android Activity Result API, JUnit 4 unit tests, AndroidX Compose UI instrumentation tests |
| **Date of Use** | October 2026 |

---

## 3. Nature & Scope of AI Assistance

Generative AI was employed across the following development phases:

### Phase 1: Repository Inspection & Architectural Planning
- **Prompt Purpose:** Inspect existing project structure, Gradle build files, Kotlin version, and dependencies; establish an implementation plan mapped directly to the assignment rubric.
- **AI Contribution:** Verified Gradle versions catalog, confirmed Jetpack Compose + Material 3 approach, created `IMPLEMENTATION_PLAN.md` with milestone breakdowns and technical requirements mapping.

### Phase 2: Domain Logic & Data Model Design
- **Prompt Purpose:** Implement Kotlin data classes implementing `Parcelable` with `@Parcelize`, pure business domain rules (`CarRentalDomain`), and in-memory state repository (`CarRentalRepository`).
- **AI Contribution:** Designed `@Parcelize data class Car`, domain validation functions (`validateRental`), cost calculation (`calculateTotalCost`), case-insensitive search (`searchCars`), immutable sorting (`sortCars`), and reactive state management using Kotlin Coroutines `StateFlow`.

### Phase 3: Android Activity Communication & UI Implementation
- **Prompt Purpose:** Build two primary Activities (`MainActivity` for catalogue and `RentalActivity` for rental details) communicating via explicit `Intent`s, type-safe `Parcelable` passing, and the modern `ActivityResultContracts.StartActivityForResult()` API.
- **AI Contribution:** Implemented Jetpack Compose screens (`CatalogueScreen`, `RentalScreen`), custom `StarRatingBar` composable, native `Slider` with step increments for rental duration (1–7 days), dynamic cost calculator, Light/Dark Mode `Switch`, and non-destructive validation error banners.

### Phase 4: Automated Testing & Test Plan Documentation
- **Prompt Purpose:** Implement comprehensive JUnit unit tests and Compose UI instrumentation tests targeting edge cases, boundary conditions, credit limit checks (400 credits cap), duplicate booking prevention, and sorting immutability.
- **AI Contribution:** Generated 21 unit test methods in `CarRentalDomainTest` and `CarRentalRepositoryTest`, wrote UI instrumentation test cases in `CarRentalUITest`, and compiled `TEST_PLAN.md`.

### Phase 5: Documentation & Report Outline
- **Prompt Purpose:** Draft comprehensive report-support documents (`REPORT_OUTLINE.md`, `DEVELOPMENT_LOG_TEMPLATE.md`, `IMPLEMENTATION_PLAN.md`, `TEST_PLAN.md`, and this disclosure).
- **AI Contribution:** Outlined a 10–13 page report structure, detailed two alternative layout wireframe justifications, provided prompts for student personal reflections and in-class demo preparation.

---

## 4. Student Verification & Mandatory Actions Before Submission

> [!IMPORTANT]
> **Action Required for Final University Submission:**
> 1. **Verify All Transcripts & Prompts:** Check the exact prompts and conversational history used during development.
> 2. **Include in Report Appendix:** Export or paste the relevant prompt transcripts and AI outputs into **Appendix: Generative AI Disclosure** of your final PDF report as mandated by the course rubric.
> 3. **Verify All Code Personally:** Ensure you thoroughly understand how `@Parcelize`, explicit `Intent`s, `rememberLauncherForActivityResult`, and `StateFlow` operate so you can confidently explain and defend your code during the in-class laboratory demo.
> 4. **Do Not Claim Zero AI Assistance:** Under no circumstances should this submission claim that generative AI was not used. Transparent, ethical disclosure adheres to academic integrity standards.
