package org.hospitalmanagement.models.enums

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

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
        fun fromDb(value: String): DrugsType =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown DrugType: $value")
    }

    @Converter(autoApply = true)
    class DrugsTypeConverter : AttributeConverter<DrugsType, String> {
        override fun convertToDatabaseColumn(attribute: DrugsType?): String? =
            attribute?.dbValue

        override fun convertToEntityAttribute(dbData: String?): DrugsType? =
            dbData?.let { DrugsType.fromDb(it) }
    }
}