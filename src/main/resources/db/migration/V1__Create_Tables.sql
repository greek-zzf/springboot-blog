create table user
(
    id                 int PRIMARY key,
    username           VARCHAR(10),
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_ad         datetime
)