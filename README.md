# quarkus-rest-client

This application exposes a REST service that, based on the sent request, consumes a remote REST API to store/retrieve data in a remote Data Grid Cluster. 
To achieve that, the application exposes two REST Endpoints:

- query/getData
- query/putData

## Basic application execution

## Start and configure Data Grid
1. Create the **person** cache in your Data Grid installation :
~~~
<?xml version="1.0"?>
<distributed-cache name="person" owners="2" mode="SYNC" statistics="true">
	<encoding>
		<key media-type="text/plain"/>
		<value media-type="application/json"/>
	</encoding>
</distributed-cache>
~~~
Json format:
~~~
{
  "person": {
    "distributed-cache": {
      "owners": "2",
      "mode": "SYNC",
      "statistics": true,
      "encoding": {
        "key": {
          "media-type": "text/plain"
        },
        "value": {
          "media-type": "application/json"
        }
      }
    }
  }
}
~~~
2. Add the authentication mechanisms for the rest-connector endpoint in the `infinispan.xml` configuration file located in `$RHDG_HOME/server/conf/` directory:
~~~
      <endpoints socket-binding="default" security-realm="default">
         <endpoint socket-binding="default" security-realm="default">
           <hotrod-connector name="hotrod"/>
           <rest-connector name="rest">
              <authentication mechanisms="DIGEST BASIC" security-realm="default"/>
           </rest-connector>
         </endpoint>
      </endpoints>
~~~
3. Create an user and start the data grid instance and start it:
~~~
$RHDG_HOME/bin/cli user create admin -p admin
~~~
~~~
$RHDG_HOME/bin/server.sh
~~~
4. Clone the project:
~~~
git clone -b main https://github.com/alexbarbosa1989/quarkus-rest-client
~~~
5. Execute the application in dev mode (More options in [Quarkus basics](https://github.com/alexbarbosa1989/quarkus-rest-client#packaging-and-running-the-application)) section):
~~~
./mvnw quarkus:dev
~~~

## Quarkus basics
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/dg-client-1.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
