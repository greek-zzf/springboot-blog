create table blog
(
    id                 int PRIMARY key AUTO_INCREMENT,
    user_id           bigint,
    title varchar(100),
    description varchar(100),
    content text,
    created_at         datetime,
    updated_at         datetime
)