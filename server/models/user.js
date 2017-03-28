const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const UserSchema = new mongoose.Schema({
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  level: { type: Number, required: true },
  exp_needed: { type: Number, required: true }
});

UserSchema.methods.encryptPassword = (p) => bcrypt.hashSync(p, 10);

UserSchema.methods.checkPassword = (p, h) => bcrypt.compareSync(p, h);

module.exports = mongoose.model('User', UserSchema);