package dev.talosdx.pentaquark.modules

import io.ktor.application.*

fun Application.initPqModule() {
    pentaQuarkModules {
        //register all auto pq modules with tag "auto"
        enableModules()
    }
}