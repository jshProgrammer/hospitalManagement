package org.hospitalmanagement.models.enums

enum class DoseFrequency(val dbValue: String) {
    EVERY_X_DAYS("every_x_days"),
    X_DAILY("x daily"),
    EVERY_X_HOURS("every_x_hours"),
    X_WEEKLY("x weekly"),
    EVERY_X_WEEKS("every_x_weeks");

    companion object {
        fun fromDb(value: String): DoseFrequency =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown value: $value")
    }
}