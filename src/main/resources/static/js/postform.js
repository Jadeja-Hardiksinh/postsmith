document.addEventListener("DOMContentLoaded", (e) => {
  const form = document.getElementById("postform");
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(form);

    fetch("/api/v1/post/create", {
      method: "POST",
      body: formData,
    }).then((data) => console.log(data));
  });
});
