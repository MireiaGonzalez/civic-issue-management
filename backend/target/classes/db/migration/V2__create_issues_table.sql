CREATE TABLE issues (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL,
    category_id UUID NOT NULL, 

    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    priority VARCHAR(30) NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    resolved_at TIMESTAMPTZ,

    CONSTRAINT issue_reporter_id_foreign_key 
    FOREIGN KEY (reporter_id) REFERENCES users(id),

    CONSTRAINT issue_category_id_foreign_key 
    FOREIGN KEY (category_id) REFERENCES issue_categories(id),

    CONSTRAINT issue_status_check 
    CHECK (status IN ('SUBMITTED', 'IN_REVIEW', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),

    CONSTRAINT issue_priority_check 
    CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))
);

