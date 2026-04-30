package org.hospitalmanagement.models.enums

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class BookingState(val dbValue: String) {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    CHECKED_IN("checked_in"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    RELOCATED("relocated"),
    NO_SHOW("no_show"),
    CHECKED_OUT_EARLY("checked_out_early");

    companion object {
        fun fromDb(value: String): BookingState =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown BookingState: $value")
    }
    @Converter(autoApply = true)
    class BookingStateConverter : AttributeConverter<BookingState, String> {
        override fun convertToDatabaseColumn(attribute: BookingState?): String? {
            return attribute?.dbValue
        }

        override fun convertToEntityAttribute(dbData: String?): BookingState? {
            return dbData?.let { BookingState.fromDb(it) }
        }
    }
}