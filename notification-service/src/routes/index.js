"use strict";

const express = require("express");
const { apiKey, permission, errorHandler } = require("../middlewares/auth.middleware");

const router = express.Router();

//validate api key
router.use(apiKey);

//validate permissions
router.use(permission("0000"));

router.use("messages", require("./message"));

module.exports = router;
