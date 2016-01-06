package com.concur.servicename.features

import java.util.UUID
import java.util.concurrent.ExecutionException

import com.concur.servicename.{ApiFactoryHolder, BaseFeatureSpec}
import com.concur.servicename.client.model.__ServiceName_Model
import com.concur.servicename.client.__ServiceName_resourceApi
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import scala.collection.JavaConversions._

/**
 * Created by mtalbot on 14/08/2015.
 */
class JIRA1234 extends BaseFeatureSpec {

  def api() = ApiFactoryHolder.factory.get__ServiceName_resourceApi

  info("JIRA1234")
  info("As a user I need to be able to delete existing models")

  feature("delete") {
    scenario("user deletes a new entry") {
      Given("a new entry")

      val id = UUID.randomUUID().toString
      val entry = api.
        save__ServiceName_Forid(
          id,
          new __ServiceName_Model().
            setId(id).
            setFrequency(1).
            setOccurs("1982-01-30T00:00:00.000Z").
            setName("testing")
        )

      When("deleted")

      api.delete__ServiceName_Forid(id) shouldBe true

      Then("it should be removed for list and not found on get")

      validateMissing(id)
    }
    scenario("user deletes an existing entry") {
      Given("an existing entry")

      val entry = api.get__ServiceName_.getContent.head

      When("deleted")

      api.delete__ServiceName_Forid(entry.getId) shouldBe true

      Then("it should be removed for list and not found on get")

      validateMissing(entry.getId)
    }
    scenario("user deletes a non existing entry") {
      Given("a non existing id")

      val id = UUID.randomUUID().toString

      When("deleted")

      val ex = the [ExecutionException] thrownBy(api.delete__ServiceName_Forid(id))

      Then("the response should be not found")

      validateException(ex)
    }
  }

  def validateMissing(id: String): Unit = {
    api.get__ServiceName_.getContent.map(_.getId) should not contain (id)

    val ex = the[ExecutionException] thrownBy (api.get__ServiceName_ForidResponse(id))

    validateException(ex)
  }

  def validateException(ex: ExecutionException): Unit = {
    ex.getCause shouldBe a[HttpClientErrorException]

    ex.getCause should have(
      'statusCode(HttpStatus.NOT_FOUND)
    )
  }
}
