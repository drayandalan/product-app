INSERT INTO users(username, password_hash, role) VALUES ('demo', '$2a$10$D89Vw49Khir7jJyiKF4LhO3Qae.lx4qvQKtZgJgIE5l5/nVIhH6.W', 'USER');
-- password: demo123
INSERT INTO PRODUCT(NAME, DESCRIPTION, PRICE, CREATED_AT) VALUES
('Pen','Blue pen',1.50, CURRENT_TIMESTAMP()),
('Notebook','A5 ruled',3.99, CURRENT_TIMESTAMP()),
('Mug','Ceramic cup',5.49, CURRENT_TIMESTAMP());