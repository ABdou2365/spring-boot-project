ALTER TABLE customer
ADD CONSTRAINT customer_unique_email UNIQUE (email);