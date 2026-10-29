-- Datos 100% sinteticos para el taller. Ningun RUT o cuenta corresponde a personas reales.
INSERT INTO customer (id, username, full_name, rut) VALUES (1, 'ana', 'Ana Perez', '11111111-1');
INSERT INTO customer (id, username, full_name, rut) VALUES (2, 'bruno', 'Bruno Soto', '22222222-2');

INSERT INTO beneficiary (id, public_id, customer_id, alias, holder_name, holder_rut, bank_code, account_number, internal_risk_score, status, created_by, created_at)
VALUES (90, '3f6c1a52-8d4e-4b1a-9c2f-5a7e1d0b9001', 1, 'Mama', 'Carmen Perez', '33333333-3', '016', '000012345678', 12, 'VERIFIED', 'ana', CURRENT_TIMESTAMP);
INSERT INTO beneficiary (id, public_id, customer_id, alias, holder_name, holder_rut, bank_code, account_number, internal_risk_score, status, created_by, created_at)
VALUES (91, '7b2e9d14-6a3c-4f8e-b1d5-2c9a8e4f9102', 2, 'Socio', 'Diego Rojas', '44444444-4', '012', '000087654321', 35, 'VERIFIED', 'bruno', CURRENT_TIMESTAMP);
INSERT INTO beneficiary (id, public_id, customer_id, alias, holder_name, holder_rut, bank_code, account_number, internal_risk_score, status, created_by, created_at)
VALUES (92, 'c41d7e83-2b5f-4a9c-8e6d-1f3b7a2c9203', 1, 'Arriendo', 'Inmobiliaria Demo SpA', '55555555-5', '037', '000011112222', 20, 'VERIFIED', 'ana', CURRENT_TIMESTAMP);
INSERT INTO beneficiary (id, public_id, customer_id, alias, holder_name, holder_rut, bank_code, account_number, internal_risk_score, status, created_by, created_at)
VALUES (93, 'e9a85f26-4c1d-4e7b-a3f9-6d2c5b8e9304', 2, 'Hermano', 'Pablo Soto', '66666666-6', '001', '000033334444', 80, 'PENDING', 'bruno', CURRENT_TIMESTAMP);

ALTER TABLE beneficiary ALTER COLUMN id RESTART WITH 100;
