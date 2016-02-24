CREATE TABLE IF NOT EXISTS users (
  id   INT AUTO_INCREMENT PRIMARY KEY,
  user VARCHAR(100) NOT NULL,
  pass VARCHAR(64)  NOT NULL,
);

CREATE TABLE IF NOT EXISTS permissions (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL,
  permission VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS roles (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  role    VARCHAR(40)
);

-- Default user is admin:admin
INSERT INTO users (id, user, pass)
VALUES (NULL, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

SET @last_id = (SELECT TOP 1 id
                FROM users
                WHERE user = 'admin');

INSERT INTO permissions (id, user_id, permission)
VALUES (NULL, @last_id, 'read');

INSERT INTO permissions (id, user_id, permission)
VALUES (NULL, @last_id, 'write');

INSERT INTO permissions (id, user_id, permission)
VALUES (NULL, @last_id, 'delete');

INSERT INTO roles (id, user_id, role)
VALUES (NULL, @last_id, 'admin');