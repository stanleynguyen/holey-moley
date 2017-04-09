const router = require('express').Router();
const userApi = require('../api/user');
const userAuth = require('../middlewares/userAuth');

router.post('/register', userApi.registerNewUser);
router.post('/login', userApi.logInUser);
router.get('/info', userAuth, userApi.getUserInfo);
router.post('/buy', userAuth, userApi.buyNewItem);
router.post('/equip', userAuth, userApi.equipItem);

module.exports = router;