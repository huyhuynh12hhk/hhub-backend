"use strict";
const app = require("./src/app");

const PORT = process.env.PORT || 8060;

const server = app.listen(PORT, () => {
	return console.log(`Sever listening on port ${PORT}!`);
});