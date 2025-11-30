INSERT INTO users (
    username,
    email,
    password,
    role,
    first_name,
    last_name,
    created_at,
    updated_at
)
VALUES (
           'admin',
           'admin@scoring.cl',
           '$2b$12$LlhWEzpfe9XW/duB48s7nebYu5aFz3c6pudCC57Lzxqx4eWz/aLBm',
           'ADMIN',
           'System',
           'Admin',
           NOW(),
           NOW()
       )
    ON CONFLICT (email) DO NOTHING;
