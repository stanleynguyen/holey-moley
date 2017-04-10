if (process.env.NODE_ENV === 'dev' || process.env.NODE_ENV === 'test') require('dotenv').load();

const express = require('express');
const http = require('http');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const socket = require('./socket');

if (process.env.NODE_ENV === 'test') {
  const Mockgoose = require('mockgoose').Mockgoose;
  const mockgoose = new Mockgoose(mongoose);
  mockgoose.prepareStorage().then(() => {
    mongoose.connect('mongodb://example.com/TestingDB');
  });
} else {
  mongoose.connect(process.env.DATABASE, { server: { auto_reconnect: true } });
}
mongoose.Promise = global.Promise;

const routes = require('./routes');
const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use('/assets', express.static('./public'));

app.use('/', routes);

const server = http.createServer(app);
socket(server);
server.listen(process.env.PORT, (err) => {
  if (err) throw err;
  console.log('UP AND RUNNING @', process.env.PORT); // eslint-disable-line no-console
});

module.exports = server;