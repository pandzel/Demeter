[![Build Status](https://travis-ci.org/pandzel/Demeter.png?branch=master)](https://travis-ci.org/pandzel/Demeter)
# Demeter
Demeter OAI-PMH toolkit.

Demeter's aim is to provide solution for building and consuming OAI-PMH repositories (see: [spec](http://www.openarchives.org/OAI/openarchivesprotocol.html)). It consist of the following sub-projects:

- **core** - fundametal classes shared amongst all other sub-projects [[details...](https://github.com/pandzel/Demeter/wiki/Core-packages)],
- **http-client** - fully featured HTTP client [[details...](https://github.com/pandzel/Demeter/wiki/HTTP-client)],
- **service** - fron-end request processing service [[details...](https://github.com/pandzel/Demeter/wiki/Service)],
- **simple server** - generic implementation of the OAI-PMH server with limited functionality [[details...](https://github.com/pandzel/Demeter/wiki/Generic-server)],
- **enterprise server** - OAI-PMH server with complete functionality [[details...](https://github.com/pandzel/Demeter/wiki/Enterprise-Server)].
- **enterprise  manager** - OAI-PMH server front end [[details...](https://github.com/pandzel/Demeter/wiki/Enterprise-Manager)].

[more..](https://github.com/pandzel/Demeter/wiki/Home)

## Requirements

### for running
- Java JDK 11 (or later)
- Apache Tomcat 9.x.x
- Apache Cassandra 3.x.x

### for build only
- NodeJS 10.15.3 (or later)
- Apache Maven 3.6.1 (or later)

## Quick start
```
git clone https://github.com/pandzel/Demeter.git
cd Demeter
mvn clean install
```
Deploy created demeter-server/target/demeter-server-\<version\>.war onto the Apache Tomcat (or any web server of your choice).

[more...](https://github.com/pandzel/Demeter/wiki/Building-and-installation)

## Licensing
Copyright 2019 Piotr Andzel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's [LICENSE](LICENSE) file.
