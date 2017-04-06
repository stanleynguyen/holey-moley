const router = require('express').Router();
const userApiRoutes = require('./user');
const itemApiRoutes = require('./item');
const renderRoutes = require('./render');

router.use('/api/user', userApiRoutes);
router.use('/api/item', itemApiRoutes);
router.use('/', renderRoutes);

module.exports = router;