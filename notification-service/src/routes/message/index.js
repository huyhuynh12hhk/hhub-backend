"use strict";

const express = require("express");
const NotificationController = require("../../controllers/NotificationController");
const router = express.Router();


router.get("/:userId?cursor=:cursor", NotificationController.getNotification);




module.exports = router;
