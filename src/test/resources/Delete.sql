INSERT INTO _User (id, password, email, firstName, lastName, enabled)
VALUES (2,'password123', 'example@example.com', 'John', 'Doe', true);

--INSERT INTO Painting (description, name, price, quantity, user_id)
--VALUES ('Beautiful landscape painting', 'Landscape at Sunset', 99.99, 1, 2);
INSERT INTO Painting (id, description, name, price, quantity, user_id)
VALUES (200, 'Beautiful landscape painting', 'Landscape at Sunset', 99.99, 1, 2);


INSERT INTO ImageModel (name, type, path,  painting)
VALUES ('image_name_1', 'image_type_1', 'image_path_1', 200);
