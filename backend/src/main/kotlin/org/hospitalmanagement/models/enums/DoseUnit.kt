package org.hospitalmanagement.models.enums

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

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

    @Converter(autoApply = true)
    class DoseUnitConverter : AttributeConverter<DoseUnit, String> {
        override fun convertToDatabaseColumn(attribute: DoseUnit?): String? {
            return attribute?.dbValue
        }

        override fun convertToEntityAttribute(dbData: String?): DoseUnit? {
            return dbData?.let { DoseUnit.fromDb(it) }
        }
    }
}