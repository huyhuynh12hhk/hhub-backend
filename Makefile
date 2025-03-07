up-docker:
	test -f .env || touch .env
	docker compose -f docker-compose.dev.yml up -d