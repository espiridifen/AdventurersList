-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (1, TRUE, 'ADMIN,USER', 'admin',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'user1', 'apellido1');
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (2, TRUE, 'USER', 'user',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'user2', 'apellido2');

INSERT INTO games (name, description, experience, date, gamesystem, sessionquantity, owner, type, meeting)
VALUES ('Marberto', 'Descripcion1', 'Beginner', '2007-03-17T16:00:00', 'Dungeons and Dragons 5th Edition', 1, 2, 'One-shot', 'Online');

INSERT INTO games (name, description, experience, date, gamesystem, sessionquantity, owner, type, meeting)
VALUES ('Juego2', 'Descripcion2', 'Beginner', '2020-04-20T16:00:00', 'Dungeons and Dragons 5th Edition', 3, 1, 'One-shot', 'Online');

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
