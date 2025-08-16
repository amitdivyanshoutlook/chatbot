#!/bin/bash

# Build the Docker image
sudo docker build -t perplexity .

# Stop and remove existing container if it is running
sudo docker stop perplexity || true
sudo docker rm perplexity || true

# Run the new container (no keystore volume mount needed)
sudo docker run --name perplexity -p 8443:8443 perplexity