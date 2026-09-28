# S3-like Object Storage System

A simplified S3-like object storage system built as a hackathon prototype to demonstrate core distributed-system and system-design concepts such as load balancing, stateless API services, metadata management, object replication, versioning, checksums, and containerized deployment.

## Features

- Create and list buckets
- Upload objects
- Download objects
- List objects in a bucket
- Object versioning
- View object versions
- Delete objects
- SHA-256 checksums
- 3-way object replication
- PostgreSQL metadata storage
- Nginx load balancing
- Two stateless API instances
- Shared object storage between API instances
- Docker Compose deployment
- Health monitoring using Spring Boot Actuator

---

## System Architecture

```text
                         Client
                           |
                           v
                    +-------------+
                    |    Nginx    |
                    | Load Balancer|
                    +------+------+
                           |
                 +---------+---------+
                 |                   |
                 v                   v
            +---------+         +---------+
            |  API 1  |         |  API 2  |
            | Spring  |         | Spring  |
            |  Boot   |         |  Boot   |
            +----+----+         +----+----+
                 |                   |
                 +---------+---------+
                           |
              +------------+------------+
              |                         |
              v                         v
       +-------------+         +----------------+
       | PostgreSQL  |         | Object Storage |
       |  Metadata   |         |                |
       +-------------+         | Node 1         |
                               | Node 2         |
                               | Node 3         |
                               +----------------+
