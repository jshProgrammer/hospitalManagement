package org.hospitalmanagement.api.facilities.requestModels

import org.hospitalmanagement.models.enums.BookingState
import java.util.Date
import com.fasterxml.jackson.annotation.JsonProperty

data class BookingRequest(
    val from: Date,
    val until: Date?,
    val state: BookingState,

    @JsonProperty("room_id")
    val roomId: Long,

    @JsonProperty("patient_id")
    val patientId: Long
)