# Penta Quark Modules
[![Build Status](https://travis-ci.org/TalosDx/pentaquark-modules.svg?branch=master)](https://travis-ci.org/TalosDx/pentaquark-modules) 
[![Coverage Status](https://coveralls.io/repos/github/TalosDx/PentaQuark/badge.svg?branch=master)](https://coveralls.io/github/TalosDx/PentaQuark?branch=master)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Penta Quark Modules allows modules to register automatically, everything you need to inherit from one of the PentaQuarkModule or AutoPentaQuarkModule classes

Depending on the class with which you inherit, modules can register themselves in ktor

## Usage
### Maven
```xml
<dependency>
  <groupId>dev.talosdx</groupId>
  <artifactId>pentaquark-modules</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
#### Groovy
```groovy
implementation 'dev.talosdx:pentaquark-modules:1.0.0'
```
#### Kotlin DSL
```kotlin
implementation("dev.talosdx:pentaquark-modules:1.0.0")
```


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
