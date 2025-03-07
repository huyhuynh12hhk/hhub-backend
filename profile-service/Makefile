.PHONY: install
install:
	poetry install

.PHONY: startapp
startapp:
	@poetry run python -c "import os; os.makedirs('profile_service/apps/$(filter-out $@,$(MAKECMDGOALS))', exist_ok=True)"
	@poetry run python -m profile_service.manage startapp $(filter-out $@,$(MAKECMDGOALS)) profile_service/apps/$(filter-out $@,$(MAKECMDGOALS))

.PHONY: migrate
migrate:
	poetry run python -m profile_service.manage migrate

.PHONY: migrations
migrations:
	poetry run python -m profile_service.manage makemigrations

.PHONY: superuser
superuser:
	poetry run python -m profile_service.manage createsuperuser

.PHONY: shell
shell:
	poetry run python -m profile_service.manage shell

.PHONY: run
run:
	poetry run python -m profile_service.manage runserver

.PHONY: test
test:
	poetry run python -m profile_service.manage test


.PHONY: update
update: install migrate;


%:
	@: