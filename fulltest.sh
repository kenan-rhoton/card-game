docker-compose down
docker-compose build
docker-compose run backend lein test
docker-compose run e2e test-delayed
