package com.traderbird.ksp

object KspInjectionInitializer {
    @JvmStatic
    fun init() {
        try {

            val clazz = Class.forName("generated.CollectedFunctionRegistry")
            val method = clazz.getMethod("processCollectFunction")
            method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}