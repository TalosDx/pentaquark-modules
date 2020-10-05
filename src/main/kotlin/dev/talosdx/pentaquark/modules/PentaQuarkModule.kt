package dev.talosdx.pentaquark.modules

import io.ktor.application.*

abstract class PentaQuarkModule(open val moduleConfig: Application.() -> Unit = {})

abstract class AutoPentaQuarkModule(tagName: String = AUTO_TAG_NAME, moduleConfig: Application.() -> Unit) : PentaQuarkModule(moduleConfig) {
    constructor(moduleConfig: Application.() -> Unit) : this(AUTO_TAG_NAME, moduleConfig)
    constructor() : this(AUTO_TAG_NAME, {})

    init {
        val element = this
        PentaQuarkModuleRegistry.registerModule(tagName, element)
    }
}

