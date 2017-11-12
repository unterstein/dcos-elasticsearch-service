#!/bin/bash

cd $(dirname $0)

sbt clean
sbt universal:packageZipTarball
docker build --tag unterstein/eaas-web:latest .
