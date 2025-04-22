"use strict";
require("dotenv").config({ path: `.env.${process.env.NODE_ENV}` });
const { runConsumer, ensureTopicExists } = require("./messaging/kafka/consumer");

const express = require("express");

const app = express();
require("./database/mongoInit");
ensureTopicExists()
	.then(() => runConsumer())
	.catch(console.error);

app.use((req, res, next) => {
	error = new Error("Something went wrong");
	next(error);
});
app.use((error, req, res, next) => {
	console.log("Got an exception: ", error);
});

module.exports = app;
