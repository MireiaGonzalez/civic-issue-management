CREATE TABLE issue_attachments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    issue_id UUID NOT NULL,

    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT issue_attachment_issue_id_foreign_key FOREIGN KEY (issue_id) 
    REFERENCES issues(id) ON DELETE CASCADE,
    
    CONSTRAINT issue_attachment_file_size_check CHECK (file_size > 0)
);