package controllers

import java.util.UUID
import javax.inject.{Inject, _}

import dal.UpdateCountRepository
import models.Trend
import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class TrendController @Inject() (updateCountRepository: UpdateCountRepository) extends Controller {

  def trends(alertId: UUID) = Action {
    if(alertId.version() != 1) BadRequest("alertId must be a UUID version 1")

    if(alertId == null) BadRequest

    val updateCounts = updateCountRepository.getUpdateCountsByAlertId(alertId)

    if(updateCounts.size() < 2) {
      NotFound("Alert with id: " + alertId + " was not found")
    }
    else {
      val delta = updateCounts.get(0) - updateCounts.get(1)

      Ok(Json.toJson[Trend](new Trend(alertId, delta)))
    }
  }
}
