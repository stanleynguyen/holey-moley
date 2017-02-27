const router = require('express').Router();
const userApiRoutes = require('./user');

router.use('/api/user', userApiRoutes);

module.exports = router;