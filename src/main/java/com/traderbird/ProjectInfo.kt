package com.traderbird

object ProjectInfo {
    @JvmStatic
    fun sayHello(): Boolean {
        println("Hello from Traderbird Ltd.!")
        printInfo()
        return true
    }

    @JvmStatic
    fun printInfo() {
        positionMap
            .map { (name, positions) ->
                "$name: ${positions.joinToString(", ")}"
            }.sorted()
            .forEach { println(it) }
    }

    private val positionMap = listOf(
        "Yiran" to listOf("Designer (Backend, Frontend)", "Developer (Backend, Frontend)"),
        "Joe" to listOf("Designer (Frontend)", "Developer (Frontend)"),
        "Way" to listOf("Project Manager (Frontend)", "Developer (Frontend)"),
        "Tommy" to listOf("Developer (Frontend)"),
        "Leo" to listOf("Developer (Frontend)"),
    )
}
