package controllers

import java.util.UUID
import javax.inject._

import com.datastax.driver.core.Cluster
import play.api.libs.json.Json
import play.api.mvc._

import scala.io.Source._

case class Alert(id: Int, name: String)

@Singleton
class ServiceController @Inject() extends Controller {

  var cluster = Cluster.builder()
    .addContactPoint(System.getenv("CASSANDRA_NODES"))
    .withPort(9042)
    .build()

  var session = cluster.connect()

  initDatabase()

  def index = Action {
    Ok("Trend Service")
  }

  def trends(alertId: String) = Action {
    val rows = session.execute("SELECT * FROM trends.trend where alert_id = " + UUID.fromString(alertId));
    val count = rows.one().getInt("count")
    Ok(Json.toJson(count))
  }

  def initDatabase(): Unit = {
    fromInputStream(getClass.getResourceAsStream("/init.cql"))
      .getLines
      .mkString
      .split(';')
      .filter(l => l.trim.length() > 0)
      .foreach(l => {
        session.execute(l)
      })
  }

}
