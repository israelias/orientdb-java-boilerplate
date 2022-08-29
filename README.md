# OrientDB with Java in 5 minutes
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