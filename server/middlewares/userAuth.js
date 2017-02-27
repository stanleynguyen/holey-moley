const jwt = require('jsonwebtoken');

module.exports = (req, res, next) => {
  const token = req.body.token || req.query.token || req.headers['x-access-token'];
  if (!token) return res.status(403).send('Unauthorized');
  jwt.verify(token, process.env.SECRET, (err, decoded) => {
    if (err) return res.status(500).json(err);
    req.user = decoded;
    next();
  });
};
