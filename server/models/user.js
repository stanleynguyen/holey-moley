const mongoose = require('mongoose');
mongoose.Promise = global.Promise;
const bcrypt = require('bcrypt');

const equipmentLimit = (v) => v.length <= 3;

const UserSchema = new mongoose.Schema({
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  level: { type: Number, required: true },
  exp_needed: { type: Number, required: true },
  gold: { type: Number, required: true, min: 0 },
  inventory: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Item'
    }
  ],
  equipped: {
    type: [{
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Item'
    }],
    validate: {
      validator: equipmentLimit, 
      message: '{PATH} exceeds the limit of 3'
    }
  }
});

UserSchema.methods.encryptPassword = (p) => bcrypt.hashSync(p, 10);

UserSchema.methods.checkPassword = (p, h) => bcrypt.compareSync(p, h);

module.exports = mongoose.model('User', UserSchema);