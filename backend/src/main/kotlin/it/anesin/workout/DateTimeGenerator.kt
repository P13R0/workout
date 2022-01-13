package it.anesin.workout

import java.time.LocalDateTime
import java.time.ZoneId

interface DateTimeGenerator {
  fun now(): LocalDateTime
}

class UTCDateTimeGenerator: DateTimeGenerator {
  override fun now(): LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
}
