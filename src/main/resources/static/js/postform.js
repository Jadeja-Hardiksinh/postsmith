document.addEventListener("DOMContentLoaded", (e) => {
  const form = document.getElementById("postform");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(form);
    console.log(formData.get("platform"));
    console.log(formData.get("content"));
    console.log(formData.get("media"));
    console.log(formData.get("schedule"));
    let reqBody = {
      Id: 1,
      platform: formData.get("platform"),
      content: formData.get("content"),
      mediaUrl: formData.get("media"),
      status: "PENDING",
      scheduledAt: formData.get("schedule"),
    };
    fetch("/api/v1/post/create", {
      method: "POST",
      body: JSON.stringify(reqBody),
      headers: { "Content-Type": "application/json" },
    }).then((data) => console.log(data));
  });
});
