const router = require('express').Router();
const itemApi = require('../api/item');
const userAuth = require('../middlewares/userAuth');
const adminAuth = require('../middlewares/adminAuth');

router.get('/all', userAuth, itemApi.getAllItems);
router.post('/', adminAuth, itemApi.createNewItem);
router.put('/', adminAuth, itemApi.editItem);
router.delete('/', adminAuth, itemApi.deleteItem);

module.exports = router;