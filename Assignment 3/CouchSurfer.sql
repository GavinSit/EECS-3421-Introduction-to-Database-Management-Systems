create table User (
	UID int primary key,
	birthday date not null,
	screen_name varchar (50) not null,
	email varchar (100) not null,
	password varchar(20) not null
);

create table Profile (
	UID int,
	first_name varchar(50),
	last_name varchar(50),
	interests varchar(500),
	pets varchar (500),
	music varchar (500),
	more_about_me varchar(500),
	foreign key (UID) references User(UID)
		on delete cascade
);	

create table Friend_Request(
	UID int,
	UID2 int,
	date_of_meeting date,
	friendship_type char(20) not null,
	show_friend_list boolean,
	foreign key (UID) references User(UID)
		on delete cascade,
	foreign key (UID2) references User(UID)
		on delete cascade
);

create table Connection (
	connectID int primary key, 
	UID int, 
	UID2 int,
	connection_instance varchar(100),
	foreign key (UID) references User(UID)
		on delete cascade,
	foreign key (UID2) references User(UID)
		on delete cascade,
);

create table Host (
	HID int primary key,
	UID int, 
	foreign key (UID) references User(UID)
		on delete cascade,
);

create table Location(
	LID int primary key,
	HID,
	current_capacity int not null,
	max_capacity int not null, 
	city varchar(30) not null,
	postal_code char(6),
	street_number int not null,
	street_address varchar(30) not null,
	province varchar (30) not null,
	country varchar(30) not null,
	foreign key (HID) references Host(HID)
		on delete cascade
);

create table Request (
	RID int primary key,
	UID int,
	LID int,
	arrival_date date not null,
	departure_date date not null, 
	number_of_surfers int not null,
	status char (8) not null,
	foreign key (UID) references User(UID)
		on delete cascade,
	foreign key (LID) references Location(LID)
		on delete cascade
);

create table Testimonials (
	TID int primary key,
	UID int,
	tohost int,
	rating int,
	comments varchar (500),
	foreign key (UID) references User(UID)
		on delete cascade,
	foreign key (tohost) references Host(HID)
);

create table Group (
	GID int primary key, 
	UID int,
	description varchar(200),
	group_type varchar(20) not null,
	category varchar (20) not null,
	foreign key (UID) references User(UID) --dont delete group if UID is deleted
);

create table Members (
	UID int,
	GID int,
	foreign key (UID) references User(UID)
		on delete cascade,
	foreign key (GID) references Group(GID)
		on delete cascade,
	primary key (UID, GID)
);

create table Posts(
	PostID int primary key,
	GID int,
	title varchar(30) not null,
	content varchar (500) not null,
	foreign key (GID) references Group(GID)
		on delete cascade,
);

create table Comments (
	CID int primary key,
	PostID int,
	content not null,
	time_of_comment timestamp not null,
	foreign key (PostID) references Posts(PostID)
		on delete cascade,
);

create table Event (
	EID int primary key,
	UID int,
	event_name varchar(50) not null,
	location varchar(200) not null,
	date_event date not null,
	time_event time not null,
	description varchar (500) not null,
	foreign key (UID) references User(UID)
		on delete cascade,
);

create table Moderator (
	MID int primary key,
	UID int,
	foreign key (UID) references User(UID)
		on delete cascade,
);

create table Report (RID
	RID int primary key,
	GID int,
	PostID int,
	reason_for_report varchar(200),
	foreign key (GID) references Group(GID),
	foreign key (PostID) references Posts(PostID)
);