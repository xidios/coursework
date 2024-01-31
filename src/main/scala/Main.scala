import Server.{ApplicationServer, Routes}
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.unsafe.implicits.global
import cats.implicits.toTraverseOps
import database.Database
import doobie.Transactor
import services.VacancyStatus.updateVacancyStatus
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {
  implicit val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/postgres",
    "postgres",
    "1234"
  )
  implicit val db = new Database
  implicit val appServer = new ApplicationServer()

  override def run(args: List[String]): IO[ExitCode] = {
    db.createVacanciesTable.unsafeRunSync()
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(Routes.make)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
