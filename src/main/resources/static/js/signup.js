import { checkSession } from "./checkSession.js";
document.addEventListener("DOMContentLoaded", function () {
  init();
  // Set current year in footer
  const yearElements = document.querySelectorAll("#year, #yearMobile");
  const currentYear = new Date().getFullYear();
  yearElements.forEach((el) => {
    el.textContent = currentYear;
  });
});
const signUpForm = document.getElementById("signupForm");
signUpForm.addEventListener("submit", (e) => {
  e.preventDefault();
  let formData = new FormData(signUpForm);
  let jsonFormData = {
    userName: formData.get("fname"),
    password: formData.get("password"),
    email: formData.get("email"),
  };
  console.log(jsonFormData);
  fetch("/api/v1/users", {
    method: "POST",
    body: JSON.stringify(jsonFormData),
    headers: { "Content-Type": "application/json" },
    redirect: "follow",
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      if (data.redirectTo) {
        window.location.href = data.redirectTo;
      } else {
        toastDisplay(data.message, data.status);
      }
    });
});
function toastDisplay(msg, type) {
  let toastContaniner = document.getElementById("toast-container");
  let toastDiv = document.createElement("div");
  toastDiv.classList.add("toast", type);
  toastDiv.innerText = msg;

  toastContaniner.appendChild(toastDiv);
  setTimeout(() => toastDiv.classList.add("show"), 10);

  setTimeout(() => {
    toastDiv.classList.remove("show");
    setTimeout(() => toastDiv.remove(), 400);
  }, 4000);
}
async function init() {
  const sessionData = await checkSession();
  console.log(sessionData);
  if (sessionData.status === "success") {
    window.location.href = "./dashboard";
  }
}
