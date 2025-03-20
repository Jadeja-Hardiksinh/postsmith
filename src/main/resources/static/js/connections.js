import { checkSession } from "./checkSession.js";
document.addEventListener("DOMContentLoaded", function () {
  init();
  const tabs = document.querySelectorAll(".tab-item");
  const tabPanels = document.querySelectorAll(".tab-panel");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      // Remove active class from all tabs and panels
      tabs.forEach((t) => t.classList.remove("active"));
      tabPanels.forEach((panel) => panel.classList.remove("active"));

      // Add active class to clicked tab
      tab.classList.add("active");

      // Show the corresponding panel
      const tabId = tab.getAttribute("data-tab");
      document.getElementById(`${tabId}-panel`).classList.add("active");
    });
  });
});
const twitterForm = document.getElementById("twitter-form");

twitterForm.addEventListener("submit", (e) => {
  e.preventDefault();

  fetch("/user/social/create")
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      console.log(data);
      if (data.redirectTo) {
        console.log(data.redirectTo);
        window.location.href = data.redirectTo;
      } else {
        console.log("Hewyy");
      }
    });
});
async function init() {
  const sessionData = await checkSession();
  if (sessionData.status === "error") {
    window.location.href = "./login";
  }
}
