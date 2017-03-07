const socket = io();
socket.on('message', console.log);
socket.emit('join');

const moles = document.querySelectorAll('.mole');
const items = document.querySelectorAll('.item');
const scoreBoard = document.querySelector('#score');
const manaBar = document.querySelector('.mana');
let score = 0;
let mana = 0;

function peep(moleIdx) {
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
}

moles.forEach(function(m) { m.addEventListener('click', hit); });
items.forEach(function(i) { i.addEventListener('click', useItem); });

socket.on('mole', peep);
