# Penta Quark Modules
Penta Quark Modules allows modules to register automatically, everything you need to inherit from one of the PentaQuarkModule or AutoPentaQuarkModule classes

Depending on the class with which you inherit, modules can register themselves in ktor

## Usage
### Prepare for use pq modules
```kotlin
fun main(args : Array<String>) {
    embeddedServer(Netty, 8080) {
        pentaQuarkModules { 
            //register all auto pq modules with tag "auto" 
            enableModules()
        }
    }.start()
}
```
Or you can use another way by registering the module with the extension function
```kotlin
fun Application.initPqModule() {
    pentaQuarkModules {
        //register all auto pq modules with tag "auto" 
        enableModules()
    }
}
```
and add ext fun to application.conf

Or just add this to application.conf
```hocon
ktor {
  application {
    modules = [
      dev.talosdx.pentaquark.modules.InitPqModuleKt.initPqModule
    ]
  }
}
```

### Example usage pq modules
Example 1
```kotlin
class ExampleController() : AutoPentaQuarkModule() {
    override val moduleConfig: Application.() -> Unit
        get() = {
            consoleLog()
        }

    private fun consoleLog() {
        print("something")
    }
}
```

Example 2
```kotlin
class ExampleController(service: ExampleService) : AutoPentaQuarkModule({
    routing {
        get("/consolelog") {
            service.consoleLog()
        }
    }
})

class ExampleService {
    fun consoleLog() {
        print("something")
    }
}
``` 
