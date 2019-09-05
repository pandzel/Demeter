// create keyspace 'demeter'
DROP KEYSPACE demeter;
CREATE KEYSPACE demeter 
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

// create tables
USE demeter;

CREATE TABLE counter (
   table_name TEXT PRIMARY KEY,
   counter COUNTER
);

CREATE TABLE config (
   id INT PRIMARY KEY,
   repositoryName TEXT,
   baseURL TEXT,
   adminEmail LIST<TEXT>
);

CREATE TABLE tokens (
   id UUID PRIMARY KEY,
   cql TEXT,
   pagingState TEXT
) WITH default_time_to_live = 60;

CREATE TABLE sets (
   id UUID PRIMARY KEY,
   setSpec TEXT,
   setName TEXT
);

CREATE TABLE records (
   id UUID PRIMARY KEY,
   title TEXT,
   creator TEXT,
   subject TEXT,
   description TEXT,
   publisher TEXT,
   contributor TEXT,
   date DATE,
   type TEXT,
   format TEXT,
   identifier TEXT,
   source TEXT,
   language TEXT,
   relation TEXT,
   coverage TEXT,
   rights TEXT
);

CREATE INDEX identifierIndex ON records(identifier);
CREATE INDEX dateIndex ON records(date);

CREATE TABLE collections (
  recordId UUID,
  setId UUID,
  PRIMARY KEY (recordId, setId)
);

CREATE INDEX setIdx ON collections(setId);
