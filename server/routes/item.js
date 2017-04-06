const router = require('express').Router();
const itemApi = require('../api/item');
const adminAuth = require('../middlewares/adminAuth');

router.get('/all', itemApi.getAllItems);
router.post('/', adminAuth, itemApi.createNewItem);
router.put('/', adminAuth, itemApi.editItem);
router.delete('/', adminAuth, itemApi.deleteItem);

module.exports = router;