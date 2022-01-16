package it.anesin.workout.domain

import java.time.LocalDateTime
import java.util.*

data class Trainer(
  val _id: UUID,
  val username: String,
  val name: String,
  val trainees: Set<UUID> = emptySet(),
  val createdAt: LocalDateTime
)
