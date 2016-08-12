import java.util
import java.util.UUID

import dal.{CassandraService, UpdateCountRepository}
import models.Trend
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar._
import org.scalatestplus.play._
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class TrendControllerTest extends PlaySpec with OneAppPerTest {

  "TrendController" should {
    "return a positive trend" in {
      val mockUpdateCountRepository = mock[UpdateCountRepository]
      val existingUUID = UUID.fromString("50554d6e-29bb-11e5-b345-feff819cdc9f")
      val updateCounts = util.Arrays.asList(123, 23)
      when(mockUpdateCountRepository.getUpdateCountsByAlertId(existingUUID)).thenReturn(updateCounts)

      val mockApp = new GuiceApplicationBuilder()
        .overrides(bind[CassandraService].toInstance(mock[CassandraService]))
        .overrides(bind[UpdateCountRepository].toInstance(mockUpdateCountRepository))
        .build

      contentAsJson(route(mockApp, FakeRequest(GET, "/trends/alert-id/50554d6e-29bb-11e5-b345-feff819cdc9f")).get) mustBe
        Json.toJson(new Trend(alertId = existingUUID, delta = 100))

    }

    "return a negative trend" in {
      val mockUpdateCountRepository = mock[UpdateCountRepository]
      val existingUUID = UUID.fromString("50554d6e-29bb-11e5-b345-feff819cdc9f")
      val updateCounts = util.Arrays.asList(23, 123)
      when(mockUpdateCountRepository.getUpdateCountsByAlertId(existingUUID)).thenReturn(updateCounts)

      val mockApp = new GuiceApplicationBuilder()
        .overrides(bind[CassandraService].toInstance(mock[CassandraService]))
        .overrides(bind[UpdateCountRepository].toInstance(mockUpdateCountRepository))
        .build

      contentAsJson(route(mockApp, FakeRequest(GET, "/trends/alert-id/50554d6e-29bb-11e5-b345-feff819cdc9f")).get) mustBe
        Json.toJson(new Trend(alertId = existingUUID, delta = -100))
    }

    "return bad request when alert id is not a uuid" in {
      status(route(app, FakeRequest(GET, "/trends/alert-id/NOT_UUUID")).get) mustBe BAD_REQUEST
    }


    "return not found when there is no updates with given alert id" in {
      val mockUpdateCountRepository = mock[UpdateCountRepository]
      val nonexistingUUID = UUID.fromString("50554d6e-29bb-11e5-b345-feff819cdc9f")
      when(mockUpdateCountRepository.getUpdateCountsByAlertId(nonexistingUUID)).thenReturn(new util.ArrayList[Int])

      val mockApp = new GuiceApplicationBuilder()
        .overrides(bind[CassandraService].toInstance(mock[CassandraService]))
        .overrides(bind[UpdateCountRepository].toInstance(mockUpdateCountRepository))
        .build

      status(route(mockApp, FakeRequest(GET, "/trends/alert-id/50554d6e-29bb-11e5-b345-feff819cdc9f")).get) mustBe NOT_FOUND
    }

    "return not found when there is less than two update counts for a given alert id" in {
      val mockUpdateCountRepository = mock[UpdateCountRepository]
      val existingUUID = UUID.fromString("50554d6e-29bb-11e5-b345-feff819cdc9f")
      val updateCounts = util.Arrays.asList(23)
      when(mockUpdateCountRepository.getUpdateCountsByAlertId(existingUUID)).thenReturn(updateCounts)

      val mockApp = new GuiceApplicationBuilder()
        .overrides(bind[CassandraService].toInstance(mock[CassandraService]))
        .overrides(bind[UpdateCountRepository].toInstance(mockUpdateCountRepository))
        .build

      status(route(mockApp, FakeRequest(GET, "/trends/alert-id/50554d6e-29bb-11e5-b345-feff819cdc9f")).get) mustBe NOT_FOUND
    }

  }

}
