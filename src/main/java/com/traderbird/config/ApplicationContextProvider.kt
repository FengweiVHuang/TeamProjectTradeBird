package com.traderbird.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationContextProvider : ApplicationContextAware {

    companion object {
        private lateinit var context: ApplicationContext

        fun getApplicationContext(): ApplicationContext {
            return context
        }

        fun <T> visit(function: (ApplicationContext) -> T): T {
            return function(context)
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }
}