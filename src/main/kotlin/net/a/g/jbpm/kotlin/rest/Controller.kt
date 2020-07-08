package net.a.g.jbpm.kotlin.rest

import org.jbpm.services.api.ProcessService
import org.jbpm.services.api.RuntimeDataService
import org.jbpm.services.api.model.ProcessInstanceDesc
import org.kie.api.runtime.query.QueryContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.core.MediaType


@RestController
class Controller {

    @Autowired
    var processService: ProcessService? = null;

    @Autowired
    var runtimeDataService: RuntimeDataService? = null;

    @GetMapping(path = ["/processinstances"], produces = [MediaType.APPLICATION_JSON])
    fun getProcessInstancesInfo(): Collection<ProcessInstanceDesc?>? {
        return runtimeDataService!!.getProcessInstances(QueryContext())
    }

}