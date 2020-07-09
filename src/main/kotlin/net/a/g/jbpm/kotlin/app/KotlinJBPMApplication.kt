package net.a.g.jbpm.kotlin.app

import org.jbpm.kie.services.impl.KModuleDeploymentUnit
import org.jbpm.services.api.DeploymentService
import org.jbpm.services.api.ProcessService
import org.jbpm.services.api.RuntimeDataService
import org.jbpm.services.api.model.ProcessDefinition
import org.kie.api.runtime.query.QueryContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan(basePackages = ["net.a.g.jbpm.kotlin.jpa", "net.a.g.jbpm.kotlin.rest"])
class KotlinJBPMApplication {
  @Bean
  fun deployAndValidate(): CommandLineRunner {
    return object : CommandLineRunner {
      @Autowired
      private val deploymentService: DeploymentService? = null

      @Autowired
      private val runtimeDataService: RuntimeDataService? = null

      @Autowired
      private val processService: ProcessService? = null

      @Throws(Exception::class)
      override fun run(vararg strings: String) {
        var unit: KModuleDeploymentUnit? = null
        if (strings.isNotEmpty()) {
          val arg = strings[0]
          LOGGER.info("About to deploy : {}", arg)
          val gav = arg.split(":".toRegex()).toTypedArray()
          unit = KModuleDeploymentUnit(gav[0], gav[1], gav[2])
          deploymentService!!.deploy(unit)
          LOGGER.info("{} successfully deployed", arg)
        }
        LOGGER.info("Available processes:")
        val processes: Collection<ProcessDefinition> = runtimeDataService!!.getProcesses(QueryContext())
        for (def in processes) {
          LOGGER.info("\t{} (with id '{})", def.getName(), def.getId())
        }
        if (unit != null && !processes.isEmpty()) {
          val processId: String = processes.iterator().next().getId()
          LOGGER.info("About to start process with id {}", processId)
          val processInstanceId = processService!!.startProcess(unit.identifier, processId)
          LOGGER.info("Started instance of {} process with id {}", processId, processInstanceId)
          processService.abortProcessInstance(processInstanceId)
          LOGGER.info("Aborted instance with id {}", processInstanceId)
        }
        LOGGER.info("========= Verification completed successfully =========")
      }
    }
  }

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(KotlinJBPMApplication::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
      System.setProperty("hibernate.connection.provider_class","net.a.g.jbpm.kotlin.jpa.ForceSpringBootConnectionProvider");

      SpringApplication.run(KotlinJBPMApplication::class.java, *args)
    }
  }
}