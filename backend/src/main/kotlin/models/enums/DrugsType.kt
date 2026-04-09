enum class DrugsType(val dbValue: String) {
    TABLET("tablet"),
    CAPSULE("capsule"),
    SYRUP("syrup"),
    INJECTION("injection"),
    INFUSION("infusion"),
    OINTMENT("ointment"),
    CREAM("cream"),
    DROPS("drops"),
    SPRAY("spray"),
    SUPPOSITORY("suppository");

    companion object {
        fun fromDb(value: String): DrugType =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown DrugType: $value")
    }
}