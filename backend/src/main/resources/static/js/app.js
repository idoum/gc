function toggleTheme() {
  const root = document.documentElement;
  const current = root.getAttribute("data-bs-theme") || "light";
  root.setAttribute("data-bs-theme", current === "light" ? "dark" : "light");
}
