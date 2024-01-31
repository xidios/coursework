package Server

import cats.effect.IO
import cats.implicits.toTraverseOps
import database.Database
import models.Vacancy
import services.VacancyStatus.updateVacancyStatus
import webAPI.ApiService

class ApplicationServer(implicit db: Database) {
  def parseAllInfoFromHH(): IO[List[Vacancy]] =
    db.getSortedVacancies

  def updateVacancies(): IO[String] = for {
    response <- ApiService.fetchDataFromApi
    total <- response.traverse(db.insertVacancy)
    result = s"total: ${total.length}; added: ${total.count {
      case true => true
      case _    => false
    }}; skipped: ${total.count {
      case true => false
      case _    => true
    }}"
    vacancies <- db.getSortedVacancies
    _ <- updateVacancyStatus(response, vacancies)
  } yield result

  def searchVacanciesBySalary(
      minSalary: Int,
      maxSalary: Int
  ): IO[List[Vacancy]] = db.searchVacanciesBySalary(minSalary, maxSalary)

  def searchVacanciesRequiringTest(bool: Boolean): IO[List[Vacancy]] =
    db.searchVacanciesRequiringTest(bool)

  def searchVacanciesByCriteria(
      minSalary: Int,
      maxSalary: Int,
      requireTest: Boolean = true
  ): IO[List[Vacancy]] =
    db.searchVacanciesByCriteria(
      minSalary,
      maxSalary,
      requireTest
    )
}
