package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.db.Room
import org.springframework.data.jpa.domain.Specification

object RoomsSpecifications{
    fun hasStationID(stationID: Long): Specification<Room> =
        Specification { root, _, cb ->
            cb.equal(root.get<Room>("station").get<Long>("id"), stationID)
        }

    fun hasNumber(number: Long): Specification<Room> =
        Specification { root, _, cb ->
            cb.equal(root.get<Long>("number"), number)
        }
}