create table users(
        id serial primary key,
        username varchar(255) not null,
        firstname varchar(255) not null,
        lastname varchar(255) not null ,
        password varchar(255) not null
    );