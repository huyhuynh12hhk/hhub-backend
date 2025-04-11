"use strict";

const mongoose = require("mongoose");

const DOCUMENT_NAME = "Notification"; // Set the collection name
const COLLECTION_NAME = "notifications"; // Set the collection name

// Declare the Schema of the Mongo model
var notificationSchema = new mongoose.Schema({
	noti_type: { type: String, required: true, enum: ["Default", "Message", "FriendRequest", "GroupInvite"] },
	noti_content: { type: String, required: true },
	noti_senderId: { type: String, required: true },
	noti_receiverId: { type: String, required: true },
	noti_options: { type: Object, default: {} },
},{
	timestamps: true,
	collection: COLLECTION_NAME,
});

//Export the model
module.exports = {notification: mongoose.model(DOCUMENT_NAME, notificationSchema)}
