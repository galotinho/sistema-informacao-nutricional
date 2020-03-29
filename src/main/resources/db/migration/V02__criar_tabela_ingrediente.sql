CREATE TABLE ingredientes (
  id BIGINT AUTO_INCREMENT NOT NULL,
  nome VARCHAR(255) NOT NULL,
  carboidratos DOUBLE NOT NULL,
  proteinas DOUBLE NOT NULL,
  gordurastotais DOUBLE NOT NULL,
  gordurassaturadas DOUBLE NOT NULL,
  gordurastrans DOUBLE NOT NULL,
  fibraalimentar DOUBLE NOT NULL,
  sodio VARCHAR(10) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO ingredientes VALUES (1, 'Nibs', 84.519, 5.88, 4, 2.408, 0, 3.5, 0.504);
INSERT INTO ingredientes VALUES (2, 'Manteiga de Cacau', 0, 0, 100, 59.7, 0, 0, 0);
INSERT INTO ingredientes VALUES (3, 'Açúcar', 99.59, 0, 0.1, 0.018, 0, 0, 0.001);
INSERT INTO ingredientes VALUES (4, 'Leite em Pó', 38.419, 26.32, 26.709, 16.742, 0, 0, 0.371);
INSERT INTO ingredientes VALUES (5, 'Lecitina', 0, 0, 100, 15, 0, 0, 0);
