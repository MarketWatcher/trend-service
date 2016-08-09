CREATE KEYSPACE IF NOT EXISTS trends WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE trends;
CREATE TABLE IF NOT EXISTS trend (
 id timeuuid,
 alert_id timeuuid,
 count int,
 process_date timestamp,
 PRIMARY KEY(alert_id, id)
);
CREATE INDEX IF NOT EXISTS by_id ON trends.trend("id");
INSERT INTO trend (id, alert_id, count, process_date) VALUES (080ab5ee-57f5-11e6-8b77-86f30ca893d3, 187fc5b8-57f5-11e6-8b77-86f30ca893d3, 666, 1470061925);
