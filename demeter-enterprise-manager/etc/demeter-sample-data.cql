USE demeter;

INSERT INTO sets (id, setSpec, setName)
  VALUES (uuid(), 'catalog', 'Main catalog of data');
INSERT INTO sets (id, setSpec, setName)
  VALUES (uuid(), 'arch', 'Archived data');
INSERT INTO sets (id, setSpec, setName)
  VALUES (uuid(), 'attic', 'Not yet approved data');

INSERT INTO records (id, identifier, title, description, creator, date, type)
  VALUES (uuid(), 'http://arXiv.org/abs/cs/0112017', 'Digital Libraries',
          'With the increasing technical sophistication of both information consumers and providers',
          'Dushay, Naomi', '2001-12-14', 'e-print');