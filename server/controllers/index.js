const path = require('path');

module.exports = {
  renderWelcome(req, res) {
    res.sendFile(path.resolve('views/welcome.html'));
  },
  
  renderGame(req, res) {
    res.sendFile(path.resolve('views/game.html'));
  }
};