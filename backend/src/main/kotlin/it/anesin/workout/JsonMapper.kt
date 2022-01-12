package it.anesin.workout

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.vertx.core.json.jackson.DatabindCodec

object JsonMapper {
  fun setPreferences() {
    DatabindCodec.mapper()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
      .registerModule(JavaTimeModule())
      .registerKotlinModule()
  }
}
