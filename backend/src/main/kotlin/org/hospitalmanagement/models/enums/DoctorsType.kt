package org.hospitalmanagement.models.enums
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class DoctorsType(val dbValue: String) {
    ASSISTANT_PHYSICIAN("assistant_physician"),
    SENIOR_PHYSICIAN("senior_physician"),
    CHIEF_PHYSICIAN("chief_physician"),
    CONSULTANT("consultant"),
    RESIDENT("resident"),
    ATTENDING_PHYSICIAN("attending_physician"),
    HEAD_OF_DEPARTMENT("head_of_department");

    companion object {
        fun fromDb(value: String): DoctorsType =
            values().find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown value: $value")
    }
}

@Converter(autoApply = true)
class DoctorsTypeConverter : AttributeConverter<DoctorsType, String> {

    override fun convertToDatabaseColumn(attribute: DoctorsType?): String? {
        return attribute?.dbValue
    }

    override fun convertToEntityAttribute(dbData: String?): DoctorsType? {
        return dbData?.let {
            DoctorsType.fromDb(it)
        }
    }
}