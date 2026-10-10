package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * MainActivity for Gymnastics Scoring App.
 * Manages routine progression (10 elements across 3 difficulty zones),
 * scoring rules (0 to 20 range), deductions, state preservation on rotation,
 * and localization support.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GymnasticsApp"

        // State Restoration Keys
        private const val KEY_SCORE = "KEY_SCORE"
        private const val KEY_CURRENT_ELEMENT = "KEY_CURRENT_ELEMENT"
        private const val KEY_DEDUCTION_TAKEN = "KEY_DEDUCTION_TAKEN"

        // Constants
        private const val MAX_ELEMENTS = 10
        private const val MAX_SCORE = 20
        private const val MIN_SCORE = 0
        private const val DEDUCTION_POINTS = 2
    }

    // State Variables
    private var score: Int = 0
    private var currentElement: Int = 0
    private var isDeductionTaken: Boolean = false

    // Views
    private lateinit var tvTitle: TextView
    private lateinit var tvElement: TextView
    private lateinit var tvZone: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvStatusMessage: TextView
    private lateinit var btnPerform: Button
    private lateinit var btnDeduction: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        // Restore saved instance state on rotation/configuration change
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            currentElement = savedInstanceState.getInt(KEY_CURRENT_ELEMENT, 0)
            isDeductionTaken = savedInstanceState.getBoolean(KEY_DEDUCTION_TAKEN, false)
            Log.d(TAG, "onCreate: Restored state -> Score: $score, Element: $currentElement, DeductionTaken: $isDeductionTaken")
        } else {
            Log.d(TAG, "onCreate: Initialized default routine state")
        }

        setupClickListeners()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCORE, score)
        outState.putInt(KEY_CURRENT_ELEMENT, currentElement)
        outState.putBoolean(KEY_DEDUCTION_TAKEN, isDeductionTaken)
        Log.d(TAG, "onSaveInstanceState: Saved state -> Score: $score, Element: $currentElement, DeductionTaken: $isDeductionTaken")
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tvTitle)
        tvElement = findViewById(R.id.tvElement)
        tvZone = findViewById(R.id.tvZone)
        tvScore = findViewById(R.id.tvScore)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)
        btnPerform = findViewById(R.id.btnPerform)
        btnDeduction = findViewById(R.id.btnDeduction)
        btnReset = findViewById(R.id.btnReset)
    }

    private fun setupClickListeners() {
        btnPerform.setOnClickListener {
            performNextElement()
        }

        btnDeduction.setOnClickListener {
            takeDeduction()
        }

        btnReset.setOnClickListener {
            resetRoutine()
        }
    }

    /**
     * Performs the next element in the routine.
     * Increases score based on the zone of the current element:
     * - Elements 1-3 (Basic): +1 pt
     * - Elements 4-7 (Intermediate): +2 pts
     * - Elements 8-10 (Advanced): +3 pts
     */
    private fun performNextElement() {
        if (currentElement >= MAX_ELEMENTS || isDeductionTaken) {
            Log.w(TAG, "performNextElement called when not allowed. Element: $currentElement, DeductionTaken: $isDeductionTaken")
            return
        }

        currentElement++
        val pointsAdded = getPointsForElement(currentElement)
        val prevScore = score
        score = (score + pointsAdded).coerceAtMost(MAX_SCORE)

        Log.d(
            TAG,
            "Perform Clicked -> New Element: $currentElement/$MAX_ELEMENTS, Points Added: +$pointsAdded, Score: $prevScore -> $score"
        )

        updateUI()
    }

    /**
     * Takes a deduction of 2 points.
     * Allowed only after 1st element is completed and before routine finishes.
     * Once a deduction is taken, no further elements can be performed.
     */
    private fun takeDeduction() {
        if (currentElement !in 1 until MAX_ELEMENTS || isDeductionTaken) {
            Log.w(TAG, "takeDeduction called when not allowed. Element: $currentElement, DeductionTaken: $isDeductionTaken")
            return
        }

        val prevScore = score
        score = (score - DEDUCTION_POINTS).coerceAtLeast(MIN_SCORE)
        isDeductionTaken = true

        Log.d(
            TAG,
            "Deduction Clicked -> Score: $prevScore -> $score (-$DEDUCTION_POINTS), DeductionTaken set to true. Performance stopped."
        )

        updateUI()
    }

    /**
     * Resets the routine score, element counter, and deduction state back to 0.
     */
    private fun resetRoutine() {
        Log.d(TAG, "Reset Clicked -> Resetting routine from Element: $currentElement, Score: $score, DeductionTaken: $isDeductionTaken")

        score = 0
        currentElement = 0
        isDeductionTaken = false

        updateUI()
    }

    /**
     * Returns points rewarded based on element number:
     * Elements 1-3: Basic (1 point each)
     * Elements 4-7: Intermediate (2 points each)
     * Elements 8-10: Advanced (3 points each)
     */
    private fun getPointsForElement(element: Int): Int {
        return when (element) {
            in 1..3 -> 1
            in 4..7 -> 2
            in 8..10 -> 3
            else -> 0
        }
    }

    /**
     * Updates view texts, score color according to difficulty zone, button enabled states,
     * and status message.
     */
    private fun updateUI() {
        // 1. Score Display
        tvScore.text = score.toString()

        // 2. Element Counter Display
        tvElement.text = getString(R.string.label_element_format, currentElement)

        // 3. Zone and Score Text Color according to current element
        val (zoneName, colorRes) = getZoneInfo(currentElement)
        tvZone.text = getString(R.string.label_zone_format, zoneName)
        tvScore.setTextColor(ContextCompat.getColor(this, colorRes))

        // 4. Button States:
        // - Perform: Enabled if < 10 elements and no deduction taken
        // - Deduction: Enabled only after 1st element (currentElement >= 1), before 10th element, and if no deduction taken yet
        // - Reset: Always enabled
        val canPerform = currentElement < MAX_ELEMENTS && !isDeductionTaken
        val canDeduct = currentElement in 1 until MAX_ELEMENTS && !isDeductionTaken

        btnPerform.isEnabled = canPerform
        btnDeduction.isEnabled = canDeduct
        btnReset.isEnabled = true

        // 5. Status / Completion Message Display
        tvStatusMessage.text = when {
            currentElement == MAX_ELEMENTS -> getString(R.string.status_complete)
            isDeductionTaken -> getString(R.string.status_deduction_taken)
            currentElement > 0 -> getString(R.string.status_in_progress)
            else -> getString(R.string.status_ready)
        }

        Log.d(
            TAG,
            "UI Updated -> Element: $currentElement, Score: $score, PerformEnabled: $canPerform, DeductEnabled: $canDeduct"
        )
    }

    /**
     * Helper to return zone description string and zone color resource.
     * Blue for basic (Elements 1-3), Green for intermediate (Elements 4-7), Orange for advanced (Elements 8-10).
     */
    private fun getZoneInfo(element: Int): Pair<String, Int> {
        return when (element) {
            in 1..3 -> Pair(getString(R.string.zone_basic), R.color.zone_basic)
            in 4..7 -> Pair(getString(R.string.zone_intermediate), R.color.zone_intermediate)
            in 8..10 -> Pair(getString(R.string.zone_advanced), R.color.zone_advanced)
            else -> Pair(getString(R.string.zone_not_started), R.color.zone_basic)
        }
    }
}