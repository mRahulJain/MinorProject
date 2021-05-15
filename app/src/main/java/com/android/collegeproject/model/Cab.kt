package com.android.collegeproject.model

data class CabMessage(
    val msg: String
)

data class Cabs(
    val _id: String,
    val loc: Loc,
    val vehicleNumber: String,
    val isBooked: Boolean,
    val name: String,
    val vehicle: String,
    val vehicleType: String,
    val contact: String
)

data class Loc(
    val type: String,
    val coordinates: Array<Double>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Loc

        if (type != other.type) return false
        if (!coordinates.contentEquals(other.coordinates)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + coordinates.contentHashCode()
        return result
    }
}

data class Bookings(
    val user: GeoLocation,
    val destination: String,
    val driverId: String,
    val status: String
)

data class GeoLocation(
    val lat: Double,
    val long: Double
)
