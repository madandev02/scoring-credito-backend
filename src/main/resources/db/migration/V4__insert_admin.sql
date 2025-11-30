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
           '$2a$12$C8e6GE41rE1gl4t0mK3r3OwQ7yEItUiTJLnYiLMi9F2ySvtJngk2K', -- admin123
           'ADMIN',
           'System',
           'Admin',
           NOW(),
           NOW()
       )
    ON CONFLICT (email) DO NOTHING;
