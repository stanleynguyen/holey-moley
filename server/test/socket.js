const chai = require('chai');
const server = require('../index');
const serverUrl = `http://localhost:${server.address().port}`;
const io = require('socket.io-client');
const options = {
  transport: ['websocket'],
  'force new connection': true
};
const should = chai.should(); // eslint-disable-line no-unused-vars

describe('Socket', () => {
  let client1, client2;
  beforeEach((done) => {
    client1 = io.connect(serverUrl, options);
    client2 = io.connect(serverUrl, options);
    client1.on('connect', () => {
      client2.on('connect', () => {
        done();
      });
    });
  });
  
  afterEach((done) => {
    client1.on('disconnect', () => {
      if (!client2.connected) done();
    });
    client2.on('disconnect', () => {
      if (!client1.connected) done();
    });
    client1.disconnect();
    client2.disconnect();
  });
  
  describe('Connection', () => {
    it('should be able to accept client connections', (done) => {
      client1.on('start', done);
      client1.emit('join');
      client2.emit('join');
    });
    
    it('should let the other player win on disconnection', (done) => {
      client2.on('win', done);
      client1.on('start', client1.disconnect.bind(client1));
      client1.emit('join');
      client2.emit('join');
    });
  });
  
  describe('Game', () => {
    it('should start game when 2 random players matched', (done) => {
      client1.on('start', done);
      client1.emit('join');
      client2.emit('join');
    });
    
    it('should broadcast item use', (done) => {
      client2.on('item', (i) => {
        i.should.be.a('string');
        i.should.eql('bomb');
        done();
      });
      client1.on('start', client1.emit.bind(client1, 'item', 'bomb'));
      client1.emit('join');
      client2.emit('join');
    });
    
    it('should broadcast score', (done) => {
      client2.on('score', (s) => {
        s.should.be.a('number');
        s.should.eql(1);
        done();
      });
      client1.on('start', client1.emit.bind(client1, 'score'));
      client1.emit('join');
      client2.emit('join');
    });
    
    it('should broadcast player\'s death', (done) => {
      client1.on('start', client1.emit.bind(client1, 'dead'));
      client2.on('win', done);
      client1.emit('join');
      client2.emit('join');
    });
  });
});