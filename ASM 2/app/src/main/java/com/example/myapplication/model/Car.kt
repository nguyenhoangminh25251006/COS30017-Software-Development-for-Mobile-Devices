package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class representing a vehicle in the rental catalogue.
 * Implements [Parcelable] for explicit Intent passing across Activities.
 */
data class Car(
    val id: String,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float, // Rating from 1.0 to 5.0
    val kmsDriven: Int,
    val dailyCost: Int, // Daily rental cost in credits
    val imageResId: Int,
    val engine: String = "V-4 engine",
    val seats: Int = 5,
    val color: String = "Metallic",
    val mileage: String = "16.5 kmpl",
    val transmission: String = "8-speed automatic",
    val fuelType: String = "Gasoline",
    val description: String = "A reliable and fuel-efficient vehicle designed for comfortable daily driving and city travel, offering smooth handling and modern safety features."
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
        dest.writeString(engine)
        dest.writeInt(seats)
        dest.writeString(color)
        dest.writeString(mileage)
        dest.writeString(transmission)
        dest.writeString(fuelType)
        dest.writeString(description)
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
                imageResId = parcel.readInt(),
                engine = parcel.readString() ?: "V-4 engine",
                seats = parcel.readInt(),
                color = parcel.readString() ?: "Metallic",
                mileage = parcel.readString() ?: "16.5 kmpl",
                transmission = parcel.readString() ?: "8-speed automatic",
                fuelType = parcel.readString() ?: "Gasoline",
                description = parcel.readString() ?: ""
            )
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}
