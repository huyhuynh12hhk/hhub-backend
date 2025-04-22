FROM golang:1.24.2-alpine3.21 AS build

WORKDIR /build

COPY . .

RUN go mod download

# RUN go build -ldflags "-s -w" -o bin ./cmd/server #Disable debugging with lightweight size
RUN go build -o app ./cmd/server


FROM alpine:3.21

COPY ./configs /configs

COPY --from=build build/app /

# EXPOSE 8224

ENTRYPOINT [ "/app" ]