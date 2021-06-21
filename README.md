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

```cql
CREATE TABLE IF NOT EXISTS topsy_auth.video (
    video_id text,
    title text,
    url text,
    PRIMARY KEY ((title), video_id)
);
```

Insert data:

```cql
USE topsy_auth;

INSERT INTO video (video_id			, title						, url)
VALUES            ('first_video_id'   , 'Zymposium - Full Stack Development', 'https://www.youtube.com/watch?v=VqTqFhU9Mrs');

INSERT INTO video (video_id, title)
VALUES            (now() 	, 'Zymposium - ZIO Schema');

// Inserts will also cause upsert
INSERT INTO video (video_id			, title				, url)
VALUES           ('first_video_id'    , 'ZIO WORLD - ZScheduler' , 'https://www.youtube.com/watch?v=GaWcmRHS-qI');
```

You can also use updates:
```cql
UPDATE video
SET url = 'https://www.youtube.com/watch?v=6A1SA5Be9qw&t=5182s'
WHERE video_id = 'first_video_id';
```
