"use strict";
const app = require("./src/app");

const PORT = process.env.PORT || 8060;
const ENV = process.env.NODE_ENV;

const server = app.listen(PORT, () => {
	console.log(`Sever running on "${ENV??"default"}" profile.`);
	console.log(`Sever listening on port ${PORT}.`);
});
