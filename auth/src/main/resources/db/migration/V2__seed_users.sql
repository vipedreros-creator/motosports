-- Activamos pgcrypto aquí para que herede las funciones 'crypt' y 'gen_salt'
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (email, password, active)
VALUES
    ('user1@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user2@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user3@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user4@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user5@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user6@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user7@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user8@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user9@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE),
    ('user10@mail.cl', crypt('abcd.1234', gen_salt('bf')), TRUE)
ON CONFLICT (email) DO NOTHING;