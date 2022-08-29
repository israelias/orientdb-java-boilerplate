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