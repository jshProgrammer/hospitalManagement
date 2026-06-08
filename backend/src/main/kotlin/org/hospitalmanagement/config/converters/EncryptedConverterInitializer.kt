package org.hospitalmanagement.config.converters

import org.hospitalmanagement.services.CryptoUtility
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EncryptedConverterInitializer(private val cryptoUtility: CryptoUtility) {

    @EventListener(ApplicationReadyEvent::class)
    fun initializeConverters() {
        EncryptedStringConverter.setUtility(cryptoUtility)
    }
}
