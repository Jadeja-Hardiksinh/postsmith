export async function checkSession() {
  try {
    let response = await fetch("/api/v1/authenticate", { method: "GET" });

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    let resJson = await response.json();
    return resJson;
  } catch (error) {
    return { status: "error", message: error.message };
  }
}
