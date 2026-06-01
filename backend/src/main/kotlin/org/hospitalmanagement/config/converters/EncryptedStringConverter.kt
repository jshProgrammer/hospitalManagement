package org.hospitalmanagement.config.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.hospitalmanagement.services.CryptoUtility
import org.springframework.stereotype.Component

@Converter(autoApply = false)
@Component
class EncryptedStringConverter : AttributeConverter<String, String> {

    companion object {
        private var cryptoUtility: CryptoUtility? = null

        fun setUtility(utility: CryptoUtility) {
            cryptoUtility = utility
        }
    }

    override fun convertToDatabaseColumn(attribute: String?): String {
        if (attribute == null || attribute.isBlank()) return ""
        return cryptoUtility?.encrypt(attribute) ?: attribute
    }

    override fun convertToEntityAttribute(dbData: String?): String {
        if (dbData == null || dbData.isBlank()) return ""
        return try {
            cryptoUtility?.decrypt(dbData) ?: dbData
        } catch (e: Exception) {
            dbData
        }
    }
}
