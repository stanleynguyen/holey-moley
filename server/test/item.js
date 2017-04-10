let User = require('../models/user');
let Item = require('../models/item');

const chai = require('chai');
const chaiHttp = require('chai-http');
const server = require('../index');
const should = chai.should(); // eslint-disable-line no-unused-vars
chai.use(chaiHttp);

describe('Item', () => {
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
  
  beforeEach((done) => {
    Item.remove({}, (err) => {
      done(err);
    });
  });
  
  describe('/GET item', () => {
    let token;
    before((done) => {
      chai.request(server)
        .post('/api/user/login')
        .send({ username: 'test', password: 'test' })
        .end((err, res) => {
          if (err) return done(err);
          token = res.body.token;
          done();
        });
    });
    
    it('should get all the items', (done) => {
      chai.request(server)
        .get(`/api/item/all?token=${token}`)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('array');
          res.body.length.should.be.eql(0);
          done();
        });
    });
  });
  
  describe('/POST item', () => {
    it('should create a new item', (done) => {
      const item = {
        id: 'ultimate',
        img: '/some/random/source',
        required_level: 1000,
        price: 1000000,
        mana_cost: 100
      };
      chai.request(server)
        .post('/api/item')
        .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
        .send(item)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a('object');
          res.body.should.have.property('_id');
          res.body.should.have.property('id').eql(item.id);
          res.body.should.have.property('img').eql(item.img);
          res.body.should.have.property('required_level').eql(item.required_level);
          res.body.should.have.property('price').eql(item.price);
          res.body.should.have.property('mana_cost').eql(item.mana_cost);
          done();
        });
    });
    
    it('should not create a new item with duplicate id', (done) => {
      const item = {
        id: 'ultimate',
        img: '/other/source',
        required_level: 10,
        price: 1,
        mana_cost: 1
      };
      const newItem = Item(item);
      newItem.save((err) => {
        if (err) return done(err);
        chai.request(server)
          .post('/api/item')
          .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
          .send(item)
          .end((err, res) => {
            res.should.have.status(500);
            res.body.should.be.a('object');
            res.body.should.have.property('code').eql(11000);
            res.body.should.have.property('index');
            res.body.should.have.property('errmsg');
            done();
          });
      });
    });
    
    it('should not create a new item with missing field', (done) => {
      const item = {
        img: '/some/random/source',
        required_level: 1000,
        price: 1000000,
        mana_cost: 100
      };
      chai.request(server)
        .post('/api/item')
        .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
        .send(item)
        .end((err, res) => {
          res.should.have.status(500);
          res.body.should.be.a('object');
          res.body.should.have.property('errors');
          res.body.should.have.property('message');
          res.body.should.have.property('name');
          done();
        });
    });
  });
  
  describe('/PUT item', () => {
    it('should update an item with given id', (done) => {
      const item = new Item({
        id: 'ultimate',
        img: '/some/random/source',
        required_level: 1000,
        price: 1000000,
        mana_cost: 100
      });
      item.save((err) => {
        if (err) return done(err);
        item.img = '/another/image/source';
        item.price = 1;
        item.required_level = 1;
        chai.request(server)
          .put('/api/item')
          .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
          .send(item)
          .end((err, res) => {
            res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('_id').eql(item._id.toString());
            res.body.should.have.property('id').eql(item.id);
            res.body.should.have.property('img').eql(item.img);
            res.body.should.have.property('required_level').eql(item.required_level);
            res.body.should.have.property('price').eql(item.price);
            res.body.should.have.property('mana_cost').eql(item.mana_cost);
            done();
          });
      });
    });
    
    it('should not update item props to null', (done) => {
      const item = new Item({
        id: 'ultimate',
        img: '/some/random/source',
        required_level: 1000,
        price: 1000000,
        mana_cost: 100
      });
      item.save((err) => {
        if (err) return done(err);
        item.img = null;
        item.price = 1;
        item.required_level = 1;
        chai.request(server)
          .put('/api/item')
          .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
          .send(item)
          .end((err, res) => {
            res.should.have.status(500);
            res.body.should.be.a('object');
            res.body.should.have.property('errors');
            res.body.should.have.property('message');
            res.body.should.have.property('name');
            done();
          });
      });
    });
  });
  
  describe('/DELETE item', () => {
    it('should delete item given id', (done) => {
      const item = new Item({
        id: 'ultimate',
        img: '/some/random/source',
        required_level: 1000,
        price: 1000000,
        mana_cost: 100
      });
      item.save((err) => {
        if (err) return done(err);
        chai.request(server)
          .delete('/api/item')
          .auth(process.env.ADMIN_ACC, process.env.ADMIN_PSSWD)
          .send(item)
          .end((err, res) => {
            res.should.have.status(200);
            res.body.should.be.a('object');
            done();
          });
      });
    });
  });
});