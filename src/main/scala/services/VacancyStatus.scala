package services

import cats.effect.IO
import cats.implicits.toTraverseOps
import database.Database
import domain.{Vacancy => DomainVacancy}
import models.{Vacancy => ModelVacancy}

object VacancyStatus {
  def updateVacancyStatus(
      responseVacancies: List[DomainVacancy],
      dbVacancies: List[ModelVacancy]
  )(implicit db: Database): IO[Unit] = {
    dbVacancies
      .map { dbVacancy =>
        val isActive = responseVacancies.exists(_.id == dbVacancy.id)
        IO.whenA(!isActive)(db.updateActiveStatusById(dbVacancy.id, isActive))
      }
      .sequence
      .map(_ => ())
  }
}
