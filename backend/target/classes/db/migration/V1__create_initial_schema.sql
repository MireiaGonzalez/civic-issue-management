CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT user_role_check CHECK (role IN ('CITIZEN', 'ADMIN'))
);

CREATE TABLE issue_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO issue_categories (name, description) VALUES
    ('Accessibility', 'Issues related to accessibility features'),
    ('Safety', 'Issues related to safety concerns'),
    ('Maintenance', 'Issues related to maintenance and repairs'),
    ('Environmental', 'Issues related to environmental concerns'),
    ('Other', 'Other miscellaneous issues');