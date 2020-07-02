package net.a.g.jbpm.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JBPMApplication

fun main(args: Array<String>) {
  runApplication<JBPMApplication>(*args){
  }
}