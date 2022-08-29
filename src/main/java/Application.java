import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
public class Application {
  public static void main(String[] args) {

    OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
    ODatabaseSession db = orient.open("orientdb-java-boilerplate", "admin", "admin");

   // Nothing in this session yet

    db.close();
    orient.close();

  }

}
