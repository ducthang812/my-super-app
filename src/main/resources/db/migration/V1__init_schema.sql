CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,  --id account
                          username VARCHAR(50) NOT NULL, --username account
                          email VARCHAR(255) NOT NULL, --email accouint
                          password_hash VARCHAR(255) NOT NULL, --password hash
                          status VARCHAR(20) NOT NULL, --status account
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, --created at
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, --updated at

                          CONSTRAINT uk_accounts_username UNIQUE (username),
                          CONSTRAINT uk_accounts_email UNIQUE (email)
);

--user_profiles
CREATE TABLE user_profiles (
                               user_id BIGINT PRIMARY KEY, --user id
                               display_name VARCHAR(100), --ten hien thi
                               bio VARCHAR(500), --bio
                               avatar_url VARCHAR(500), --dia chi avatar
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--post
CREATE TABLE posts (
                       id BIGSERIAL PRIMARY KEY, --id bai post
                       author_id BIGINT NOT NULL, --id nguoi post bai
                       content TEXT NOT NULL, --noi dung bai
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--comment--
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY, --id cua comment
                          post_id BIGINT NOT NULL,  --id cua bat post co comment
                          author_id BIGINT NOT NULL, --id cua nguoi comment
                          content TEXT NOT NULL, --noi dung comment
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--like--
CREATE TABLE post_likes (
                            post_id BIGINT NOT NULL, --id cua bai post duoc like
                            user_id BIGINT NOT NULL, --id cua nguoi like bai post
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            PRIMARY KEY (post_id, user_id)
);

--follows--
CREATE TABLE follows (
                         follower_id BIGINT NOT NULL, --id cua nguoi follow
                         following_id BIGINT NOT NULL, --id cua nguoi duoc follow
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         PRIMARY KEY (follower_id, following_id),

                         CONSTRAINT chk_follow_self
                             CHECK (follower_id <> following_id)
);

--Notifications--

CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY, --id cua noti
                               receiver_id BIGINT NOT NULL, --id cua nguoi nhan dc noti
                               actor_id BIGINT NOT NULL, --id cua nguoi gui noti
                               type VARCHAR(50) NOT NULL, --type
                               reference_id BIGINT, --ref
                               is_read BOOLEAN NOT NULL DEFAULT FALSE, --noti da duoc doc chua
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


---create index----
CREATE INDEX idx_posts_author_created
    ON posts(author_id, created_at DESC);

CREATE INDEX idx_comments_post_created
    ON comments(post_id, created_at);

CREATE INDEX idx_notifications_receiver_created
    ON notifications(receiver_id, created_at DESC);

CREATE INDEX idx_follows_following
    ON follows(following_id);

---foreign key----
alter table  user_profiles
add constraint fk_user_profiles_account
foreign key (user_id) references accounts(id);


alter table comments
add constraint fk_comments_post
foreign key (post_id) references posts(id);

alter table comments
add constraint fk_comment_account
foreign key (author_id) references accounts(id);

alter table posts
add constraint fk_posts_accounts
foreign key (author_id) references accounts(id);

alter table follows
add constraint fk_follower_id_accounts
foreign key (follower_id) references accounts(id);

alter table follows
    add constraint fk_following_id_accounts
        foreign key (following_id) references accounts(id);

alter table post_likes
add constraint fk_post_likes_posts
foreign key (post_id) references posts(id);

alter table post_likes
    add constraint fk_post_likes_accounts
        foreign key (user_id) references accounts(id);






