const Item = require('../models/item');

module.exports = {
  
  // GET /api/item/all 
  getAllItems(req, res) {
    Item.find({}, (err, items) => {
      if (err) return res.status(500).json(err);
      res.status(200).json(items);
    });
  },
  
  // POST /api/item
  createNewItem(req, res) {
    const { id, img, required_level, price } = req.body;
    const newItem = new Item({ id, img, required_level, price });
    newItem.save((err) => {
      if (err) return res.status(500).json(err);
      res.status(200).json(newItem);
    });
  },
  
  // PUT /api/item
  editItem(req, res) {
    const { id, img, required_level, price } = req.body;
    Item.findOne({ id }, (err, i) => {
      if (err) return res.status(500).json(err);
      if (!i) return res.status(404).json({ responseText: 'Not Found' });
      i.img = img;
      i.required_level = required_level;
      i.price = price;
      i.save((err) => {
        if (err) return res.status(500).json(err);
        res.status(200).json(i);
      });
    });
  },
  
  // DELETE /api/item
  deleteItem(req, res) {
    const { id } = req.body;
    Item.findOneAndRemove({ id }, (err) => {
      if (err) return res.status(500).json(err);
      res.status(200).json({});
    });
  }
};