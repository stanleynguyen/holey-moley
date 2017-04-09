const User = require('../models/user');
const Item = require('../models/item');
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
    User.findOne({ _id: req.user._id })
      .populate('inventory equipped')
      .exec((err, u) => {
        if (err) return res.status(500).json(err);
        res.status(200).json(u);
      });
  },
  
  // POST /api/user/buy
  buyNewItem(req, res) {
    const { item } = req.body;
    User.findOne({ _id: req.user._id }, (err, u) => {
      if (err) return res.status(500).json(err);
      Item.findOne({ _id: item }, (err, i) => {
        if (err) return res.status(500).json(err);
        if (!i) return res.status(404).json({ responseText: 'Item Not Found' });
        if (u.inventory.indexOf(item) !== -1) return res.status(403).json({ responseText: 'Bought liao!' });
        if (u.level < i.required_level) return res.status(403).json({ responseText: 'You need to level up' });
        if (u.gold < i.price) return res.status(403).json({ responseText: 'Not enough gold' });
        // eligible to buy
        u.gold -= i.price;
        u.inventory = [...u.inventory, i._id];
        u.save((err) => {
          if (err) return res.status(500).json(err);
          res.status(200).json([i]);
        });
      });
    });
  },
  
  // POST /api/user/equip
  equipItem(req, res) {
    const { item } = req.body;
    User.findOne({ _id: req.user._id }, (err, u) => {
      if (err) return res.status(500).json(err);
      if (u.inventory.indexOf(item) === -1) return res.status(404).json({ responseText: 'Item Not In Inventory' });
      // check if item alr equipped, if so unequip it
      const idx = u.equipped.indexOf(item);
      let equipped;
      if (idx === -1) {
        // handle equip
        if (u.equipped.length >= 3) return res.status(403).json({ responseText: 'Cant hold more than 3. Unequip something first!' });
        u.equipped = [...u.equipped, item];
        equipped = true;
      } else {
        // handle unequip
        u.equipped = [
          ...u.equipped.slice(0, idx),
          ...u.equipped.slice(idx+1)
        ];
        equipped = false;
      }
      u.save((err) => {
        if (err) return res.status(500).json(err);
        res.status(200).json({ equipped });
      });
    });
  }
};