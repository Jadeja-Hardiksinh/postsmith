import { checkSession } from "./checkSession.js";

document.addEventListener("DOMContentLoaded", function () {
  // init();
  // Set current year in footer
  const yearElements = document.querySelectorAll("#year, #yearMobile");
  const currentYear = new Date().getFullYear();
  yearElements.forEach((el) => {
    el.textContent = currentYear;
  });
});

const form = document.getElementById("loginForm");

form.addEventListener("submit", (e) => {
  e.preventDefault();
  let formData = new FormData(form);
  let reqJson = {
    email: formData.get("email"),
    password: formData.get("password"),
  };
  fetch("/api/v1/user/login", {
    method: "POST",
    body: JSON.stringify(reqJson),
    headers: { "Content-Type": "application/json" },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      if (data.redirectTo) {
        window.location.href = data.redirectTo;
      }
      toastDisplay(data.message, data.status);
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

// async function init() {
//   const sessionData = checkSession();
//   if (sessionData.status === "success") {
//     //window.location.href = "./dashboard";
//     console.log("sucess");
//   }
// }
