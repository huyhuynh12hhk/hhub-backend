const { getNotification } = require("../services/NotificationService");

class NotificationController {
	getNotification = async (req, res, next) => {
		getNotification({ userId: req.params.userId, cursor: req.query.cursor });
	};
}

module.exports = new NotificationController();
