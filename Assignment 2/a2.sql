-- Add below your SQL statements. 
-- You can create intermediate views (as needed). Remember to drop these views after you have populated the result tables.
-- You can use the "\i a2.sql" command in psql to execute the SQL commands in this file.

-- Query 1 statements
-- find the max among the neighbors with the same c1id
create view maxneighbourHeight(c1id, c2id, c2name, height) as
	select neighbour.country as c1id, neighbour.neighbor as c2id, cname as c2name, max(height)
	from neighbour join country on neighbour.neighbor = country.cid
	group by c1id;

insert into query1 (
	select c1id, country.name as c1name, c2id, c2name
	from country join maxneighbourHeight on cid = c1id
	order by c1name asc
	);

--drop view maxneighbourHeight;

-- Query 2 statements
--take all countries and subtract the ones that have ocean access
create view noOceanAccess(cid) as
	select country.cid
	from country
	minus 
	select oceanAccess.cid 
	from oceanAccess;
	
insert into query2(
	select cid, cname
	from noOceanAccess join country on noOceanAccess.cid = country.cid
	order by cname asc
	);
	
-- drop view noOceanAccess;

-- Query 3 statements
--group the countries that have no ocean access and return sum of the neighbours they have
create view totalNeighbour(c1id, total) as
	select country as c1id, sum(neighbor) as total
	from neighbour join noOceanAccess on neighbour.country = noOceanAccess.cid
	group by country;

-- find the one neighbour 
create view oneNeighbour(c1id, c2id) as
	select c1id, neighbor as c2id
	from totalNeighbour join neighbour on cid1 = country
	where total = 1;

insert into query3(
	select c1id, cname as c1name, c2id, c2name
	from country join 
		(select c1id, c2id, cname as c2name
		from oneNeighbour join country on c2id = cid)
			on country.cid = c1id								
	order by c1name asc
	);

-- drop view noOceanAccess;
-- drop view totalNeighbour;
-- drop view oneNeighbour;

-- Query 4 statements
--find the countries with direct access to the ocean
create view directAccess (dcid, dcname, oid, oname) as
	select cid as dcid, cname as dcname, oid, oname
	from country join 
		(select cid as c1id, ocean.oid, oname
		from oceanAccess join ocean on oceanAccess.oid = ocean.oid) as subq
			on country.cid = c1id;

--find the countries that have a neighbour with direct access
create view indirectAccess (cname, oname) as
	select cname, oname
	from country join 
		(select neighbour.country as c1id 
		from neighbour join directAccess on neighbour = dcid) as subquery
			on country.cid = c1id;	

--combine the countries that have direct and indirect access
insert into query4(
	select dcname as cname, oname
	from directAccess 
	union
	select cname, oname
	from indirectAccess
	order by cname asc, oname desc
	);

--drop view directAccess;
--drop view indirectAccess;

-- Query 5 statements
--find avg of each country over that time period and limit to the top 10 results in descending order
insert into query5(
	select country.cid, cname, avg(hdi) as avghdi
	from country join hdi on country.cid = hdi.cid
	where year >= 2009 and year <= 2013
	group by country.cid
	order by avghdi desc
	limit 10
	);

-- Query 6 statements
--insert into query6(
-- select cid, cname
-- order by cname asc
-- );

-- Query 7 statements
--get number of followers from each country for the different religions
create view numberFollowers(rid, rname, numberfromCountry) as
	select rid, rname, floor(population * rpercentage) as numberfromCountry
	from country join religion on country.cid = religion.cid;

--sum up all the followers from the same rid
insert into query7(
	select rid, rname, sum(numberfromCountry) as followers
	from numberFollowers 
	group by rid
	order by followers desc
	);

drop view numberFollowers;

-- Query 8 statements
--find the most popular language in a country
create view mostPopularLanguage(cid, cname, lid, lname, highest) as
	select country.cid, cname, lid, lname, max(lpercentage) as highest
	from country join language on country.cid = language.cid
	group by country.cid, language.cid;

--find the neighbouring country most popular language and join if the language is the same with country
insert into query8(
	select cname as c1name, c2name, lname
	from mostPopularLanguage join
		(select neighbour.country as c1id, cname as c2name, lid as l2id
		from neighbour join mostPopularLanguage on neighbour.neighbor = mostPopularLanguage.cid) as sub
			on mostPopularLanguage.cid = c1id and mostPopularLanguage.lid = l2id
	order by lname asc, c1name desc
	);

 -- drop view mostPopularLanguage;

-- Query 9 statements
--join all the counties with ocean access and show the deepest point
create view span(cid, cname, totalspan) as
	select cid, cname, height + max(deep) as totalspan
	from country left join 
		(select cid as c1id, ocean.depth as deep
		from oceanAccess join ocean on oceanAccess.oid = ocean.oid) as subquery
			on country.cid = c1id
	group by cid;
--the ones without ocean should be padded with null which is 0 when added?

--select the max one
insert into query9(
	select cname, max(totalspan)
	from span
	group by cid
	);
	
-- drop view span;

--group all the borderslength
create view borderSum (cid, total) as
	select country, sum(length) as total
	from neighbour
	group by country;

-- Query 10 statements
insert into query10(
	select cname, total as borderslength
	from country join borderSum on country.cid = borderSum.cid
	);
	
drop view borderSum;

