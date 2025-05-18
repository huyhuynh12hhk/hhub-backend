const { Notification: notification } = require("../models/NotifyMessageSchema");

const createMessage = async ({ type, content, senderId, receiverId, options }) => {
	const newNoti = await notification.create({
		noti_type: type,
		noti_content: content,
		noti_senderId: senderId,
		noti_receiverId: receiverId,
		noti_options: options,
	});

	return newNoti;
};

const getMessages = async ({ userId, cursor = null, limit = 10 }) => {
	const filter = { noti_receiverId: userId };

	if (cursor) {
		const { lastCreatedAt, lastId } = decodeCursor(cursorStr);
		filter.$or = [
			{ createdAt: { ["$lt"]: lastCreatedAt } },
			{ createdAt: lastCreatedAt, _id: { ["$lt"]: lastId } },
		];
	}

	const docs = await NotificationModel.find(filter)
		.sort({ createdAt: -1, _id: -1 })
		.limit(limit + 1)
		.exec();

	const hasMore = docs.length > limit;
	const items = hasMore ? docs.slice(0, -1) : docs;

	const nextCursor = hasMore
		? encodeCursor({
				lastCreatedAt: items[items.length - 1].createdAt,
				lastId: items[items.length - 1]._id,
		  })
		: null;

	return { items, nextCursor };
};

module.exports ={
	createMessage,
	getMessages
}
