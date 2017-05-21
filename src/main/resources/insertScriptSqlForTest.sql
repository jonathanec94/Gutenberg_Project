create table books
(
	id serial not null
		constraint table_name_pkey
			primary key,
	title varchar(500),
	author varchar(500)
);

create unique index table_name_id_uindex on books (id);

create table cities
(
	id serial not null,
	name varchar(500) not null
		constraint cities_pkey
			primary key,
	latitude numeric,
	longitude numeric,
	geom geometry(Point,4326)
);

create unique index cities_id_uindex
	on cities (id);

create unique index cities_name_uindex
	on cities (name);

create table cityinbook
(
	cityname varchar(500)
		constraint cityinbook_city
			references cities,
	bookid integer
		constraint cityinbook_book
			references books
);




INSERT INTO books(id,title, author) VALUES
  (1, 'book1', 'author1'),
  (2, 'book2', 'author2'),
  (3, 'book3', 'author3'),
  (4, 'book4', 'author4');

INSERT INTO cities(id, name, latitude, longitude, geom) VALUES
  (1, 'copenhagen', 55.67594,12.56553, ST_GeomFromText('POINT(55.67594 12.56553)', 4326)),
  (2, 'london', 42.98339,-81.23304, ST_GeomFromText('POINT(42.98339 -81.23304)', 4326)),
  (3, 'stockholm', 59.33258,18.0649, ST_GeomFromText('POINT(59.33258 18.0649)', 4326)),
  (4, 'berlin', 52.52437,12.56553, ST_GeomFromText('POINT(52.52437 13.41053)', 4326));


INSERT INTO cityinbook (cityname, bookid) VALUES
('copenhagen', 1),
('berlin', 1),
('berlin', 2),
('london', 3),
('stockholm', 3),
('copenhagen', 4),
('london', 4),
('stockholm', 4),
('berlin', 4);


