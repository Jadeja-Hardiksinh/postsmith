document.addEventListener("DOMContentLoaded", (e) => {
  const logoutBtn = document.getElementById("quick-post-btn");
  const connectBtn = document.getElementById("upgrade-btn");
  logoutBtn.addEventListener("click", (e) => {
    fetch("/api/v1/user/logout", { method: "POST" }).then((data) => {
      if (data.redirected) {
        window.location.href = data.url;
      }
    });
  });
  connectBtn.addEventListener("click", (e) => {
    fetch("/api/v1/user/x/token").then((data) => console.log(data));
  });
});
