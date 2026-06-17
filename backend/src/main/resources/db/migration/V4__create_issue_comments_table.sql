CREATE TABLE issue_comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    issue_id UUID NOT NULL,
    author_id UUID NOT NULL,

    body TEXT NOT NULL,
    visibility VARCHAR(10) NOT NULL DEFAULT 'PUBLIC',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT issue_comments_issue_id_foreign_key FOREIGN KEY (issue_id) 
    REFERENCES issues(id) ON DELETE CASCADE,

    CONSTRAINT issue_comments_author_id_foreign_key FOREIGN KEY (author_id) 
    REFERENCES users(id),

    CONSTRAINT issue_comments_visibility_check CHECK (visibility IN ('PUBLIC', 'ADMIN_ONLY')),
    
    CONSTRAINT issue_comments_body_check CHECK (length(trim(body)) > 0)
);