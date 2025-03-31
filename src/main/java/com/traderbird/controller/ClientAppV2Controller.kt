package com.traderbird.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class ClientAppV2Controller {
    @GetMapping(value = ["/",  "/v2/**"])
    fun index(): String {
        return "forward:/index.html"
    }
}