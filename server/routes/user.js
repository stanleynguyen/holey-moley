const router = require('express').Router();
const userApi = require('../api/user');
const userAuth = require('../middlewares/userAuth');

router.post('/register', userApi.registerNewUser);
router.post('/login', userApi.logInUser);
router.get('/', userAuth, userApi.proctectedRoute);

module.exports = router;