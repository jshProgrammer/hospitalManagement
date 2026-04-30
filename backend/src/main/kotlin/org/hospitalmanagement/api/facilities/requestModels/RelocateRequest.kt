package org.hospitalmanagement.api.facilities.requestModels

import com.fasterxml.jackson.annotation.JsonProperty

data class RelocateRequest(
    @JsonProperty("room_id")
    val roomId: Long
)