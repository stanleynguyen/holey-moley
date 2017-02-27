'use strict';
const navItems = document.querySelectorAll('.nav li');
const tabItems = document.querySelectorAll('.tab-item');
const name = document.querySelector('#profile #name');
const level = document.querySelector('#profile #level');
const exp = document.querySelector('#profile #exp');
const expBar = document.querySelector('#profile .exp-bar');
const token = localStorage.getItem('token');
if (!token) window.location = '/';
(function() {
  getUserInfo();
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

function getUserInfo() {
  const request = new XMLHttpRequest();
  request.open('GET', `/api/user/info?token=${token}`, true);
  request.onload = function() {
    console.log(token, request.status);
    if (request.status === 200) {
      const data = JSON.parse(request.responseText);
      name.textContent = data.username;
      level.textContent = data.level;
      const expLevel = data.level * 200;
      const expGained = expLevel - data.exp_needed;
      exp.textContent = `${expGained}/${expLevel}`;
      expBar.style.width = `${Math.floor(expGained/expLevel * 100)}%`;
    } else if (request.status > 400) {
      alert('Error! Redirecting back to login');
      window.location = '/';
    }
  };
  request.onerror = function() {
    alert('Error! Redirecting back to login');
    window.location = '/';
  };
  request.send();
}