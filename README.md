# Demeter
Demeter OAI-PMH toolkit.

Demeter aims to provide solution for building and consuming OAI-PMH repositories. It consist of the following sub-projects:
- 'core' - fundametal classes shared amongst all other sub-projects,
- 'http-client' - fully developed HTTP client capable to format and send requests to any OAI-PMH end-point, as well receive and translate responses,
- 'service' - a facet implementation base for any OAI-PMH server; it contains fully implemented front-end capable of understand OAI-PMH request as well it will produce correct responses. It is intended to be extended by implemented an adaptor to the actual data set,
- 'server; - a simple and generic implementation of the OAI-PMH server ready to be deployed; an example fo the 'service' extendsion,
- 'enterprise server' - fully developed server.
