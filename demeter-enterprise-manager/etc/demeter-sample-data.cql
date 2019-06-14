USE demeter;

INSERT INTO config (id, repositoryName, baseURL, adminEmail)
  VALUES(0, 'Demeter Enterprise OAI-PMH Server', 'http://localhost:8080/demeter-enterprise', ['admin@localhost']);
  
INSERT INTO sets (id, setSpec, setName)
  VALUES (10000000-0000-0000-0000-000000000001, 'catalog', 'Main catalog of data');
UPDATE counter SET counter = counter + 1 WHERE table_name = 'sets';

INSERT INTO sets (id, setSpec, setName)
  VALUES (10000000-0000-0000-0000-000000000002, 'arch', 'Archived data');
UPDATE counter SET counter = counter + 1 WHERE table_name = 'sets';

INSERT INTO sets (id, setSpec, setName)
  VALUES (10000000-0000-0000-0000-000000000003, 'attic', 'Not yet approved data');
UPDATE counter SET counter = counter + 1 WHERE table_name = 'sets';


INSERT INTO records (id, identifier, title, description, creator, date, type)
  VALUES (20000000-0000-0000-0000-000000000001, 'http://arXiv.org/abs/cs/0112017', 'Digital Libraries',
          'With the increasing technical sophistication of both information consumers and providers',
          'Dushay, Naomi', '2001-12-14', 'e-print');
UPDATE counter SET counter = counter + 1 WHERE table_name = 'records';


INSERT INTO records_sets (recordId, setId)
  VALUES (20000000-0000-0000-0000-000000000001, 10000000-0000-0000-0000-000000000001);


INSERT INTO sets_records (setId, recordId)
  VALUES (10000000-0000-0000-0000-000000000001, 20000000-0000-0000-0000-000000000001);
