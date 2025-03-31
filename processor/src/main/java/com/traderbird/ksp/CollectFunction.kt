package com.traderbird.ksp

import kotlin.reflect.KClass
import com.google.auto.service.AutoService

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CollectFunction(
    val targetClass: KClass<*>,
    val targetField: String
)