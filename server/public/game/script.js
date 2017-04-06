'use strict';
// check login
const token = localStorage.getItem('token');
if (!token) window.location = '/';
// main
const subsDisplay = document.querySelector('.drop .subtitle');
const moles = document.querySelectorAll('.mole');
const items = document.querySelectorAll('.item');
const myScore = document.querySelector('#my-score');
const opponentScore = document.querySelector('#opp-score');
const manaBar = document.querySelector('.mana');
let score = 0;
let health = 3;
let mana = 0;
const socket = io();
let loadingSubInterval;
// vars for kena items
let frozen, bomb;
const frozenOverlay = document.querySelector('.freeze-overlay');
const heartIcons = document.querySelectorAll('.heart');
const explosionOverlay = document.querySelector('.explosion-overlay');
const isEventSupported = (function(){
  const TAGNAMES = {
    'select':'input','change':'input',
    'submit':'form','reset':'form',
    'error':'img','load':'img','abort':'img'
  };
  function isEventSupported(eventName) {
    var el = document.createElement(TAGNAMES[eventName] || 'div');
    eventName = 'on' + eventName;
    var isSupported = (eventName in el);
    if (!isSupported) {
      el.setAttribute(eventName, 'return;');
      isSupported = typeof el[eventName] == 'function';
    }
    el = null;
    return isSupported;
  }
  return isEventSupported;
})();

(function() {
  loadingSubInterval = setInterval(generateSubs(), 3000);
    
  socket.emit('join');
  socket.on('start', turnOffDrop);
  socket.on('mole', peep);
  socket.on('item', kena);
  socket.on('score', updateOppScore);
  socket.on('win', handleWin);
  socket.on('lose', handleLose);
  
  moles.forEach(function(m) { m.addEventListener('touchstart', hit.bind(m, socket)); });
  items.forEach(function(i) { i.addEventListener('touchstart', useItem); });
  if (!isEventSupported('touchstart')) {
    moles.forEach(function(m) { m.addEventListener('click', hit.bind(m, socket)); });
    items.forEach(function(i) { i.addEventListener('click', useItem); });
  }
})();

function generateSubs() {
  let idx = -1;
  const subtitles = [
    'Loading...',
    'Getting your inventory from garage...',
    'Waiting for moles from hospital...',
    'Waiting for Thor to get hammer...'
  ];
  return () => {
    idx = (idx + 1) % subtitles.length;
    subsDisplay.textContent = subtitles[idx];
  };
}

function turnOffDrop() { 
  clearInterval(loadingSubInterval);
  document.querySelector('.drop').style.display = 'none'; 
}

function peep(moleIdx) {
  if (frozen) return;
  const mole = moles[moleIdx];
  if (bomb) mole.classList.add('mole-bomb');
  mole.classList.add('up');
  const _bomb = bomb;
  setTimeout(function() {
    mole.classList.remove('up');
    if (_bomb) bomb = false;
    if (_bomb) setTimeout(() => { mole.classList.remove('mole-bomb'); }, 500);
  }, 500);
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

function hit(socket, e) {
  if (!e.isTrusted) return;
  if (this.classList.contains('mole-bomb')) {
    health--;
    explosionOverlay.style.display = 'block';
    setTimeout(() => explosionOverlay.style.display = 'none', 200);
    updateHealth(health);
  }
  if (this.clasList.contains('mole-hitted')) return;
  score++;
  if (mana < 100) mana += 10;
  updateManaStatus();
  checkItems();
  this.classList.add('mole-hitted');
  this.classList.remove('up');
  setTimeout(() => this.classList.remove('mole-hitted'), 500);
  myScore.textContent = score;
  socket.emit('score');
}

function useItem() {
  if (mana - this.dataset.cost < 0) return;
  mana -= this.dataset.cost;
  updateManaStatus();
  checkItems();
  if (this.dataset.id === 'health') {
    health < 3 && health++;
    updateHealth(health);
  }
  socket.emit('item', this.dataset.id);
}

function kena(item) {
  switch (item) {
    case 'freeze':
      frozen = true;
      frozenOverlay.style.display = 'block';
      setTimeout(() => {
        frozen = false;
        frozenOverlay.style.display = 'none';
      }, 3000);
      break;
    case 'bomb':
      bomb = true;
      break;
    default:
      return;
  }
}

function updateOppScore(score) { opponentScore.textContent = score; }

function updateHealth(health) {
  if (health === 0) socket.emit('dead');
  heartIcons.forEach((i) => i.classList.remove('off'));
  for (let i=0; i<3-health; i++) {
    heartIcons[i].classList.add('off');
  }
}

function handleWin() { 
  socket.emit('end', localStorage.getItem('token')); 
  alert('You won');
  window.location = '/home';
}

function handleLose() {
  socket.emit('end', localStorage.getItem('token'));
  alert('You Lost');
  window.location = '/home';
}