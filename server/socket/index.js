module.exports = (server) => {
  const io = require('socket.io')(server);
  let waitingQueue = [];
  
  function checkQ() {
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
  }
  
  io.on('connection', (socket) => {
    socket.on('join', () => {
      waitingQueue.push(socket);
      checkQ();
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