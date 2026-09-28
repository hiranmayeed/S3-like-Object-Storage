# S3-like Object Storage

A simplified S3-like object storage service built as a hackathon
prototype to demonstrate core object-storage and system-design concepts.

## Features

-   Bucket creation and listing
-   Object upload and download
-   Object listing
-   Object deletion
-   Object versioning
-   SHA-256 checksums
-   PostgreSQL-based metadata storage
-   Three-way object replication
-   Stateless API instances
-   Nginx load balancing
-   Docker Compose deployment
-   Spring Boot Actuator health monitoring

## Architecture

``` text
Client
   |
   v
Nginx Load Balancer
   |
   +-------------------+
   |                   |
   v                   v
API Instance 1      API Instance 2
   |                   |
   +---------+---------+
             |
       +-----+-----+
       |           |
       v           v
 PostgreSQL    Object Storage
 Metadata      node1 / node2 / node3
```

The API layer is stateless. PostgreSQL stores object metadata, while
object data is stored separately and replicated across three simulated
storage nodes.

## Technology Stack

-   Java 17
-   Spring Boot
-   Spring Data JPA / Hibernate
-   PostgreSQL
-   Nginx
-   Docker
-   Docker Compose
-   Maven

## Running Locally with Docker

### Prerequisites

-   Docker Desktop
-   Git
-   Java 17
-   Maven

### Clone and run

``` bash
git clone https://github.com/hiranmayeed/S3-like-Object-Storage
cd s3-object-storage-team
mvn clean package -DskipTests
docker compose up -d --build
```

Nginx is exposed on port `80`.

### Check health

``` bash
curl.exe http://localhost/actuator/health
```

Expected:

``` json
{"status":"UP"}
```

## API Usage

### Create a bucket

``` bash
curl.exe -X POST "http://localhost/buckets?name=demo"
```

### List buckets

``` bash
curl.exe "http://localhost/buckets"
```

### Upload an object

``` bash
curl.exe -X POST "http://localhost/buckets/demo/objects/test.txt" -H "Content-Type: application/octet-stream" --data-binary "Hello S3"
```

The response contains the object ID, name, version, size, SHA-256
checksum, storage path, and creation time.

### Download an object

``` bash
curl.exe "http://localhost/buckets/demo/objects/test.txt"
```

### List objects

``` bash
curl.exe "http://localhost/buckets/demo/objects"
```

### View versions

``` bash
curl.exe "http://localhost/buckets/demo/objects/test.txt/versions"
```

Uploading the same object name again creates a new version.

### Delete an object

``` bash
curl.exe -X DELETE "http://localhost/buckets/demo/objects/test.txt"
```

## Object Storage and Replication

Each uploaded object receives a UUID as its storage identifier.

Three copies are maintained:

``` text
storage-data/
├── node1/<object-uuid>
├── node2/<object-uuid>
└── node3/<object-uuid>
```

SHA-256 is calculated for uploaded objects and stored as metadata.

For this prototype, the three storage nodes are simulated as
directories. A production implementation would use separate storage
nodes or failure domains.

## Versioning

Uploading the same object name again increments its version:

``` text
test.txt
  ├── version 1 -> UUID A
  └── version 2 -> UUID B
```

A normal GET returns the latest version. The `/versions` endpoint
exposes version history.

## Load Balancing

Nginx distributes requests between:

``` text
api1:8080
api2:8080
```

Both API instances use the same PostgreSQL database and shared
object-storage volume.

This allows an object uploaded through one API instance to be retrieved
through the other.

## Verification

The prototype has been tested for:

-   Bucket creation and listing
-   Object upload, download, and listing
-   Object versioning
-   Object deletion
-   SHA-256 checksum generation
-   Three-way replication
-   Shared storage between API instances
-   Nginx load balancing
-   Retrieval through the remaining API instance when one API is stopped
-   Spring Boot health endpoint

## Stopping the System

``` bash
docker compose down
```

This preserves named Docker volumes.

To also remove database and object-storage volumes:

``` bash
docker compose down -v
```

Use `down -v` only when you intentionally want to delete the persisted
data.

## Limitations

This is a hackathon prototype, not a production-scale object-storage
platform.

It does not currently implement:

-   Multipart uploads
-   Erasure coding
-   Raft/Paxos consensus
-   Distributed metadata sharding
-   Real multi-machine storage nodes
-   Advanced IAM
-   Production failure-domain management
-   Large-scale capacity management
-   Background garbage collection and compaction

The three storage nodes are simulated locally, and both API instances
share the same Docker storage volume.

## Future Improvements

-   Multipart uploads
-   Authentication and fine-grained authorization
-   Metadata sharding
-   Consistent hashing for data placement
-   Erasure coding
-   Replication repair
-   Object integrity verification
-   Distributed storage nodes
-   Metrics and monitoring
-   Rate limiting
-   Cloud deployment

## License

This project was developed as a hackathon prototype.
