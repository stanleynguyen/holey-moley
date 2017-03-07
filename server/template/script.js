const socket = io();
socket.on('message', console.log);
socket.emit('join');

const moles = document.querySelectorAll('.mole');
const items = document.querySelectorAll('.item');
const scoreBoard = document.querySelector('#score');
const manaBar = document.querySelector('.mana');
let lastMoleIdx;
let score = 0;
let mana = 0;

function randTime(min, max) {
  return Math.round(Math.random() * (max - min) + min);
}

function randMole(moles) {
  const idx = Math.floor(Math.random() * moles.length);
  if (idx === lastMoleIdx) {
    return randMole(moles);
  }
  lastMoleIdx = idx;
  return moles[idx];
}

function peep() {
  const time = randTime(200, 1000);
  const mole = randMole(moles);
  mole.classList.add('up');
  setTimeout(function() {
    mole.classList.remove('up');
    peep();
  }, time);
}

function updateManaStatus() {
  manaBar.style.height = (mana / 100) * 120 + 'px';
}

function checkItems() {
  items.forEach(function(i) {
    if (mana >= i.dataset.cost) i.classList.remove('off');
    else i.classList.add('off');
  });
}

function hit(e) {
  socket.emit('message');
  if (!e.isTrusted) return;
  score++;
  if (mana < 100) mana += 10;
  updateManaStatus();
  checkItems();
  this.classList.remove('up');
  scoreBoard.textContent = score;
}

function useItem() {
  mana -= this.dataset.cost;
  updateManaStatus();
  checkItems();
}

moles.forEach(function(m) { m.addEventListener('click', hit); });
items.forEach(function(i) { i.addEventListener('click', useItem); });

socket.on('start', peep);
window.addEventListener('beforeunload', console.log.bind(console, new Date()));
