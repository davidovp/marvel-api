#!/bin/sh

# run the 'latest' docker image
docker run -p 8080:8080 -p 9090:9090 "com.davidov/marvel-api:latest"
