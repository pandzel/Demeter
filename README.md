[![Build Status](https://travis-ci.org/pandzel/Demeter.png?branch=master)](https://travis-ci.org/pandzel/Demeter)
# Demeter
Demeter OAI-PMH toolkit.

Demeter's aim is to provide solution for building and consuming OAI-PMH repositories. It consist of the following sub-projects:

- **core** - fundametal classes shared amongst all other sub-projects,
- **http-client** - fully implemented HTTP client,
- **service** - request processing service,
- **server** - generic implementation of the OAI-PMH server with limited functionality,
- **enterprise server** - OAI-PMH server with complete functionality.

## Requirements

- Java JDK 8 (or later)
- Apache Tomcat 9.x.x

## Quick start
```
git clone https://github.com/pandzel/Demeter.git
cd Demeter
mvn clean install
```
Deploy created demeter-server/target/demeter-server-\<version\>.war onto the Apache Tomcat (or any web server of your choice).

