package it.anesin.workout.domain

import java.time.LocalDateTime
import java.util.*

data class Trainee(
  val _id: UUID,
  val username: String,
  val name: String,
  val trainers: Set<UUID> = emptySet(),
  val createdAt: LocalDateTime
)
