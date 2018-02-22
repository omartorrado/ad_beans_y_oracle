alter session set nls_date_format = 'dd.mm.yyyy hh24:mi:ss';
drop table pedidos;
drop table ventas;
drop table productos;
purge recyclebin; 
create table productos(
id integer,
descripcion varchar2(50),
stockactual integer,
stockminimo integer,
pvp float,
primary key (id)
);
insert into productos values(1,'boligrafo',100,5,2);
insert into productos values(2,'lapiz',50,5,2);
insert into productos values(3,'libreta',50,5,2);
insert into productos values(4,'hoja',100,5,2);
insert into productos values(5,'marcador',100,5,2);

create table ventas(
id integer,
idproducto integer,
fechaventa date,
cantidad integer,
primary key(id),
foreign key (idproducto) references productos(id)
);


create table pedidos(
id integer,
idproducto integer,
fechapedido date,
cantidad integer,
primary key(id),
foreign key (idproducto) references productos(id)
);
