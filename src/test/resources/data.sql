-- Passwords are in the format: Password<UserLetter>123. Unless specified otherwise.
-- Encrypted using https://www.javainuse.com/onlineBcrypt
INSERT INTO local_user (email, first_name, last_name, password, username, email_verified)
    VALUES ('UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', '$2a$10$nb3fTr4Xyu5lTQpxjmwwxuDricTLKxe2E1YEK.bzU2/3T8Qu4PSiW', 'UserA', true),
           ('UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', '$2a$10$P9SxRvX7p3qnEARiqgOVUulKcQVbY3FlO1pBNBI.8CW.NOJZ9R2VO', 'UserB', false);
