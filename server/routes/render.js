const router = require('express').Router();
const controller = require('../controllers');

router.get('/', controller.renderWelcome);
router.get('/game', controller.renderGame);

module.exports = router;