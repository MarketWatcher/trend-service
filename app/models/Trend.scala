package models

import java.util.UUID
import play.api.libs.json._

case class Trend(alertId: UUID, delta: Int)

object Trend {
  implicit val trendFormat = Json.format[Trend]
}
