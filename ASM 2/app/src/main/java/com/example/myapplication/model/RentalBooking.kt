package com.example.myapplication.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an active or completed car rental booking.
 */
@Parcelize
data class RentalBooking(
    val car: Car,
    val rentalDays: Int,
    val totalCost: Int
) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(car, flags)
        dest.writeInt(rentalDays)
        dest.writeInt(totalCost)
    }

    companion object CREATOR : Parcelable.Creator<RentalBooking> {
        override fun createFromParcel(parcel: Parcel): RentalBooking {
            val car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                parcel.readParcelable(Car::class.java.classLoader, Car::class.java)
            } else {
                @Suppress("DEPRECATION")
                parcel.readParcelable(Car::class.java.classLoader)
            } ?: Car("", "", "", 0, 0f, 0, 0, 0)

            return RentalBooking(
                car = car,
                rentalDays = parcel.readInt(),
                totalCost = parcel.readInt()
            )
        }

        override fun newArray(size: Int): Array<RentalBooking?> {
            return arrayOfNulls(size)
        }
    }
}
