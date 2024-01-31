package services

import cats.effect.IO
import io.circe.parser.decode
import domain.{Vacancy, VacancyList}

object JsonParser {
  def handle(response: String): List[Vacancy] =
    decode[VacancyList](response) match {
      case Right(response) => response.items
      case Left(error) =>
        println(error.toString)
        List.empty[Vacancy]
    }
}
