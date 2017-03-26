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
      const sidInfo = Object.keys(io.sockets.adapter.sids[socket.id]);
      const room = sidInfo[sidInfo.length-1];
      socket.broadcast.to(room).emit('message', `message from ${socket.id}`);
    });
    
    socket.on('item', (id) => {
      const sidInfo = Object.keys(io.sockets.adapter.sids[socket.id]);
      const room = sidInfo[sidInfo.length-1];
      socket.broadcast.to(room).emit('item', id);
    });
    
    socket.on('score', () => {
      const socketId = socket.id;
      const room = playerBook[socketId].getRoom();
      playerBook[socketId].incScore();
      socket.broadcast.to(room).emit('score', playerBook[socketId].getScore());
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
  countDown(io, room, moleEmitter);
}

function emitMole(io, roomName) {
  let mole;
  return setInterval(() => {
    mole = randMole();
    io.to(roomName).emit('mole', mole);
  }, 500);
}

function countDown(io, roomName, emitter) {
  setTimeout(() => {
    clearInterval(emitter);
    io.to(roomName).emit('end');
  }, 60000);
}

const randMole = () => Math.floor(Math.random() * 9);

// class to save player info
function Player(socket, socketRoom) {
  const id = socket.id;
  const room = socketRoom;
  let score = 0;
  this.getId = () => id;
  this.getRoom = () => room;
  this.getScore = () => score;
  this.incScore = () => score++;
}