# Topsy Auth

## Cassandra setup

Use docker-compose to spin up Cassandra and use the following to log into the Cassandra node
and get cqlsh running:

```sh
docker exec -it datastax-enterprise cqlsh
```

You can also utilize DataStax Studio by visiting [localhost:9091](http://localhost:9091) for a better UI experience
and creating a new connection to `datastax-enterprise` which is the DataStax Enterprise container running in the same
Docker network.

### Data model

Create keyspace (houses tables) for local development:

```cql
CREATE KEYSPACE topsy_auth
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

USE topsy_auth;
CREATE TABLE IF NOT EXISTS topsy_auth.user (
    id text,
    name text,
    email text,
    PRIMARY KEY (id)
);
```

### Examples

Create table:

```cqlsh
CREATE TABLE IF NOT EXISTS topsy_auth.user (
    last_name text,
    first_name text,
    middle_initial text,
    title text,
    PRIMARY KEY ((last_name), first_name)
);
```

Insert data:

```cqlsh
USE topsy_auth;

INSERT INTO user (first_name, last_name)
VALUES           ('Mary'    , 'Rodriguez');

INSERT INTO user (first_name, last_name, title)
VALUES           ('Wanda'   , 'Nguyen' , 'Mrs.');

INSERT INTO user (first_name, middle_initial, last_name, title)
VALUES           ('Bill'    , 'S'           , 'Nguyen' , 'Mr.');

// Causes upserts
INSERT INTO user (first_name, middle_initial, last_name, title)
VALUES           ('Bill'    , 'R'           , 'Nguyen' , 'Mr.');
```

Inserts will also cause upserts:
```cqlsh
INSERT into user (first_name, last_name, title)
VALUES              ('Mary', 'Rodriguez',  'Mrs.');
```

You can also use updates:
```cqlsh
UPDATE user
SET middle_initial = 'Q'
WHERE
  first_name = 'Mary' AND
  last_name = 'Rodriguez';
```
