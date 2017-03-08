const router = require('express').Router();
const controller = require('../controllers');

router.get('/', controller.renderWelcome);

module.exports = router;