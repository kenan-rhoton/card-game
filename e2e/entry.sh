#!/bin/bash
if [[ "$1" == "test" ]]; then
    /opt/testcafe/docker/testcafe-docker.sh firefox /tests/*.js
elif [[ "$1" == "test-delayed" ]]; then
    /opt/testcafe/docker/testcafe-docker.sh firefox /tests/*.js --app 'true' --app-init-delay 20000
else
    eval "$@"
fi
