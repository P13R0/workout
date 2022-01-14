package it.anesin.workout.provider

import java.util.*

interface PasswordProvider {
  fun random(): String
}

class DefaultPasswordProvider: PasswordProvider {
  override fun random(): String {
    val length = 10
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (0..length)
      .map { Random().nextInt(chars.size) }
      .map { chars[it] }
      .joinToString("")
  }
}

