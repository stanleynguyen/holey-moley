const auth = require('basic-auth');

module.exports = (req, res, next) => {
  const credentials = auth(req);
  if (!credentials) return res.status(401).json({ responseText: 'Unauthorized' });
  if (credentials.name === process.env.ADMIN_ACC && credentials.pass === process.env.ADMIN_PSSWD) {
    next();
  } else {
    res.status(401).json({ responseText: 'Wrong Credentials' });
  }
};