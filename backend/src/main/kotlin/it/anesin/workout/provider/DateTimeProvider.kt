package it.anesin.workout.provider

import java.time.LocalDateTime
import java.time.ZoneId

interface DateTimeProvider {
  fun now(): LocalDateTime
}

class UTCDateTimeProvider: DateTimeProvider {
  override fun now(): LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
}
