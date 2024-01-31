package Server

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe.syntax.EncoderOps
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.implicits._
import io.circe.generic.auto._

object Routes {
  def make(implicit appServer: ApplicationServer): HttpApp[IO] = {
    val routes = HttpRoutes.of[IO] {
      case GET -> Root / "hello" =>
        Ok("Hello, World!")
      case GET -> Root / "vacancies" =>
        println("/vacancies")
        Ok(
          appServer
            .parseAllInfoFromHH()
            .unsafeRunSync()
            .asJson
        )
      case POST -> Root / "vacancies" / "update" =>
        println("/vacancies/update")
        Ok {
          val text = appServer
            .updateVacancies()
            .unsafeRunSync()
          val resultText = s"Success: 'Ok', text: '$text'"
          resultText.asJson
        }

      case GET -> Root / "vacancies" / "search" / "salary" :? MinSalaryQueryParam(
            minSalary
          ) +& MaxSalaryQueryParam(maxSalary) =>
        println("GET /vacancies/search/salary")
        Ok(
          appServer
            .searchVacanciesBySalary(minSalary, maxSalary)
            .unsafeRunSync()
            .asJson
        )

      case GET -> Root / "vacancies" / "search" / "test" :? BoolQueryParamMatcher(
            bool
          ) =>
        println("GET /vacancies/search/test")
        Ok(
          appServer
            .searchVacanciesRequiringTest(bool)
            .unsafeRunSync()
            .asJson
        )

      case GET -> Root / "vacancies" / "search" / "criteria" :? MinSalaryQueryParam(
            minSalary
          ) +& MaxSalaryQueryParam(maxSalary) +& BoolQueryParamMatcher(
            requireTest
          ) =>
        println("GET /vacancies/search/criteria")
        Ok(
          appServer
            .searchVacanciesByCriteria(minSalary, maxSalary, requireTest)
            .unsafeRunSync()
            .asJson
        )
    }
    routes.orNotFound
  }

  object MinSalaryQueryParam extends QueryParamDecoderMatcher[Int]("minSalary")
  object MaxSalaryQueryParam extends QueryParamDecoderMatcher[Int]("maxSalary")
  object BoolQueryParamMatcher extends QueryParamDecoderMatcher[Boolean]("test")

}
