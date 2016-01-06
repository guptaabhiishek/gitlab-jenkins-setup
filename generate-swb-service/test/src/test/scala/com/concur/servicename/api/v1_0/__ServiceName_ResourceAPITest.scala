package com.concur.servicename.api.v1_0

import java.util.concurrent.ExecutionException
import java.util.{Date, UUID}

import com.concur.servicename.{ApiFactoryHolder, BaseFlatSpec, BaseTest}
import com.concur.servicename.client.ApiFactory
import com.concur.servicename.client.__ServiceName_resourceApi
import com.concur.servicename.client.model.__ServiceName_Model
import org.joda.time.DateTime
import org.scalatest.FlatSpec
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import scala.collection.JavaConversions._

/**
 * Created by mtalbot on 12/08/2015.
 */
class __ServiceName_ResourceAPITest extends BaseFlatSpec {

  def api() = ApiFactoryHolder.factory.get__ServiceName_resourceApi
  val testKey = UUID.randomUUID

  "An __ServiceName_ resource" should "be pre-populated with values in its list verb" in {
    val response = api.get__ServiceName_Response
    val body = response.getBody

    response should have(
      'statusCode(HttpStatus.OK)
    )

    body.getContent.size() should be > 15

    body.getLinks should have size (1)

    body.getLinks.head should have(
      'rel("self"),
      'href("http://localhost:" + ApiFactoryHolder.port + "/__ServiceName_/v1.0/")
    )
  }

  it should "add new instances" in {
    val response = api.
      save__ServiceName_ForidResponse(
        testKey.toString,
        new __ServiceName_Model().
          setFrequency(2L).
          setId(testKey.toString).
          setName("Blurb").
          setOccurs("1982-01-30")
      )

    response should have(
      'statusCode(HttpStatus.ACCEPTED)
    )

    val body = response getBody

    body.getLinks.head should have(
      'rel("self"),
      'href("http://localhost:" + ApiFactoryHolder.port + "/__ServiceName_/v1.0/" + testKey.toString)
    )

    body should have(
      'frequency(2L),
      'id(testKey.toString),
      'name("Blurb"),
      'occurs("1982-01-30T00:00:00.000Z")
    )
  }

  it should "have the new instance in list" in {
    val entry = api.
      get__ServiceName_Response.
      getBody.
      getContent.
      find(_.getId.equals(testKey.toString))

    entry shouldBe defined

    entry.get should have(
      'frequency(2L),
      'id(testKey.toString),
      'name("Blurb"),
      'occurs("1982-01-30T00:00:00.000Z")
    )
  }

  it should "be deletable" in {
    api.delete__ServiceName_Forid(testKey.toString) shouldBe true
  }

  it should "validate saved instances" in {
    val save = (freq: Long, name: String, occurs: String) => {
      val id = UUID.
        randomUUID.
        toString

      val ex = the[ExecutionException] thrownBy api.save__ServiceName_ForidResponse(
        id,
        new __ServiceName_Model().
          setId(id).
          setFrequency(freq).
          setName(name).
          setOccurs(occurs)
      )

      ex.getCause shouldBe a[HttpClientErrorException]

      ex.getCause should have(
        'statusCode(HttpStatus.BAD_REQUEST)
      )
    }

    save(-1, "A Name", "1982-01-30T00:00:00.000Z")

    save(101, "A Name", "1982-01-30T00:00:00.000Z")

    save(35, "", "1982-01-30T00:00:00.000Z")

    save(22, "A Name", null)
  }

}
