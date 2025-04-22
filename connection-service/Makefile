
.PHONY: run
run:
	go run ./cmd/server/main.go

.PHONY: test
test:
	go test -v ./...

.PHONY: swaggen
swaggen:
	swag init -g ./cmd/server/main.go -o ./cmd/swag/docs

