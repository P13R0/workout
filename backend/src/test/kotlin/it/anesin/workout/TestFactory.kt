package it.anesin.workout

import it.anesin.workout.domain.Trainee
import it.anesin.workout.domain.Trainer
import java.time.LocalDateTime
import java.util.*

object TestFactory {
  fun trainerWith(id: UUID) = Trainer(id, "anEmail", "aName", setOf(), LocalDateTime.now())
  fun traineeWith(id: UUID) = Trainee(id, "anEmail", "aName", setOf(), LocalDateTime.now())
}
