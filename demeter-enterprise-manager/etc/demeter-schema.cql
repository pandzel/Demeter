// create keyspace 'demeter'
DROP KEYSPACE demeter;
CREATE KEYSPACE demeter 
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

// create tables
USE demeter;

CREATE TABLE sets (
   id UUID PRIMARY KEY,
   setSpec TEXT,
   setName TEXT
);

CREATE TABLE records (
   id UUID PRIMARY KEY,
   identifier TEXT,
   title TEXT,
   description TEXT,
   creator TEXT,
   date DATE,
   type TEXT
);

CREATE INDEX identifierIndex ON records(identifier);
CREATE INDEX dateIndex ON records(date);