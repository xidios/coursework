package webAPI

import cats.effect.IO
import domain.Vacancy
import services.JsonParser.handle
import sttp.client3._
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

object ApiService {
  def fetchDataFromApi: IO[List[Vacancy]] =
    AsyncHttpClientCatsBackend[IO]().flatMap { implicit backend =>
      fetch()
    }

  def fetch(
      page: Int = 0
  )(implicit backend: SttpBackend[IO, Any]): IO[List[Vacancy]] = {
    basicRequest
      .get(
        uri"https://api.hh.ru/vacancies?per_page=100&page=$page&text=%D1%8F%D0%B7%D1%8B%D0%BA%20scala&no_magic=false"
      )
      .send()
      .flatMap { response =>
        if (response.isSuccess) {
          response.body match {
            case Left(_) =>
              println(f"${response.code}: ${response.statusText}")
              IO.pure(List.empty[Vacancy])
            case Right(value) =>
              val vacancies = handle(value)
              if (vacancies.size < 100) {
                IO.pure(vacancies)
              } else {
                fetch(page + 1).map(vacancies ++ _)
              }
          }
        } else {
          println(f"${response.code}: ${response.statusText}")
          IO.pure(List.empty[Vacancy])
        }
      }
  }
}
