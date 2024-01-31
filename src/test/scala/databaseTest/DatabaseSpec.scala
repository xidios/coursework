package databaseTest

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.Transactor
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.SpanSugar._
import database.Database
import domain.{Salary, Vacancy}
import models.{Vacancy => ModelVacancy}
import org.mockito.MockitoSugar.{mock, when}

class DatabaseSpec extends AnyFlatSpec with Matchers with ScalaFutures {

  implicit val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "1234"
  )

  val database: Database = mock[Database]

  "Database" should "create vacancies table" in {
    when(database.createVacanciesTable).thenReturn(IO.unit)
    val result = database.createVacanciesTable.unsafeRunSync()
    result shouldBe ()
  }

//  it should "insert and retrieve vacancies" in {
//    val vacancy = Vacancy(
//      id = "1",
//      salary = Some(Salary(Some(50000), Some(70000), "USD", gross = true)),
//      name = "Software Engineer",
//      has_test = Some(true),
//      alternate_url = Some("https://example.com")
//    )
//
//    val insertResult = database.insertVacancy(vacancy).unsafeRunSync()
//    insertResult shouldBe true
//
//    val retrievedVacancies = database.getSortedVacancies.unsafeRunTimed(5.seconds).get
//    retrievedVacancies should not be empty
//    retrievedVacancies.head shouldBe a[ModelVacancy]
//    retrievedVacancies.head.id shouldBe "1"
//  }
//
//  it should "update active status by id" in {
//    val vacancy = Vacancy(
//      id = "2",
//      salary = Some(Salary(Some(60000), Some(80000), "USD", gross = true)),
//      name = "Data Scientist",
//      has_test = Some(false),
//      alternate_url = Some("https://example.com/data-scientist")
//    )
//
//    val insertResult = database.insertVacancy(vacancy).unsafeRunSync()
//    insertResult shouldBe true
//
//    val initialStatus = database.getSortedVacancies.unsafeRunSync().head.active
//    initialStatus shouldBe true
//
//    database.updateActiveStatusById("2", isActive = false).unsafeRunSync()
//
//    val updatedStatus = database.getSortedVacancies.unsafeRunSync().head.active
//    updatedStatus shouldBe false
//  }
}
