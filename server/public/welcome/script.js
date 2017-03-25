'use strict';
(function() {
  const loginModal = document.querySelector('#modal__login');
  const loginBtn = document.querySelector('#btn__login');
  const loginClose = document.querySelector('#modal__login .close');
  const registerModal = document.querySelector('#modal__register');
  const registerBtn = document.querySelector('#btn__register');
  const registerClose = document.querySelector('#modal__register .close');
  const registrationForm = document.querySelector('#form__register');
  const loginForm = document.querySelector('#form__login');
  
  loginBtn.addEventListener('click', openModal.bind(null, loginModal));
  loginClose.addEventListener('click', closeModal.bind(null, loginModal));
  
  registerBtn.addEventListener('click', openModal.bind(null, registerModal));
  registerClose.addEventListener('click', closeModal.bind(null, registerModal));
  
  registrationForm.addEventListener('submit', function(e) {
    e.preventDefault();
    register(this);
  });
  
  loginForm.addEventListener('submit', function(e) {
    e.preventDefault();
    login(this);
  });
  
  window.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal')) {
      closeModal(loginModal);
      closeModal(registerModal);
    }
  });
})();

function openModal(modal) { modal.style.display = 'flex'; }

function closeModal(modal) { modal.style.display = 'none'; }

function register(registrationForm) {
  const usernameInput = registrationForm.querySelector('input[name="username"]');
  const passwordInput = registrationForm.querySelector('input[name="password"]');
  const confirmInput = registrationForm.querySelector('input[name="confirm-password"]');
  const submitBtn = registrationForm.querySelector('button[type="submit"]');
  const username = encodeURIComponent(usernameInput.value);
  const password = encodeURIComponent(passwordInput.value);
  const confirm = encodeURIComponent(confirmInput.value);
  if (password !== confirm) return alert('password confirmation doesn\'t match');
  submitBtn.disabled = true;
  submitBtn.textContent = 'REGISTERING...';
  const data = `username=${username}&password=${password}`;
  const request = new XMLHttpRequest();
  request.open('POST', '/api/user/register', true);
  request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  request.onreadystatechange = function() {
    if (request.readyState === 4) {
      if (request.status === 200) {
        alert('Registration successful!');
        registrationForm.reset();
        submitBtn.disabled = false;
        submitBtn.textContent = 'REGISTER';
      } else if (request.status === 500) {
        alert(request.responseText);
        submitBtn.disabled = false;
        submitBtn.textContent = 'REGISTER';
      } else {
        alert(request.responseText);
        submitBtn.disabled = false;
        submitBtn.textContent = 'REGISTER';
      }
    }
  };
  request.onerror = function() {
    alert('Error! Check your Internet connection');
    submitBtn.disabled = false;
    submitBtn.textContent = 'REGISTER';
  };
  request.send(data);
}

function login(loginForm) {
  const usernameInput = loginForm.querySelector('input[name="username"]');
  const passwordInput = loginForm.querySelector('input[name="password"]');
  const username = encodeURIComponent(usernameInput.value);
  const password = encodeURIComponent(passwordInput.value);
  const submitBtn = loginForm.querySelector('button[type="submit"]');
  submitBtn.disabled = true;
  submitBtn.textContent = 'LOGGING IN...';
  const request = new XMLHttpRequest();
  request.open('POST', '/api/user/login', true);
  request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  request.onreadystatechange = function() {
    if (request.readyState === 4) {
      if (request.status === 200) {
        const response = JSON.parse(request.responseText);
        const token = response.token;
        localStorage.setItem('token', token);
        window.location = '/home';
      } else {
        alert(request.responseText);
        submitBtn.disabled = false;
        submitBtn.textContent = 'LOGIN';
      }
    }
  };
  request.onerror = function() {
    alert('Error! Check your Internet connection');
    submitBtn.disabled = false;
    submitBtn.textContent = 'LOGIN';
  };
  const data = `username=${username}&password=${password}`;
  request.send(data);
}