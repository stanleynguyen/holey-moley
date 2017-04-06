const mongoose = require('mongoose');
mongoose.Promise = global.Promise;

const ItemSchema = new mongoose.Schema({
  id: { type: String, required: true, unique: true },
  img: { type: String, required: true },
  required_level: { type: Number, required: true },
  price: { type: Number, required: true }
});

module.exports = mongoose.model('Item', ItemSchema);