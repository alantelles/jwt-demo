package com.alangomes.jwtdemo.dto

import jakarta.validation.constraints.Null

abstract class AppUser {

    @Null
    List<String> roles

}
