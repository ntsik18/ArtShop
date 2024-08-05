INSERT INTO _User (password, email, firstName, lastName, enabled)
VALUES ('password123', 'example@example.com', 'John', 'Doe', true);

INSERT INTO Painting (description, name, price, quantity, user_id)
VALUES ('Beautiful landscape painting', 'Landscape at Sunset', 99.99, 1, 1);
INSERT INTO Painting (description, name, price, quantity, user_id)
VALUES ('Abstract art with vibrant colors', 'Colorful Abstraction', 149.99, 1, 1);
INSERT INTO Painting (description, name, price, quantity, user_id)
VALUES ('Classic portrait of a woman', 'Mona Lisa', 999.99, 1, 1);
INSERT INTO Painting (description, name, price, quantity, user_id)
VALUES ('Surrealistic dream scene', 'Dreamscape', 199.99, 1, 1);
INSERT INTO Painting (description, name, price, quantity, user_id)
VALUES ('Abstract expressionism masterpiece', 'Composition VII', 999.99, 1, 1);

INSERT INTO ImageModel (name, type, path,  painting)
VALUES ('image_name_1', 'image_type_1', 'image_path_1', 1),
       ('image_name_2', 'image_type_2', 'image_path_2',  2),
       ('image_name_3', 'image_type_3', 'image_path_3',  3),
       ('image_name_4', 'image_type_4', 'image_path_4',  4),
       ('image_name_5', 'image_type_5', 'image_path_5',  5);
