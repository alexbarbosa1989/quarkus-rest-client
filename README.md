# quarkus-rest-client

This application exposes a REST service that, based on the sent request, consumes a remote REST API to store/retrieve data in a remote Data Grid Cluster. 
To achieve that, the application exposes two REST Endpoints:

- query/getData
- query/putData

## Basic application execution

## Configure and start the Data Grid server (Which exposes the REST API)
1. Create the **person** cache in your Data Grid installation :
~~~
<?xml version="1.0"?>
<distributed-cache name="person" owners="2" mode="SYNC" statistics="true">
	<encoding>
		<key media-type="x-protostream/x-protostream"/>
		<value media-type="x-protostream/x-protostream"/>
	</encoding>
</distributed-cache>
~~~
Json format:
~~~
{
  "distributed-cache": {
    "owners": "2",
    "mode": "SYNC",
    "statistics": true,
    "encoding": {
      "key": {
        "media-type": "application/x-protostream"
      },
      "value": {
        "media-type": "application/x-protostream"
      }
    }
  }
}
~~~
2. Add the authentication mechanisms for the rest-connector endpoint that includes `BASIC` in the `infinispan.xml` configuration file located in `$RHDG_HOME/server/conf/` directory. Here is an example:
~~~
        <endpoints>
          <endpoint socket-binding="default" security-realm="default">
                <hotrod-connector name="hotrod-default" socket-binding="default">
                    <authentication security-realm="default">
                        <sasl server-name="infinispan" mechanisms="SCRAM-SHA-512 SCRAM-SHA-384 SCRAM-SHA-256 SCRAM-SHA-1 DIGEST-SHA-512 DIGEST-SHA-384 DIGEST-SHA-256 DIGEST-SHA CRAM-MD5 DIGEST-MD5" qop="auth"/>
                    </authentication>
                </hotrod-connector>
                <rest-connector name="rest-default" socket-binding="default">
                    <authentication mechanisms="BASIC DIGEST" security-realm="default"/>
                </rest-connector>
          </endpoint>
        </endpoints>
~~~
3. Create a user and start the Data Grid instance:
~~~
$RHDG_HOME/bin/cli user create admin -p admin
~~~
~~~
$RHDG_HOME/bin/server.sh
~~~

## Clone and execute the Quarkus application (rest-client)
1. Clone the project:
~~~
git clone -b main https://github.com/alexbarbosa1989/quarkus-rest-client
~~~
2. Upload `person.proto` schema provided in the app root directory to the running Data Grid server:
~~~
curl -u admin:admin -X POST --data-binary @./person.proto http://localhost:11222/rest/v2/caches/___protobuf_metadata/person.proto
~~~
3. Execute the application in dev mode (More options in [Quarkus basics](https://github.com/alexbarbosa1989/quarkus-rest-client/blob/main/README.md#quarkus-basics) section):
~~~
./mvnw compile quarkus:dev
~~~
4. Update the `application.properties` file located in [`main/src/main/resources/application.properties`](https://github.com/alexbarbosa1989/quarkus-rest-client/blob/main/src/main/resources/application.properties) with the credentials and Data Grid server URL. Here is the default provided in the current app:
~~~
quarkus.rest-client."com.redhat.test.rest.RemoteServiceClient".url=http://localhost:11222

dgserver.username=admin
dgserver.password=admin
~~~
5. Use the exposes REST endpoint:
   
   5.a. Use the `query/putData/{key}` to put data in the external Data Grid cache:
   ~~~
   curl -X POST --header 'Content-Type: application/json' -d '{ "firstName": "Sadio", "lastName": "Mané", "bornYear": "1992", "bornIn": "Senegal" }' http://localhost:8080/query/putData/person1
   ~~~
   5.b. Use the `query/getData/{key}` to put data in the external Data Grid cache:
   ~~~
   curl -X GET http://localhost:8080/query/getData/person1
   ~~~

## ____
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
