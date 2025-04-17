
.PHONY: run
run:
	go run ./cmd/server/main.go

.PHONY: di
di:
	cd ./internal/initializer/di
	wire	

.PHONY: swaggen
swaggen:
	swag init -g ./cmd/server/main.go -o ./cmd/swag/docs

