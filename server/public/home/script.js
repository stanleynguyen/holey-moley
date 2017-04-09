'use strict';
const navItems = document.querySelectorAll('.nav li');
const tabItems = document.querySelectorAll('.tab-item');
const name = document.querySelector('#profile #name');
const _level = document.querySelector('#profile #level');
const _exp = document.querySelector('#profile #exp');
const expBar = document.querySelector('#profile .exp-bar');
const _gold = document.querySelector('#profile #gold');
const _inventory = document.querySelector('#inventory');
const _shop = document.querySelector('#shop');
const token = localStorage.getItem('token');
if (!token) window.location = '/';
(function() {
  getUserInfo()
    .then(displayUserInfo)
    .then(getShopItems)
    .then(displayShop)
    .then(bindShopEvents)
    .then(bindInventoryEvents)
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
      } else if (request.status === 401) {
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
    _gold.textContent = data.gold;
    _inventory.innerHTML += InventoryList(data.inventory, data.equipped);
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
          if (d.price <= userData.gold 
            && d.required_level <= userData.level
            && !userData.inventory.some((i) => i._id === d._id)) d.canBuy = true;
          return d;
        });
        resolve(rtv);
      } else if (request.status > 500) {
        reject();
      } else if (request.status === 401) {
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

function bindShopEvents() {
  const itemsCanBuy = document.querySelectorAll('#shop .tab-item:not(.locked)');
  itemsCanBuy.forEach((i) => i.addEventListener('click', buyItem.bind(i)));
}

function buyItem() {
  const itemId = this.dataset.id;
  const _this = this;
  return new Promise(function(resolve, reject) {
    const request = new XMLHttpRequest();
    request.open('POST', '/api/user/buy', true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    const data = `item=${itemId}&token=${token}`;
    request.onload = function() {
      if (request.readyState === 4) {
        if (request.status === 200) {
          _inventory.innerHTML += InventoryList(JSON.parse(request.responseText));
          alert('Successfully bought item');
          _this.classList.add('locked');
        } else if (request.status === 401) {
          localStorage.removeItem('token');
          alert('Please login first!');
          window.location = '/';
          reject();
        } else {
          const response = JSON.parse(request.responseText);
          alert(response.responseText);
        }
      }
    };
    request.onerror = reject;
    request.send(data);
  });
}

function bindInventoryEvents() {
  const invItems = document.querySelectorAll('#inventory .tab-item');
  invItems.forEach((i) => i.addEventListener('click', equipItem.bind(i)));
}

function equipItem() {
  const itemId = this.dataset.id;
  const _this = this;
  return new Promise(function(resolve, reject) {
    const request = new XMLHttpRequest();
    request.open('POST', '/api/user/equip', true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    const data = `item=${itemId}&token=${token}`;
    request.onload = function() {
      if (request.readyState === 4) {
        if (request.status === 200) {
          const equipped = JSON.parse(request.responseText).equipped;
          (equipped)
            ? _this.classList.add('equipped')
            : _this.classList.remove('equipped');
        } else if (request.status === 401) {
          localStorage.removeItem('token');
          alert('Please login first!');
          window.location = '/';
          reject();
        } else {
          const response = JSON.parse(request.responseText);
          alert(response.responseText);
        }
      }
    };
    request.onerror = reject;
    request.send(data);
  });
}

function handleError() {
  alert('Error! Redirecting back to login');
  window.location = '/';
}

// functions to render views 
const InventoryList = function(invList, equipmentList) {
  return invList.map(function(i) {
    let equipped = false;
    if (equipmentList) equipped = equipmentList.some((e) => e._id === i._id);
    return `<div class="tab-item ${(equipped && 'equipped')}" data-id="${i._id}">
              <img class="img-fluid" src="${i.img}" />
            </div>`;
  }).join('');
};

const ShopList = function(itmList) {
  return itmList.map(function(i) {
    return `<div class="tab-item ${(i.canBuy ? '' : 'locked')}" data-id="${i._id}">
              <img class="img-fluid" src="${i.img}" />
              <p>Level ${i.required_level}</p>
              <p>${i.price} Gold</p>
            </div>`;
  }).join('');
};