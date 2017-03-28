'use strict';
const navItems = document.querySelectorAll('.nav li');
const tabItems = document.querySelectorAll('.tab-item');
(function() {
  navItems.forEach((i) => i.addEventListener('click', toggle.bind(i)));
  tabItems.forEach((i) => i.addEventListener('click', handleClick.bind(i)));
})();

function toggle() {
  document.querySelector('.nav li.active').classList.remove('active');
  document.querySelector('.tab-pane.active').classList.remove('active');
  this.classList.add('active');
  document.querySelector(`#${this.dataset.toggle}`).classList.add('active');
}

function handleClick() {
  if (this.dataset.goto) window.location = this.dataset.goto;
}