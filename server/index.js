if (process.env.NODE_ENV === 'dev') require('dotenv').load();

const express = require('express');
const http = require('http');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');

mongoose.connect(process.env.DATABASE, { server: { auto_reconnect: true } });
mongoose.Promise = global.Promise;

const routes = require('./routes');
const app = express();
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/', routes);

const server= http.createServer(app);
server.listen(process.env.PORT, (err) => {
  if (err) throw err;
  console.log('UP AND RUNNING @', process.env.PORT); // eslint-disable-line no-console
});