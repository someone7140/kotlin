package com.covit19.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ping")
class PingController {

  @RequestMapping(method = [RequestMethod.GET])
  @CrossOrigin
  fun ping(): String {
    return "OK"
  }
}
