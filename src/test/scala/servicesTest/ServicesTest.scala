package servicesTest

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import services.JsonParser
import domain.Vacancy
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.MockitoSugar.{mock, when}
import servicesTest.TestData.validResponse
import sttp.client3.{Request, Response, SttpBackend}
import sttp.model.StatusCode
import webAPI.ApiService

object TestData {
  val validResponse =
    """
      |{
      |  "items": [
      |    {
      |      "id": "1",
      |      "salary": {
      |        "from": 50000,
      |        "to": 70000,
      |        "currency": "USD",
      |        "gross": true
      |      },
      |      "name": "Software Engineer",
      |      "has_test": true,
      |      "alternate_url": "https://example.com"
      |    }
      |  ]
      |}
      |""".stripMargin
}
class JsonParserSpec extends AnyFlatSpec with Matchers with ScalaFutures {

  "JsonParser" should "handle valid response" in {

    val vacancies = JsonParser.handle(validResponse)
    vacancies should have size 1
    val vacancy = vacancies.head
    vacancy.id shouldBe "1"
    vacancy.name shouldBe "Software Engineer"
    vacancy.has_test shouldBe Some(true)
  }

  it should "handle invalid response" in {
    val invalidResponse = "invalid JSON"
    val vacancies = JsonParser.handle(invalidResponse)
    vacancies shouldBe empty
  }
}

class ApiServiceSpec extends AnyFlatSpec with Matchers with ScalaFutures {

  implicit val backend = mock[SttpBackend[IO, Any]]
  "ApiService" should "fetch data from API" in {
    val response = Response[Either[String, String]](
      Right(validResponse),
      StatusCode.Ok,
      "OK"
    )

    when(backend.send(any[Request[Either[String, String], Any]]))
      .thenReturn(IO.pure(response))

    val fetchedData = ApiService.fetch().unsafeRunSync()
    fetchedData should not be empty
  }

  it should "handle API request failure" in {
    val response = Response[Either[String, String]](
      Left("Error"),
      StatusCode.InternalServerError,
      "Error"
    )

    when(backend.send(any[Request[Either[String, String], Any]]))
      .thenReturn(IO.pure(response))

    val fetchedData = ApiService.fetch().unsafeRunSync()
    fetchedData shouldBe empty
  }
}
