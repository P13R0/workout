package it.anesin.workout

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class JsonExtension: BeforeAllCallback, ExtensionContext.Store.CloseableResource {
  override fun close() = Unit
  override fun beforeAll(context: ExtensionContext) = JsonMapper.setPreferences()
}
