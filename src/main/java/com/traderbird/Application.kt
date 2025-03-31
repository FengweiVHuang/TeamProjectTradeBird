package com.traderbird

import com.traderbird.ksp.KspInjectionInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    println("Hello from SpringBoot!")

    KspInjectionInitializer.init()

    ProjectInfo.sayHello()
    runApplication<Application>(*args)
}
