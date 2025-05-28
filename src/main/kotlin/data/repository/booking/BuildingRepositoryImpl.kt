package org.daazay.data.repository.booking

import kotlinx.datetime.Clock.System
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.data.mapper.booking.toBuilding
import org.daazay.data.table.booking.BuildingTable
import org.daazay.data.utils.tranzaction
import org.daazay.domain.model.building.Building
import org.daazay.domain.model.building.BuildingCreate
import org.daazay.domain.model.building.BuildingUpdate
import org.daazay.domain.repository.building.BuildingRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class BuildingRepositoryImpl : BuildingRepository {
    init {
        tranzaction { SchemaUtils.create(BuildingTable) }
    }

    override suspend fun add(building: BuildingCreate): Building = tranzaction {
        BuildingTable.insert {
            it[BuildingTable.name] = building.name
            it[BuildingTable.address] = building.address
            it[BuildingTable.createdAt] = System.now().toLocalDateTime(TimeZone.UTC)
            it[BuildingTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }.toBuilding()
    }

    override suspend fun updateById(id: UUID, building: BuildingUpdate): Unit = tranzaction {
        if (BuildingTable.selectAll().where { BuildingTable.id eq id }.empty()) {
            throw NoSuchElementException("Building with id $id does not exist")
        }

        BuildingTable.update( { BuildingTable.id eq id } ) {
            building.name?.let { v -> it[BuildingTable.name] = v }
            building.address?.let { v -> it[BuildingTable.address] = v }
            it[BuildingTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun findById(id: UUID): Building = tranzaction {
        BuildingTable.selectAll()
            .where { BuildingTable.id eq id }
            .singleOrNull()
            ?.toBuilding()
            ?: throw NoSuchElementException("Building with id $id does not exist")
    }

    override suspend fun getAll(): List<Building> = tranzaction {
        BuildingTable.selectAll().map { it.toBuilding() }
    }

    override suspend fun deleteById(id: UUID) = tranzaction {
        if (BuildingTable.deleteWhere { BuildingTable.id eq id } == 0) {
            throw NoSuchElementException("Building with id $id does not exist")
        }
    }

    override suspend fun deleteAll(): Int = tranzaction {
        BuildingTable.deleteAll()
    }
}