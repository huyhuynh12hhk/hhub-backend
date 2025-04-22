"use strict";

const host = process.env.HOST || "localhost";



const appSettings = {
	port: process.env.PORT || 3000,
	host: host,
	debug: process.env.DEBUG || true,
	kafka: {
		clientId: process.env.KAFKA_CLIENT_ID || "notification-app",
		brokers: [process.env.KAFKA_BROKERS],
		groupId: process.env.KAFKA_GROUP_ID || "notification-group",
		topic: process.env.KAFKA_TOPIC || "notification-test-topic",
	},
	database: {
		host: process.env.DB_HOST || "localhost",
		port: process.env.DB_PORT || 27017,
		name: process.env.DB_NAME || "db_noti_test",
		user: process.env.DB_USER || "admin",
		password: process.env.DB_PASSWORD || "password",
		appName: process.env.DB_APP_NAME ? "&appName=" + process.env.DB_APP_NAME : "",
	},
};

module.exports = appSettings;
