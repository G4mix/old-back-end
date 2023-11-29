SELECT * FROM users;

CREATE DOMAIN valid_username AS VARCHAR(50)
    CHECK (VALUE ~ '^[a-zA-Z0-9_]*$' AND LENGTH(VALUE) >= 3 AND LENGTH(VALUE) <= 50);

CREATE DOMAIN gmail_email AS TEXT
    CHECK (VALUE ~ '.+@gmail\\.com(\\.br)?$');

ALTER TABLE users
    ALTER COLUMN username SET DATA TYPE valid_username
    USING (username::VARCHAR(50))::valid_username,
    ALTER COLUMN email SET DATA TYPE gmail_email
    USING (email::TEXT)::gmail_email;

INSERT INTO users (email, username) VALUES ('lucas13@gmail.com', 'Lucas21');

CREATE TABLE statistics (
    id SERIAL,
    posts_total INT DEFAULT 0,
    users_total INT DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE OR REPLACE FUNCTION increment_post_count() RETURNS TRIGGER AS $$
BEGIN
    UPDATE statistics SET posts_total = posts_total + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_post_insert
AFTER INSERT ON post
FOR EACH ROW EXECUTE PROCEDURE increment_post_count();

CREATE OR REPLACE FUNCTION increment_user_count() RETURNS TRIGGER AS $$
BEGIN
    UPDATE statistics SET users_total = users_total + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_user_insert
AFTER INSERT ON users
FOR EACH ROW EXECUTE PROCEDURE increment_user_count();

CREATE OR REPLACE FUNCTION decrement_post_count() RETURNS TRIGGER AS $$
BEGIN
    UPDATE statistics SET posts_total = posts_total - 1;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_post_delete
AFTER DELETE ON post
FOR EACH ROW EXECUTE PROCEDURE decrement_post_count();

CREATE OR REPLACE FUNCTION decrement_user_count() RETURNS TRIGGER AS $$
BEGIN
    UPDATE statistics SET users_total = users_total - 1;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_user_delete
AFTER DELETE ON users
FOR EACH ROW EXECUTE PROCEDURE decrement_user_count();

INSERT INTO statistics (posts_total, users_total) VALUES (0, 0);

SELECT * FROM statistics;