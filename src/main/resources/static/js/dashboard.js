import { checkSession } from "./checkSession.js";

document.addEventListener("DOMContentLoaded", (e) => {
  init();
});
async function init() {
  const sessionData = await checkSession();
  if (sessionData.status === "error") {
    window, (location.href = "./login");
  }
}
