package it.anesin.workout.provider

import java.util.*

interface IdGenerator {
  fun random(): UUID
}

class UUIDIdGenerator: IdGenerator {
  override fun random(): UUID = UUID.randomUUID()
}
