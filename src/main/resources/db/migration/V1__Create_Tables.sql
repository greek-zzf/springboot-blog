create table user
(
    id                 int PRIMARY key AUTO_INCREMENT,
    username           VARCHAR(20) NOT NULL unique,
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_at         datetime
)