const jwt = require('jsonwebtoken');
const User = require('../models/user');
let waitingQueue = [];

module.exports = (server) => {
  const io = require('socket.io')(server);
  
  let playerBook = {};
  
  io.on('connection', (socket) => {
    
    socket.on('join', () => {
      waitingQueue.push(socket);
      checkQ(io, waitingQueue, playerBook);
    });
    
    socket.on('item', (id) => {
      const player = playerBook[socket.id];
      if (!player) return;
      const room = player.getRoom();
      socket.broadcast.to(room).emit('item', id);
    });
    
    socket.on('score', () => {
      const player = playerBook[socket.id];
      if (!player) return;
      const room = player.getRoom();
      player.incScore();
      socket.broadcast.to(room).emit('score', player.getScore());
    });
    
    socket.on('dead', () => {
      const deadPlayer = playerBook[socket.id];
      if (!deadPlayer) return;
      const winner = playerBook[deadPlayer.getOpponent()];
      if (winner) io.to(winner.getId()).emit('win');
      io.to(deadPlayer.getId()).emit('lose');
    });
    
    socket.on('end', (token) => {
      const player = playerBook[socket.id];
      if (!player) return;
      playerBook[socket.id] = null;
      let expGain = player.getScore();
      let goldGain = player.getScore();
      if (player.getWin()) {
        expGain += 30;
        goldGain += 20;
      }
      jwt.verify(token, process.env.SECRET, (err, decoded) => {
        if (err) return;
        User.findOne({ _id: decoded._id })
          .exec((err, u) => {
            if (err) return;
            if (!u) return;
            if (expGain > u.exp_needed) {
              u.level++;
              u.exp_needed = u.level * 200;
            } else {
              u.exp_needed -= expGain;
            }
            u.gold += goldGain;
            u.save();
          });
      });
    });
    
    socket.on('disconnect', () => {
      const qIdx = waitingQueue.indexOf(socket);
      if (qIdx !== -1) waitingQueue.splice(qIdx, 1);
      const disconnectedPlayer = playerBook[socket.id];
      if (!disconnectedPlayer) return;
      playerBook[socket.id] = null;
      const opponent = playerBook[disconnectedPlayer.getOpponent()];
      if (opponent) {
        io.to(opponent.getId()).emit('win');
        playerBook[disconnectedPlayer.getOpponent()] = null;
      }
    });
  });
  
  
};

function checkQ(io, playerBook) {
  if (waitingQueue.length < 2) return;
  const player1 = waitingQueue[0];
  const player2 = waitingQueue[1];
  if (playerBook[player1.id]) {
    waitingQueue = waitingQueue.slice(1);
    return;
  }
  if (playerBook[player2.id]) {
    waitingQueue = waitingQueue.slice(0,1);
    return;
  }
  waitingQueue = waitingQueue.slice(2);
  const room = player1.id + '#' + player2.id;
  player1.join(room);
  player2.join(room);
  playerBook[player1.id] = new Player(player1, player2, room);
  playerBook[player2.id] = new Player(player2, player1, room);
  io.to(room).emit('start');
  const moleEmitter = emitMole(io, room);
  countDown(io, room, moleEmitter, playerBook);
}

function emitMole(io, roomName) {
  let mole;
  return setInterval(() => {
    mole = randMole();
    io.to(roomName).emit('mole', mole);
  }, 1500);
}

function countDown(io, roomName, emitter, playerBook) {
  setTimeout(() => {
    clearInterval(emitter);
    const playerIds = roomName.split('#');
    const player1 = playerBook[playerIds[0]], player2 = playerBook[playerIds[1]];
    let winner, loser;
    if (!player1 || !player2) return;
    if (player1.getScore() > player2.getScore()) {
      winner = player1;
      player1.setWin();
      loser = player2;
    } else {
      winner = player2;
      player2.setWin();
      loser = player1;
    }
    io.to(winner.getId()).emit('win');
    io.to(loser.getId()).emit('lose');
  }, 60000);
}

const randMole = () => Math.floor(Math.random() * 9);

// class to save player info
function Player(socket, opponentSoc, socketRoom) {
  const id = socket.id;
  const opponent = opponentSoc;
  const room = socketRoom;
  let score = 0;
  let win = false;
  this.getId = () => id;
  this.getOpponent = () => opponent.id;
  this.getRoom = () => room;
  this.getScore = () => score;
  this.incScore = () => score++;
  this.getWin = () => win;
  this.setWin = () => win = true;
}