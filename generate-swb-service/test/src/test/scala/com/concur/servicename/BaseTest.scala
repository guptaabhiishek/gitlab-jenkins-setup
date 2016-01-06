package com.concur.servicename

import com.concur.servicename.client.ApiFactory
import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.test.context.{SmartContextLoader, TestContext, TestContextManager}

/**
 * Created by mtalbot on 12/08/2015.
 */
trait BaseTest {

  new TestContextManager(this.getClass()).prepareTestInstance(this)

}
