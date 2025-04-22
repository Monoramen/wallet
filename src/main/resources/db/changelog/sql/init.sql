INSERT INTO wallet (wallet_id, operation_type, balance)
VALUES
    ('a6f8a0c1-5f03-470e-9bfa-8e7b3aaf98c2', 'DEPOSIT',  1200.00),
    ('d442cf63-703e-4a9e-9bcb-2e3c37f3e256', 'WITHDRAW',  700.00),
    ('5d59b463-0e82-4cbf-bcf5-089e3e3b7e10', 'DEPOSIT', 850.00),
    ('f41b6427-0fa7-4a1e-9644-173d2cbdc49b', 'WITHDRAW',  350.00),
    ('7d1ec5e0-bb4d-42f5-b9c6-f391cb6e78fd', 'DEPOSIT', 2000.00)
    ON CONFLICT (wallet_id) DO NOTHING;
