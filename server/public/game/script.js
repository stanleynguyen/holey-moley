'use strict';
const subsDisplay = document.querySelector('.drop .subtitle');
const moles = document.querySelectorAll('.mole');
const items = document.querySelectorAll('.item');
const scoreBoard = document.querySelector('#score-board');
const manaBar = document.querySelector('.mana');
let score = 0;
let mana = 0;
const socket = io();
let loadingSubInterval;
// vars for kena items
let frozen, bomb;

(function() {
  loadingSubInterval = setInterval(generateSubs(), 3000);
    
  socket.emit('join');
  socket.on('start', turnOffDrop);
  socket.on('mole', peep);
  socket.on('item', kena);
  
  moles.forEach(function(m) { m.addEventListener('click', hit); });
  items.forEach(function(i) { i.addEventListener('click', useItem); });
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
  mole.classList.add('up');
  setTimeout(function() {
    mole.classList.remove('up');
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

function hit(e) {
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
  socket.emit('item', this.dataset.id);
}

function kena(item) {
  switch (item) {
    case 'freeze':
      frozen = true;
      setTimeout(() => frozen = false, 3000);
      break;
    default:
      return;
  }
}