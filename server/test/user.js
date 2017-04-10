const User = require('../models/user');
const Item = require('../models/item');

const chai = require('chai');
const chaiHttp = require('chai-http');
const server = require('../index');
const should = chai.should(); // eslint-disable-line no-unused-vars
chai.use(chaiHttp);

describe('User', () => {
  before((done) => {
    User.remove({}, (err) => {
      if (err) return done(err);
      chai.request(server)
        .post('/api/user/register')
        .send({ username: 'test', password: 'test' })
        .end((err) => {
          done(err);
        });
    });
  });
  
  describe('/GET user', () => {
    let token;
    beforeEach((done) => {
      chai.request(server)
        .post('/api/user/login')
        .send({ username: 'test', password: 'test' })
        .end((err, res) => {
          token = res.body.token;
          done(err);
        });
    });
    
    it('should get current user info', (done) => {
      chai.request(server)
        .get(`/api/user/info?token=${token}`)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('object');
          res.body.should.have.property('username');
          res.body.should.have.property('level');
          res.body.should.have.property('exp_needed');
          res.body.should.have.property('gold');
          res.body.should.have.property('inventory');
          res.body.inventory.should.be.a('array');
          res.body.should.have.property('equipped');
          res.body.equipped.should.be.a('array');
          done(err);
        });
    });
    
    it('should get current user equipment', (done) => {
      chai.request(server)
        .get(`/api/user/equipment?token=${token}`)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('array');
          done(err);
        });
    });
  });
  
  describe('/POST user', () => {
    it('should register new user', (done) => {
      const user = {
        username: 'user',
        password: 'pass'
      };
      chai.request(server)
        .post('/api/user/register')
        .send(user)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('object');
          res.body.should.have.property('username').eql(user.username);
          res.body.should.have.property('password').not.eql(user.password);
          res.body.should.have.property('level').eql(1);
          res.body.should.have.property('exp_needed').eql(200);
          res.body.should.have.property('gold').eql(0);
          res.body.should.have.property('inventory');
          res.body.inventory.should.be.a('array');
          res.body.should.have.property('equipped');
          res.body.equipped.should.be.a('array');
          done();
        });
    });
    
    it('should not register new user with duplicate username', (done) => {
      const user = {
        username: 'test',
        password: 'pass'
      };
      chai.request(server)
        .post('/api/user/register')
        .send(user)
        .end((err, res) => {
          res.should.have.status(400);
          res.body.should.be.a('object');
          res.body.should.have.property('responseText').eql('Username Taken');
          done();
        });
    });
    
    it('should login an existing user', (done) => {
      const user = {
        username: 'test',
        password: 'test'
      };
      chai.request(server)
        .post('/api/user/login')
        .send(user)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('object');
          res.body.should.have.property('token');
          done();
        });
    });
  });
  
  describe('/POST user\'s item', () => {
    let token;
    const item = [];
    before((done) => {
      item[0] = new Item({
        id: 'ultimate',
        img: '/some/random/source',
        required_level: 2,
        price: 200,
        mana_cost: 100
      });
      item[1] = new Item({
        id: 'ultimate1',
        img: '/some/random/source1',
        required_level: 2,
        price: 200,
        mana_cost: 100
      });
      item[2] = new Item({
        id: 'ultimate2',
        img: '/some/random/source2',
        required_level: 2,
        price: 200,
        mana_cost: 100
      });
      item[3] = new Item({
        id: 'ultimate3',
        img: '/some/random/source3',
        required_level: 2,
        price: 200,
        mana_cost: 100
      });
      item[0].save((err) => {
        if (err) return done(err);
        item[1].save((err) => {
          if (err) return done(err);
          item[2].save((err) => {
            if (err) return done(err);
            item[3].save((err) => done(err));
          });
        });
      });
    });
    beforeEach((done) => {
      User.findOneAndUpdate({ username: 'test' }, { $set: { gold: 1000, level: 100, inventory: [] } })
        .exec((err) => {
          if (err) return done(err);
          chai.request(server)
            .post('/api/user/login')
            .send({ username: 'test', password: 'test' })
            .end((err, res) => {
              token = res.body.token;
              done(err);
            });
        });
    });
    
    it('should buy an item', (done) => {
      chai.request(server)
        .post('/api/user/buy')
        .set('x-access-token', token)
        .send({ item: item[0]._id })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('array');
          res.body.length.should.eql(1);
          done();
        });
    });
    
    it('should equip an item', (done) => {
      User.findOneAndUpdate({ username: 'test' }, { $set: { inventory: [item[0]._id] } })
        .exec((err) => {
          if (err) return done(err);
          chai.request(server)
            .post('/api/user/equip')
            .set('x-access-token', token)
            .send({ item: item[0]._id })
            .end((err, res) => {
              res.should.have.status(200);
              res.body.should.be.a('object');
              res.body.should.have.property('equipped').eql(true);
              done();
            });
        });
    });
    
    it('should uneuqip an item', (done) => {
      User.findOneAndUpdate({ username: 'test' }, { $set: { inventory: [item[0]._id], equipped: [item[0]._id] } })
        .exec((err) => {
          if (err) return done(err);
          chai.request(server)
            .post('/api/user/equip')
            .set('x-access-token', token)
            .send({ item: item[0]._id })
            .end((err, res) => {
              res.should.have.status(200);
              res.body.should.be.a('object');
              res.body.should.have.property('equipped').eql(false);
              done();
            });
        });
    });
    
    it('should not equip item not inside inventory', (done) => {
      chai.request(server)
        .post('/api/user/equip')
        .set('x-access-token', token)
        .send({ item: item[0]._id })
        .end((err, res) => {
          res.should.have.status(404);
          res.body.should.be.a('object');
          res.body.should.have.property('responseText').eql('Item Not In Inventory');
          done();
        });
    });
    
    it('should not equip more than 3 items', (done) => {
      User.findOneAndUpdate({ username: 'test' }, { $set: { inventory: item, equipped: item.slice(0,3) } })
        .exec((err) => {
          if (err) return done(err);
          chai.request(server)
            .post('/api/user/equip')
            .set('x-access-token', token)
            .send({ item: item[3]._id })
            .end((err, res) => {
              res.should.have.status(403);
              res.body.should.be.a('object');
              res.body.should.have.property('responseText').eql('Cant hold more than 3. Unequip something first!');
              done();
            });
        });
    });
  });
});