'use strict';
const navItems = document.querySelectorAll('.nav li');
const tabItems = document.querySelectorAll('.tab-item');
const name = document.querySelector('#profile #name');
const _level = document.querySelector('#profile #level');
const _exp = document.querySelector('#profile #exp');
const expBar = document.querySelector('#profile .exp-bar');
const _inventory = document.querySelector('#inventory');
const _shop = document.querySelector('#shop');
const token = localStorage.getItem('token');
if (!token) window.location = '/';
(function() {
  getUserInfo()
    .then(displayUserInfo)
    .then(getShopItems)
    .then(displayShop)
    .catch(handleError);
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
  return new Promise(function(resolve, reject) {
    const request = new XMLHttpRequest();
    request.open('GET', `/api/user/info?token=${token}`, true);
    request.onload = function() {
      if (request.status === 200) {
        const data = JSON.parse(request.responseText);
        resolve(data);
      } else if (request.status > 500) {
        reject();
      } else if (request.status > 400) {
        localStorage.removeItem('token');
        reject();
      }
    };
    request.onerror = reject;
    request.send();
  });
}

function displayUserInfo(data) {
  return new Promise(function(resolve) {
    name.textContent = data.username;
    _level.textContent = data.level;
    const expLevel = data.level * 200;
    const expGained = expLevel - data.exp_needed;
    _exp.textContent = `${expGained}/${expLevel}`;
    expBar.style.width = `${Math.floor(expGained/expLevel * 100)}%`;
    _inventory.innerHTML += InventoryList(data.inventory);
    resolve(data);
  });
}

function getShopItems(userData) {
  return new Promise(function(resolve, reject) {
    const request = new XMLHttpRequest();
    request.open('GET', `/api/item/all?token=${token}`, true);
    request.onload = function() {
      if (request.status === 200) {
        const data = JSON.parse(request.responseText);
        const rtv = data.map(function(d) {
          d.canBuy = false;
          if (d.price <= userData.gold && d.required_level <= userData.level) d.canBuy = true;
          return d;
        });
        resolve(rtv);
      } else if (request.status > 500) {
        reject();
      } else if (request.status > 400) {
        localStorage.removeItem('token');
        reject();
      }
    };
    request.onerror = reject;
    request.send();
  });
}

function displayShop(shopList) {
  return new Promise(function(resolve) {
    _shop.innerHTML += ShopList(shopList);
    resolve();
  });
}

function handleError() {
  alert('Error! Redirecting back to login');
  window.location = '/';
}

// functions to render views 
const InventoryList = function(invList) {
  return invList.map(function(i) {
    return '<div class="tab-item">\
              <img class="img-fluid" src="' + i.img + '" />\
            </div>';
  }).join('');
};

const ShopList = function(itmList) {
  return itmList.map(function(i) {
    return '<div class="tab-item ' + (i.canBuy ? '' : 'locked') + '">\
              <img class="img-fluid" src="' + i.img + '" />\
              <p>Level ' + i.required_level + '</p>\
              <p>' + i.price + '</p>\
            </div>';
  });
};