CREATE TABLE usuarios (
  id BIGINT AUTO_INCREMENT NOT NULL,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  perfil VARCHAR(20) NOT NULL,
  quantidade int DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO usuarios VALUES (1, 'Rafael Ferreira Lopes','admin@admin.com','$2a$10$VHRLcO99ddKCnDmYPx7EJOFzqoysDt0pXpflg.WUEH8IOkkCZX1g.','ADMIN', 0);
INSERT INTO usuarios VALUES (2, 'Rafael Ferreira Lopes','user@admin.com','$2a$10$VHRLcO99ddKCnDmYPx7EJOFzqoysDt0pXpflg.WUEH8IOkkCZX1g.','USUARIO', 1);