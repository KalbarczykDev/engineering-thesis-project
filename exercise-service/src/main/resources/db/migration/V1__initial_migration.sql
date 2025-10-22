create table users
(
    id             bigint auto_increment primary key,
    username       varchar(255) not null,
    password       varchar(255) not null,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table exercises
(
    id             bigint auto_increment
        primary key,
    name           varchar(255)  not null,
    category       varchar(255)  not null,
    muscle_group   varchar(255)  not null,
    equipment      varchar(255)  not null,
    description    text          null,
    image_url      varchar(2048) null,
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_at DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);



