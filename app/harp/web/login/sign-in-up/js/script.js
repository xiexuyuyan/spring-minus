function checkIfLogin(name, mail){
  const arrayData = {};
  arrayData['name'] = name;
  arrayData['mail'] = mail;

  const jsonData = JSON.stringify(arrayData);
  $.ajax({
    type: "post",
    url: "http://localhost:6688/login/actionLogin.php",
    data: jsonData,
    success: function (result) {
      window.parent.$('.sign-in-up-iframe').remove();
      console.log(result);
    },
    error: function() {
      console.log("error");
    },
    complete: function() {
      console.log('login action complete!');
    }
  });
}

const loginBtn = document.getElementById('login');
const signupBtn = document.getElementById('signup');
const loginSubmit = document.getElementById('loginSubmit');

loginBtn.addEventListener('click', (e) => {
	let parent = e.target.parentNode.parentNode;
	parent.classList.remove('slide-up');
	signupBtn.parentNode.classList.add('slide-up');
});

signupBtn.addEventListener('click', (e) => {
	let parent = e.target.parentNode;
	parent.classList.remove('slide-up');
	loginBtn.parentNode.parentNode.classList.add('slide-up');
});

loginSubmit.addEventListener('click', (e) => {
	checkIfLogin($('#userName').val(), $('#userEmail').val());
});


