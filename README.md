[![Build Status](https://travis-ci.org/pandzel/Demeter.png?branch=master)](https://travis-ci.org/pandzel/Demeter)
# Demeter
Demeter OAI-PMH toolkit.

Demeter's aim is to provide solution for building and consuming OAI-PMH repositories. It consist of the following sub-projects:

- **core** - fundametal classes shared amongst all other sub-projects [[details...](https://github.com/pandzel/Demeter/wiki/Core-packages)],
- **http-client** - fully featured HTTP client [[details...](https://github.com/pandzel/Demeter/wiki/HTTP-client)],
- **service** - fron-end request processing service [[details...](https://github.com/pandzel/Demeter/wiki/Service)],
- **server** - generic implementation of the OAI-PMH server with limited functionality [[details...](https://github.com/pandzel/Demeter/wiki/Generic-server)],
- **enterprise server** - OAI-PMH server with complete functionality [planning...].

[more..](https://github.com/pandzel/Demeter/wiki/Home)

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

[more...](https://github.com/pandzel/Demeter/wiki/Building-and-installation)

