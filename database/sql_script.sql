CREATE USER squad22_member WITH PASSWORD '12345';
CREATE DATABASE squad22_db;
GRANT ALL PRIVILEGES ON DATABASE squad22_db TO squad22_member;

\c squad22_db

CREATE TABLE user_table (
    id VARCHAR(40) PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE,
    full_name VARCHAR(55) NOT NULL,
    password TEXT NOT NULL,
    registration_info INTEGER UNIQUE,
    account_type TEXT NOT NULL,
    activity_status VARCHAR(16) NOT NULL
);

CREATE TABLE user_relation (
    relation_id VARCHAR(60) PRIMARY KEY,
    actor_id VARCHAR(40) REFERENCES user_table(id),
    subject_id VARCHAR(40) REFERENCES user_table(id),
    relation_type VARCHAR(16) NOT NULL
);

CREATE TABLE student (
    user_id VARCHAR(40) PRIMARY KEY REFERENCES user_table(id),
    course VARCHAR(32) NOT NULL,
    classes VARCHAR(32)[] NOT NULL
);

CREATE TABLE professor (
   user_id VARCHAR(40) PRIMARY KEY REFERENCES user_table(id),
   courses VARCHAR(32)[],
   classes VARCHAR(32)[],
   degrees VARCHAR(32)[] NOT NULL
);

CREATE TABLE commercial_user (
     user_id VARCHAR(40) PRIMARY KEY REFERENCES user_table(id),
     stores VARCHAR(32)[],
     services VARCHAR(32)[] NOT NULL,
     opening_time TIME NOT NULL,
     closing_time TIME NOT NULL
);

CREATE TABLE community (
   id VARCHAR(64) PRIMARY KEY,
   creator VARCHAR(40) REFERENCES user_table(id),
   title VARCHAR(32) NOT NULL,
   theme VARCHAR(55) NOT NULL,
   description TEXT UNIQUE,
   visibility VARCHAR(16) NOT NULL
);

CREATE TABLE community_user_relation (
     relation_id VARCHAR(60) PRIMARY KEY,
     user_id VARCHAR(40) REFERENCES user_table(id),
     community_id VARCHAR(64) REFERENCES Community(id),
     membership_status VARCHAR(16) NOT NULL
);

CREATE TABLE post (
      id VARCHAR(80) PRIMARY KEY,
      title TEXT NOT NULL,
      content TEXT NOT NULL,
      author_id VARCHAR(40) REFERENCES user_table(id),
      post_date TIMESTAMP NOT NULL,
      likes INTEGER,
      visibility VARCHAR(16) NOT NULL,
      keywords VARCHAR(24)[] NOT NULL
);

CREATE TABLE community_post (
    relation_id VARCHAR(60) PRIMARY KEY,
    community_id VARCHAR(64) REFERENCES Community(id),
    post_id VARCHAR(80) REFERENCES Post(id),
    post_status VARCHAR(16) NOT NULL
);

CREATE TABLE comment (
    id VARCHAR(120) PRIMARY KEY,
    post_id VARCHAR(80) REFERENCES Post(id),
    content TEXT,
    author_id VARCHAR(40) REFERENCES user_table(id),
    comment_date TIMESTAMP NOT NULL,
    likes INTEGER,
    generated_sub_session VARCHAR(100) UNIQUE NOT NULL,
    used_sub_session VARCHAR(100),
    visibility VARCHAR(16) NOT NULL
);

CREATE TABLE user_post_interaction (
   interaction_id VARCHAR(120) PRIMARY KEY,
   user_id VARCHAR(40) REFERENCES user_table(id),
   post_id VARCHAR(80) REFERENCES Post(id),
   action_type VARCHAR(16) NOT NULL,
   action_status VARCHAR(16),
   is_liked BOOLEAN,
   is_commented BOOLEAN
);
