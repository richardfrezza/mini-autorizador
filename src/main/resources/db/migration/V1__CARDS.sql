create table if not exists cards (
   id int primary key auto_increment,
   number varchar(16) not null,
   password varchar(50) not null,
   balance decimal(12,2) not null default 0,
   dh_created_at timestamp null,
   dh_updated_at timestamp null,
   version int not null
);




