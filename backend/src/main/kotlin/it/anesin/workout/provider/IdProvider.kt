package it.anesin.workout.provider

import java.util.*

interface IdGenerator {
  fun random(): UUID
}

class UUIDIdProvider: IdGenerator {
  override fun random(): UUID = UUID.randomUUID()
}
