package dal

import java.util.{ArrayList, List, UUID}
import java.util.function.Consumer
import javax.inject.{Inject, Singleton}

import com.datastax.driver.core.Row

@Singleton
class UpdateCountRepository @Inject() (cassandra: CassandraService) {

  def getUpdateCountsByAlertId(alertId: UUID, limit:Int = 2) : List[Int] = {
    val rows = cassandra.getSession.execute("SELECT * FROM trends.trend where alert_id=" + alertId + " ORDER BY id LIMIT " + limit)

    val updateCounts = new ArrayList[Int]

    rows.iterator().forEachRemaining(new Consumer[Row] {
      override def accept(t: Row): Unit = {
        updateCounts.add(t.getInt("count"))
      }
    })

    return updateCounts
  }
}
