module.exports = (server) => {
  const io = require('socket.io')(server);
  let waitingQueue = [];
  
  io.on('connection', (socket) => {
    socket.on('join', () => {
      waitingQueue.push(socket);
      checkQ(io, waitingQueue);
    });
    
    socket.on('message', () => {
      console.log('got mes');
      const sidInfo = Object.keys(io.sockets.adapter.sids[socket.id]);
      const room = sidInfo[sidInfo.length-1];
      socket.broadcast.to(room).emit('message', `message from ${socket.id}`);
    });
    
    socket.on('disconnect', () => {
      const qIdx = waitingQueue.indexOf(socket);
      if (qIdx !== -1) waitingQueue.splice(qIdx, 1);
    });
  });
  
  
};

function checkQ(io, waitingQueue) {
  console.log(waitingQueue.map((w) => w.id));
  if (waitingQueue.length < 2) return;
  const player1 = waitingQueue[0];
  const player2 = waitingQueue[1];
  waitingQueue = waitingQueue.slice(2);
  console.log(waitingQueue.map((w) => w.id));
  const name = player1.id + '#' + player2.id;
  player1.join(name);
  player2.join(name);
  io.to(name).emit('start');
  const moleEmitter = emitMole(io, name);
  countDown(io, name, moleEmitter);
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
  }, 10000);
}

const randMole = () => Math.floor(Math.random() * 9);