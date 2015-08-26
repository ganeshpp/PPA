
import com.datastax.driver.core.Cluster

object CassandraConfig{

    val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
    val keySpace="demo"
    val session = cluster.connect(keySpace)
  //TODO create table if it does not exist
  /*CREATE KEYSPACE demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
CREATE TABLE Quotes ( symbol text,
                        value  double,
                        time  long,
                        name  text,
                        type  text,
                        PRIMARY KEY ((id),value,time) ) ;
  */
}
