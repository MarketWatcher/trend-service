package dal

import com.datastax.driver.core.{Cluster, Session}
import javax.inject.Singleton

import scala.io.Source._

@Singleton
class CassandraService{

  private var session: Session = null

  def getSession: Session = {
    if(session != null) return session

    val clusterBuilder = Cluster.builder()

    try {
      System
        .getenv("CASSANDRA_NODES")
        .split(",")
        .foreach(node => {
          val nodeInfo = node.split(":")
          clusterBuilder.addContactPoint(nodeInfo(0)).withPort(Integer.valueOf(nodeInfo(1)))
        })
    } catch {
      case e: Exception => println(
        """Invalid value for CASSANDRA_NODES. Nodes must explicitly specify their ports
and must be separated with commas. Example: hosta:9042,hostb:9042""".stripMargin)
    }

    val cluster = clusterBuilder.build()

    session = cluster.connect()

    initDatabase(session)

    session
  }

  getSession

  private def initDatabase(session: Session): Unit = {
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
