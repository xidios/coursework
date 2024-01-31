package database

import cats.effect.IO
import cats.implicits.catsSyntaxOptionId
import doobie._
import doobie.implicits._
import domain.Vacancy
import models.{Vacancy => ModelVacancy}

class Database(implicit transactor: Transactor[IO]) {
  def createVacanciesTable: IO[Unit] =
    sql"""CREATE TABLE IF NOT EXISTS vacancies (
        id VARCHAR(255) PRIMARY KEY,
        salary INT,
        name VARCHAR(255),
        has_test BOOLEAN,
        alternate_url VARCHAR(255),
        active BOOLEAN);""".stripMargin.update.run
      .transact(transactor)
      .map(_ => ())

  def insertVacancy(vacancy: Vacancy): IO[Boolean] =
    sql"""
      INSERT INTO vacancies (id, salary, name, has_test, alternate_url, active)
      VALUES (${vacancy.id}, ${vacancy.salary
      .map(_.from.getOrElse(0))}, ${vacancy.name}, ${vacancy.has_test}, ${vacancy.alternate_url}, true)
      ON CONFLICT (id) DO NOTHING
    """.update.run
      .transact(transactor)
      .map(_ == 1)

  def getSortedVacancies: IO[List[ModelVacancy]] =
    sql"SELECT * FROM vacancies ORDER BY salary DESC NULLS LAST"
      .query[ModelVacancy]
      .to[List]
      .transact(transactor)

  def searchVacanciesBySalary(
      minSalary: Int,
      maxSalary: Int
  ): IO[List[ModelVacancy]] =
    sql"SELECT * FROM vacancies WHERE salary BETWEEN $minSalary AND $maxSalary ORDER BY salary DESC NULLS LAST"
      .query[ModelVacancy]
      .to[List]
      .transact(transactor)

  def searchVacanciesRequiringTest(bool: Boolean): IO[List[ModelVacancy]] =
    sql"SELECT * FROM vacancies WHERE has_test = $bool ORDER BY salary DESC NULLS LAST"
      .query[ModelVacancy]
      .to[List]
      .transact(transactor)

  def searchVacanciesByCriteria(
      minSalary: Int,
      maxSalary: Int,
      requireTest: Boolean = true
  ): IO[List[ModelVacancy]] = {
    val baseQuery = sql"SELECT * FROM vacancies WHERE "
    val salaryCondition = sql"salary BETWEEN $minSalary AND $maxSalary "
    val testCondition = sql"AND has_test = $requireTest"
    val sql =
      baseQuery ++ salaryCondition ++ testCondition
    sql
      .query[ModelVacancy]
      .to[List]
      .transact(transactor)
  }

  def updateActiveStatusById(
      id: String,
      isActive: Boolean
  ): IO[Unit] = {
    sql"""
        UPDATE vacancies
        SET active = $isActive
        WHERE id = $id
      """.update.run
      .transact(transactor)
      .map(_ => ())
  }
}
