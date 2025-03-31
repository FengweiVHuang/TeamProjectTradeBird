package com.traderbird.wrappers

data class RestString(val data: String) {
    constructor() : this("")
    fun get() = data

    override fun toString() = data
}
