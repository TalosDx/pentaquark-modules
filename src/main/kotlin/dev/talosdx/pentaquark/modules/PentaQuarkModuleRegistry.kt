package dev.talosdx.pentaquark.modules

import io.ktor.application.*
import java.util.*

class PentaQuarkModuleRegistry(private val app: Application) {
    fun enableModules(tagName: String = AUTO_TAG_NAME): Set<PentaQuarkModule> {
        val mutableSet = registeredModules[tagName]
        mutableSet?.forEach { it.moduleConfig(app) }
        return mutableSet ?: setOf()
    }

    companion object {
        private val registeredModules: MutableMap<String, MutableSet<PentaQuarkModule>> = mutableMapOf(AUTO_TAG_NAME to mutableSetOf())
        val modules : Map<String, Set<PentaQuarkModule>> get() = Collections.unmodifiableMap(registeredModules)

        fun registerModule(tagName: String, module: PentaQuarkModule) = registeredModules
                .computeIfAbsent(tagName) { mutableSetOf() }
                .add(module)
    }
}
