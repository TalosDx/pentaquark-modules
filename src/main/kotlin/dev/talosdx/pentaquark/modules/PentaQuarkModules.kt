package dev.talosdx.pentaquark.modules

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

typealias PentaQuarkModulesConfigure = PentaQuarkModuleRegistry.() -> Unit
const val AUTO_TAG_NAME = "auto"

object PentaQuarkModules : ApplicationFeature<Application, PentaQuarkModuleRegistry, PentaQuarkModuleRegistry> {
    override val key: AttributeKey<PentaQuarkModuleRegistry> = AttributeKey("PentaQuarkModules")

    override fun install(pipeline: Application, configure: PentaQuarkModulesConfigure): PentaQuarkModuleRegistry {
        val ktorModuleRegistry = PentaQuarkModuleRegistry(pipeline)
        ktorModuleRegistry.enableModules()
        return ktorModuleRegistry
    }
}

/**
 * @author Roman Ushakov
 *
 * Gets or installs a [PentaQuarkModules] feature for the this [Application] and runs a [configuration] script on it
 */
@ContextDsl
fun Application.pentaQuarkModules(configuration: PentaQuarkModulesConfigure) =
        featureOrNull(PentaQuarkModules)
                ?.apply { apply(configuration) }
                ?: install(PentaQuarkModules, configuration)