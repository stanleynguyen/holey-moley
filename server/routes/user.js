const router = require('express').Router();
const userApi = require('../api/user');

router.post('/register', userApi.registerNewUser);
router.post('/login', userApi.logInUser);

module.exports = router;