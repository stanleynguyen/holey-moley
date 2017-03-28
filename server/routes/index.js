const router = require('express').Router();
const userApiRoutes = require('./user');
const renderRoutes = require('./render');

router.use('/api/user', userApiRoutes);
router.use('/', renderRoutes);

module.exports = router;