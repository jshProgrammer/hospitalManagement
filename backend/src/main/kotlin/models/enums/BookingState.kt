enum class BookingState(val dbValue: String) {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    CHECKED_IN("checked_in"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    NO_SHOW("no_show"),
    CHECKED_OUT_EARLY("checked_out_early");

    companion object {
        fun fromDb(value: String): BookingState =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown BookingState: $value")
    }
}