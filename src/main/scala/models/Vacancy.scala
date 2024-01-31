package models

case class Vacancy(
                    id: String,
                    salary: Option[Int],
                    name: String,
                    hasTest: Option[Boolean],
                    alternateUrl: Option[String],
                    active: Boolean
                  ) {
  override def toString: String = {
    val salaryString = salary.map(s => s"Salary: $s").getOrElse("Salary: N/A")
    val hasTestString = hasTest.map(ht => s"Has Test: $ht").getOrElse("Has Test: N/A")
    val alternateUrlString = alternateUrl.map(url => s"Alternate URL: $url").getOrElse("Alternate URL: N/A")

    s"""
       |Vacancy:
       |ID: $id
       |Name: $name
       |$salaryString
       |$hasTestString
       |$alternateUrlString
       |Active: $active
       |""".stripMargin
  }
}

