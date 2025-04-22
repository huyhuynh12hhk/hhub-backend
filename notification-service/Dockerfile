FROM node:20.11.1-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .


FROM node:20.11.1-alpine
WORKDIR /app
COPY --from=build /app ./
COPY package*.json ./
RUN npm install --omit=dev
EXPOSE 8060

# Run the application.
CMD ["node", "server.js"]
