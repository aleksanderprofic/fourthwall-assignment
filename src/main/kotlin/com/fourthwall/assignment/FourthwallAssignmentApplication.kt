package com.fourthwall.assignment

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("com.fourthwall.assignment.persistence")
class FourthwallAssignmentApplication

fun main(args: Array<String>) {
    runApplication<FourthwallAssignmentApplication>(*args)
}
