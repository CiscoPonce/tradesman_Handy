#!/bin/bash

# Install dependencies
npm install

# Clean dist directory if it exists
rm -rf dist

# Install @nestjs/cli globally
npm install -g @nestjs/cli

# Build the application
npm run build
