enum class DoseUnit(val dbValue: String) {
    MG("mg"),
    G("g"),
    MCG("mcg"),
    ML("ml"),
    L("l"),
    TABLET("tablet"),
    CAPSULE("capsule"),
    DROP("drop"),
    PUFF("puff"),
    UNIT("unit");

    companion object {
        fun fromDb(value: String): DoseUnit =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown DoseUnit: $value")
    }
}