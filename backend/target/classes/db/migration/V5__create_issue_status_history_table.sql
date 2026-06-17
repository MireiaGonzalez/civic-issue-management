CREATE TABLE issue_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    issue_id UUID NOT NULL,
    changed_by_id UUID NOT NULL,

    previous_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    note TEXT,
    changed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT issue_status_history_issue_id_foreign_key FOREIGN KEY (issue_id)
    REFERENCES issues(id) ON DELETE CASCADE,

    CONSTRAINT issue_status_history_changed_by_id_foreign_key FOREIGN KEY (changed_by_id)
    REFERENCES users(id),

    CONSTRAINT issue_status_history_previous_status_check
    CHECK (previous_status IS NULL OR 
    previous_status IN ('SUBMITTED', 'IN_REVIEW', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),

    CONSTRAINT issue_status_history_new_status_check 
    CHECK (new_status IN ('SUBMITTED', 'IN_REVIEW', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'))
);