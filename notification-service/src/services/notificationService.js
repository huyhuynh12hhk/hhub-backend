"use strict";

const { notification } = require("../database/models/notifyMessageSchema");

const saveMessage = async ({ type = "Default", receiverId, senderId, content, options = {} }) => {
	// if (type === "Default") {

	// }
	console.log(`${Date.now().toString("yyyy/MM/dd")}:: Saving message: ${type} - ${receiverId} - ${senderId} - ${content}`);


	const newNoti = await notification.create({
		noti_type: type,
		noti_content: content,
		noti_senderId: senderId,
		noti_receiverId: receiverId,
		noti_options: options,
	});

	return newNoti;
};


module.exports = {
	saveMessage,
};
