const User = require('../models/user');
const jwt = require('jsonwebtoken');

module.exports = {
  
  // POST /api/user/register
  registerNewUser(req, res) {
    const { username, password } = req.body;
    User.findOne({ username }, (err, u) => {
      if (err) return res.status(500).json(err);
      if (u) return res.status(400).json({ message: 'Username Taken' });
      const newUser = new User({ 
        username,
        level: 1,
        exp_needed: 1 * 200,
        gold: 0,
        inventory: [],
        equipped: []
      });
      newUser.password = newUser.encryptPassword(password);
      newUser.save((err) => {
        if (err) return res.status(500).json(err);
        res.status(200).json(newUser);
      });
    });
  },
  
  // POST /api/user/login
  logInUser(req, res) {
    const { username, password } = req.body;
    User.findOne({ username }, (err, u) => {
      if (err) return res.status(500).json(err);
      if (!u) return res.status(400).json({ message: 'User Not Found' });
      if (!u.checkPassword(password, u.password)) return res.status(401).json({ message: 'Invalid credentials' });
      const token = jwt.sign(u._doc, process.env.SECRET, { expiresIn: '720h' });
      res.status(200).json({ token });
    });
  },
  
  // GET /api/user/info
  getUserInfo(req, res) {
    res.status(200).json(req.user);
  }
};