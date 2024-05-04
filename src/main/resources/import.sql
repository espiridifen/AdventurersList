-- insert admin (username a, password aa)
INSERT INTO iwuser (id, enabled, roles, username, password, first_name, last_name)
VALUES (1, TRUE, 'ADMIN,USER', 'admin',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'user1', 'apellido1');
INSERT INTO iwuser (id, enabled, roles, username, password, first_name, last_name)
VALUES (2, TRUE, 'USER', 'user',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'user2', 'apellido2');

INSERT INTO games (name, description, experience, date, gamesystem, sessionquantity, owner, type, meeting)
VALUES ('Marberto', 'Descripcion1', 0, '2007-03-17T16:00:00', 5, 1, 2, 'One-shot', 'Online');

INSERT INTO games (name, description, experience, date, gamesystem, sessionquantity, owner, type, meeting)
VALUES ('Juego2', 'Descripcion2', 1, '2020-04-20T16:00:00', 5, 3, 1, 'One-shot', 'Online');

INSERT INTO joins (user_id, game_id)
VALUES (1, 1);

-- Messages
INSERT INTO message (id, date_sent, text, game_recipient_id, sender_id)
VALUES (0, '2024-04-01T23:55:56', 'hola', 1, 1);

-- GameSessions
INSERT INTO gamesession (date, location, title, game_id)
VALUES ('2024-04-01T23:55:56', 'online!', 'Sesion numero 1',1);

-- SessionAttendance
INSERT INTO sessionatendance (user_id, game_session_id, response)
VALUES (1, 1, 2);


INSERT INTO message (id, date_sent, text, game_recipient_id, sender_id)
VALUES (1, '2024-04-01T23:58:56', 'que tal', 1, 2);


-- start id numbering from a value that is larger than any assigned above
-- ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
