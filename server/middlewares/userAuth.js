const jwt = require('jsonwebtoken');

module.exports = (req, res, next) => {
  const token = req.body.token || req.query.token || req.headers['x-access-token'];
  if (!token) return res.status(401).json({responseText: 'Unauthorized'});
  jwt.verify(token, process.env.SECRET, (err, decoded) => {
    if (err) return res.status(500).json(err);
    if (decoded.exp >= Date.now()) res.status(401).json({ responseText: 'Token Expired' });
    req.user = decoded;
    next();
  });
};
