"use strict";

const { Notification: notification } = require("../database/models/NotifyMessageSchema");
const { createMessage, getMessages } = require("../database/repositories/NotifyMessageRepository");


const saveMessage = async ({ type = "Default", receiverId, senderId, content, options = {} }) => {
	// if (type === "Default") {

	// }
	console.log(
		`${Date.now().toString("yyyy/MM/dd")}:: Saving message: ${type} - ${receiverId} - ${senderId} - ${content}`
	);

	const newNoti = await createMessage({
		noti_type: type,
		noti_content: content,
		noti_senderId: senderId,
		noti_receiverId: receiverId,
		noti_options: options,
	});

	return newNoti;
};

const getNotification = async ({ userId, cursor = null }) => {



	console.log(
		`${Date.now().toString("yyyy/MM/dd")}:: Saving message: ${type} - ${receiverId} - ${senderId} - ${content}`
	);

	const messages = await getMessages({
		userId,
		cursor
	});

	return newNoti;
};


module.exports = {
	saveMessage,
	getNotification
};
