package domain

import io.circe.generic.semiauto._

case class Salary(
    from: Option[Int],
    to: Option[Int],
    currency: String,
    gross: Boolean
)

case class Vacancy(
    id: String,
    salary: Option[Salary],
    name: String,
    has_test: Option[Boolean],
    alternate_url: Option[String]
)

case class VacancyList(items: List[Vacancy])

object VacancyList {
  implicit val salaryDecoder = deriveDecoder[Salary]
  implicit val vacancyDecoder = deriveDecoder[Vacancy]
  implicit val vacancyListDecoder = deriveDecoder[VacancyList]
}
