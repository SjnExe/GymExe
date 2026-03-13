package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(@PrimaryKey val id: String, val name: String, val description: String)
