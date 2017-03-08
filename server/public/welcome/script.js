'use strict';
(function() {
  const loginModal = document.querySelector('#modal__login');
  const loginBtn = document.querySelector('#btn__login');
  const loginClose = document.querySelector('#modal__login .close');
  const registerModal = document.querySelector('#modal__register');
  const registerBtn = document.querySelector('#btn__register');
  const registerClose = document.querySelector('#modal__register .close');
  
  loginBtn.addEventListener('click', openModal.bind(null, loginModal));
  loginClose.addEventListener('click', closeModal.bind(null, loginModal));
  
  registerBtn.addEventListener('click', openModal.bind(null, registerModal));
  registerClose.addEventListener('click', closeModal.bind(null, registerModal));
  
  window.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal')) {
      closeModal(loginModal);
      closeModal(registerModal);
    }
  });
})();

function openModal(modal) { modal.style.display = 'flex'; }

function closeModal(modal) { modal.style.display = 'none'; }