const jwt = require('jsonwebtoken');
const User = require('../models/user');

module.exports = (server) => {
  const io = require('socket.io')(server);
  let waitingQueue = [];
  let playerBook = {};
  
  io.on('connection', (socket) => {
    
    socket.on('join', () => {
      waitingQueue.push(socket);
      checkQ(io, waitingQueue, playerBook);
    });
    
    socket.on('message', () => {
      const room = playerBook[socket.id].getRoom();
      socket.broadcast.to(room).emit('message', `message from ${socket.id}`);
    });
    
    socket.on('item', (id) => {
      const room = playerBook[socket.id].getRoom();
      socket.broadcast.to(room).emit('item', id);
    });
    
    socket.on('score', () => {
      const socketId = socket.id;
      const room = playerBook[socketId].getRoom();
      playerBook[socketId].incScore();
      socket.broadcast.to(room).emit('score', playerBook[socketId].getScore());
    });
    
    socket.on('end', (token) => {
      const player = playerBook[socket.id];
      let expGain = player.getScore();
      if (player.getWin()) expGain += 30;
      jwt.verify(token, process.env.SECRET, (err, decoded) => {
        if (err) return;
        User.findOne({ _id: decoded._id })
          .then((u) => {
            if (expGain > u.exp_needed) {
              u.level++;
              u.exp_needed = u.level * 200;
            } else {
              u.exp_needed -= expGain;
            }
            u.save();
          });
      });
    });
    
    socket.on('disconnect', () => {
      const qIdx = waitingQueue.indexOf(socket);
      if (qIdx !== -1) waitingQueue.splice(qIdx, 1);
    });
  });
  
  
};

function checkQ(io, waitingQueue, playerBook) {
  if (waitingQueue.length < 2) return;
  const player1 = waitingQueue[0];
  const player2 = waitingQueue[1];
  waitingQueue = waitingQueue.slice(2);
  const room = player1.id + '#' + player2.id;
  player1.join(room);
  player2.join(room);
  playerBook[player1.id] = new Player(player1, room);
  playerBook[player2.id] = new Player(player2, room);
  io.to(room).emit('start');
  const moleEmitter = emitMole(io, room);
  countDown(io, room, moleEmitter, playerBook);
}

function emitMole(io, roomName) {
  let mole;
  return setInterval(() => {
    mole = randMole();
    io.to(roomName).emit('mole', mole);
  }, 500);
}

function countDown(io, roomName, emitter, playerBook) {
  const playerIds = roomName.split('#');
  const player1 = playerBook[playerIds[0]], player2 = playerBook[playerIds[1]];
  let winner, loser;
  if (player1.getScore() > player2.getScore()) {
    winner = player1;
    player1.setWin();
    loser = player2;
  } else {
    winner = player2;
    player2.setWin();
    loser = player1;
  }
  setTimeout(() => {
    clearInterval(emitter);
    io.to(winner.getId()).emit('win');
    io.to(loser.getId()).emit('lose');
  }, 60000);
}

const randMole = () => Math.floor(Math.random() * 9);

// class to save player info
function Player(socket, socketRoom) {
  const id = socket.id;
  const room = socketRoom;
  let score = 0;
  let win = false;
  this.getId = () => id;
  this.getRoom = () => room;
  this.getScore = () => score;
  this.incScore = () => score++;
  this.getWin = () => win;
  this.setWin = () => win = true;
}