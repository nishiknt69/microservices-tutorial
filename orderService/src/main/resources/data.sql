INSERT INTO orders (order_status, total_price) VALUES
('PENDING', 1500.00),
('CONFIRMED', 2500.00),
('CANCELLED', 3200.00),
('DELIVERED', 4500.00),
('PENDING', 1800.00),
('CONFIRMED', 5500.00),
('DELIVERED', 2200.00),
('PENDING', 3900.00),
('CONFIRMED', 1200.00),
('DELIVERED', 6000.00);


INSERT INTO orders_item (product_id, quantity, orders_id) VALUES
(101, 2, 1),
(102, 1, 1),
(103, 2, 2),
(104, 3, 2),
(105, 1, 3),
(106, 2, 3),
(107, 3, 4),
(108, 1, 4),
(109, 2, 5),
(110, 1, 5),
(111, 2, 6),
(112, 3, 6),
(113, 1, 7),
(114, 2, 7),
(115, 3, 8),
(116, 1, 8),
(117, 2, 9),
(118, 1, 9),
(119, 2, 10),
(120, 3, 10);