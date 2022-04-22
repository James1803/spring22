drop table if exists post;
drop table if exists user;

create table post (id integer not null auto_increment, content varchar(255) not null, posted_at date not null, title varchar(32) not null, user_id integer, primary key (id));

create table user (id integer not null auto_increment, email varchar(255) not null, username varchar(16) not null, primary key (id));

alter table post add constraint FK72mt33dhhs48hf9gcqrq4fxte foreign key (user_id) references user (id);
