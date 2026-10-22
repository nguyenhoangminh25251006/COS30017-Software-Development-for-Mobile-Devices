package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a vehicle in the rental catalogue.
 * Implements [Parcelable] with [@Parcelize] for explicit Intent passing across Activities.
 */
@Parcelize
data class Car(
    val id: String,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float, // Rating from 1.0 to 5.0
    val kmsDriven: Int,
    val dailyCost: Int, // Daily rental cost in credits
    val imageResId: Int
) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(model)
        dest.writeInt(year)
        dest.writeFloat(rating)
        dest.writeInt(kmsDriven)
        dest.writeInt(dailyCost)
        dest.writeInt(imageResId)
    }

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(
                id = parcel.readString() ?: "",
                name = parcel.readString() ?: "",
                model = parcel.readString() ?: "",
                year = parcel.readInt(),
                rating = parcel.readFloat(),
                kmsDriven = parcel.readInt(),
                dailyCost = parcel.readInt(),
                imageResId = parcel.readInt()
            )
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}
