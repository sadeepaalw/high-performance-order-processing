-- Clear existing data
DELETE FROM orders;

-- Insert test data
INSERT INTO orders (order_number, status, total_amount, customer_id, product_id, quantity, created_at, updated_at, version)
VALUES 
('ORD-001', 'PENDING', 100.00, 'CUST-001', 'PROD-001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('ORD-002', 'PROCESSING', 200.00, 'CUST-002', 'PROD-002', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('ORD-003', 'COMPLETED', 300.00, 'CUST-003', 'PROD-003', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('ORD-004', 'PENDING', 150.00, 'CUST-001', 'PROD-004', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('ORD-005', 'PROCESSING', 250.00, 'CUST-002', 'PROD-005', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1); 