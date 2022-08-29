#  Java Server Application with OrientDB
> OrientDB is written completely in the Java language. This means that you can use its Java API's without needing to install any additional drivers or adapters.

This is a concoction of [this tutorial](http://orientdb.com/docs/3.0.x/fiveminute/) and configs specifically for a Maven/IntelliJ environment.

## Install
1. Download OrientDB from the following URL:
   2. https://orientdb.org/download
3. Unzip it on your FileSystem and open a shell in the directory.

## Start the server

```bash
cd orientdb-community-3.2.9
cd bin
```
Linux/OSX
```bash
./server.sh
```
Windows
```bash
server.bat
```

## Create a db 
The community edition comes with a `demodb`
Please follow the steps [here](http://orientdb.com/docs/3.0.x/fiveminute/java-1.html ) and launch http://localhost:2480/studio/index.html

## Create a Maven Project
And here, we find ourselves.

### Connecting to the DB
Assuming all else in your environment is set up, let's connect to OrientDB.

- Create an OrientDB object to manage the remote server
- Also invoke a `close()` at the end of the method (so that you don't forget it later)

The body of `Application.java` (our entry)
```java
    OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

    // let's do something with this server!

    orient.close();
```
The first argument of `OrientDB()` constructor is the URL of the remote database. It's made of two parts:

- `remote`: specifies that we are connecting to a remote (stand-alone) server
- `localhost`: the address of the host where OrientDB is running (it can be a URL or an IP, in this case orientdb is running on the local machine)

### Opening a DB Session
Until now, a connection with the server has been established, but not with the database itself 
(If you follow the tutorial, you have to create a db named `test`, a username named `admin` and a password `admin`. I created my own and set up three users with three user roles).

Now it's time to open a database session (and remember to close it at the end!):
```java
    OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
    ODatabaseSession db = orient.open("test", "admin", "admin");

    //let's do something with this session!

    db.close();    
    orient.close();
```
Here we are actually connecting to the database. The three parameters are:

- `test`: the database name (we created it a couple of steps before)
- `admin` (the first one): the username used to connect
- `admin` (the second one): the password for the connection

> By default, when you create a new database, OrientDB creates three users for you: `admin`, `reader`, `writer`; the passwords for these users are the same as the user names, eg. the password for `admin` is `admin`. You can change it later of course, and you can define more users if needed.
[Source](http://orientdb.com/docs/3.0.x/fiveminute/java-3.html)

The above is not exactly reliable, I had to create these user instances manually. Perhaps it depends on the edition that  you download (I downloaded `3.2.9` for OSX 🤷‍♂️).

Note: The database name is `orientdb-java-boilerplate` in this instance, although `test` equally persisted from the original tutorial. Another db I configured as `in memory` which, expectedly, did not persist on shutdown.

Running the application at this point with no logic should log:
```bash
2022-08-29 22:39:14:217 WARNI IMPORTANT! Using default password is unsafe, please change password for user 'admin' on database 'orientdb-java-boilerplate' [OrientDBDistributed]
```

Shutting down the application should log:
```bash
2022-08-30 01:14:20:872 WARNI Received signal: SIGINT [OSignalHandler]
2022-08-30 01:14:20:877 INFO  OrientDB Server is shutting down... [OServer]
2022-08-30 01:14:20:877 INFO  Shutting down listeners: [OServer]
2022-08-30 01:14:20:878 INFO  - ONetworkProtocolBinary /0.0.0.0:2424: [OServer]
2022-08-30 01:14:20:879 INFO  - ONetworkProtocolHttpDb /0.0.0.0:2480: [OServer]
2022-08-30 01:14:20:879 INFO  Shutting down protocols [OServer]
2022-08-30 01:14:20:881 INFO  Shutting down plugins: [OServerPluginManager]
2022-08-30 01:14:20:882 INFO  - studio [OServerPluginManager]
2022-08-30 01:14:20:882 INFO  - custom-sql-functions-manager [OServerPluginManager]
2022-08-30 01:14:20:883 INFO  - script-interpreter [OServerPluginManager]
2022-08-30 01:14:20:886 INFO  - etl [OServerPluginManager]
2022-08-30 01:14:20:886 INFO  Shutting down databases: [OServer]
2022-08-30 01:14:20:887 INFO  Orient Engine is shutting down... [Orient]
2022-08-30 01:14:20:896 INFO  - shutdown storage: OSystem... [OrientDBDistributed]
2022-08-30 01:14:21:016 INFO  - shutdown storage: orientdb-java-boilerplate... [OrientDBDistributed]
2022-08-30 01:14:23:336 INFO  Clearing byte buffer pool [Orient]
2022-08-30 01:14:23:346 INFO  OrientDB Engine shutdown complete [Orient]
2022-08-30 01:14:23:347 INFO  OrientDB Server shutdown complete
```

### Creating the schema

<img src="http://orientdb.com/docs/3.0.x/fiveminute/images/studio-schema.png" />

#### The Graph API (Apache TinkerPop 2.6) version
```java
    if (db.getClass("Person") == null) {
      db.createVertexClass("Person");
    }
    if (db.getClass("FriendOf") == null) {
      db.createEdgeClass("FriendOf");
    }

    db.close();
    orient.close();
```
[Source](http://orientdb.com/docs/3.0.x/fiveminute/java-3.html)

####  The Document API version

```java
ODatabaseDocument db;
...
   db.begin();
   ODocument doc1 = db.newInstance("Person");
   ODocument doc2 = db.newInstance("Person");

   // at this stage the record is not yet persistent

   System.out.println(doc1.getIdentity()); //this will print a temporary RID 
   
   doc1.setProperty("FriendOf", doc2);
   doc1.save(); // interchangeable with doc2.save()
   // If you forget to invoke doc1.save() before db.commit(), the record is STILL NOT PERSISTENT;
   db.commit();
   db.close();

```

### Using Database Pools

```java
OrientDB orientDB = new OrientDB("remote:localhost","root","root_passwd",OrientDBConfig.defaultConfig());

OrientDBConfigBuilder poolCfg = OrientDBConfig.builder();
poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MIN, 5);
poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MAX, 10);

ODatabasePool pool = new ODatabasePool(orientDB,"test","admin","admin", poolCfg.build());
// OPEN DATABASE
try (ODatabaseSession db = pool.acquire()) {
   // YOUR CODE
   ...
}
pool.close();
orientDB.close();
```
[Source](http://orientdb.com/docs/3.0.x/java/Document-API-Database.html)

## Java Server Application Development Process
The basic steps involved in the creation of a server application are summarized in the following table:

[Step 1: Compile the OMG IDL file for the server application.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023108)\
[Step 2: Write the methods that implement each interface's operations.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023186)\
[Step 3: Create the Server object.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023264)\
[Step 4: Compile the Java source files.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023350)\
[Step 5: Define the object activation and transaction policies.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023353)\
[Step 6: Verify the environment variables.](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023443)\
[Step 7: Finish the Server Description File.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023485)\
[Step 8: Deploy the server application.
](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm#1023515)

[Source](https://docs.oracle.com/cd/E13211_01/wle/wle50/jcreserv/maksrv.htm)

## References
- [OrientDB's javadoc](http://www.orientdb.com/javadoc/2.2.x/)
- [Java API](http://orientdb.com/docs/3.0.x/java/Java-API.html)