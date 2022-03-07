create database if not exists auto_crash;

create table types (
    id serial primary key,
    name text
);

create table rules (
    id serial primary key,
    name varchar(255) not null
);

create table accidents (
    id serial primary key,
    name varchar(2000) not null,
    text text not null,
    address varchar(255) not null,
    type_id int references types(id) not null
);

create table accident_rules (
    accident_id int references accidents(id) on delete cascade not null,
    rule_id int references rules(id) not null
);

insert into types (name) values ('Две машины');
insert into types (name) values ('Машина и человек');
insert into types (name) values ('Машина и велосипедист');

insert into rules (name) values ('Закон 1');
insert into rules (name) values ('Закон 2');
insert into rules (name) values ('Закон 2');

insert into accidents (name, text, address, type_id) values ('Авария',
                                                             'Столкнулись машины audi и opel',
                                                             'Solovetskaya, 33', 1);

insert into accident_rules (accident_id, rule_id) values (1, 1);
insert into accident_rules (accident_id, rule_id) values (1, 3);

