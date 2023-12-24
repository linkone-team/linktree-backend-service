function addCollection() {
  var xhr = new XMLHttpRequest();
  var url = "http://localhost:8083/api/v1/collection/add";
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        window.location.href = 'dashboard.html';
        console.log("Response received: ", xhr.responseText);
      } else {
        on(xhr.responseText);
      }
    }
  };
  var walletId = document.getElementById("wallet-id").value;
  var data = JSON.stringify({"slug": walletId});
  xhr.send(data);
}

function off() {
  document.getElementById("overlay").style.display = "none";
}

function on(errorText) {
  document.getElementById("overlay").style.display = "block";
  document.getElementById("overlay-text").innerText = errorText;
}

document.getElementById("wallet-id").addEventListener('input', (event) => {
  let slugified = event.target.value
    .toLowerCase() // Convert to lowercase
    .replace(/[^a-z0-9-]/g, '-') // Replace non-slug characters with a hyphen
    .replace(/-+/g, '-'); // Replace multiple hyphens with a single hyphen

  event.target.value = slugified;
});

