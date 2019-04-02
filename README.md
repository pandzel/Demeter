# Demeter
Demeter OAI-PMH toolkit.

Demeter's aims to provide solution for building and consuming OAI-PMH repositories. It consist of the following sub-projects:

- **core** - fundametal classes shared amongst all other sub-projects,
- **http-client** - fully developed HTTP client,
- **service** - request processing service,
- **server** - generic implementation of the OAI-PMH server,
- **enterprise server** - fully developed server.

## Requirements

- Java JDK 8
- Git Bash
- Apache Maven
- Apache Tomcat

## Quick start
```
git clone https://github.com/pandzel/Demeter.git
cd Demeter
mvn clean install
```
then deploy created demeter-server/target/demeter-server-\<version\>.war onto the Apache Tomcat (or any web server of your choice).

