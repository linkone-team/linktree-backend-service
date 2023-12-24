const queryParams = new URLSearchParams(window.location.search);
const slug = queryParams.get('slug'); // Gets the value of the 'id' parameter
const img_url = `http://localhost:8083/api/v1/images/${slug}`;
const title = document.querySelector('.dashboard-title');

async function fetchAndUpdateWallet() {
  title.innerText = `Wallet of ${slug}`
  fetch(`http://localhost:8083/api/v1/collection/${slug}/links`)
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      console.log(data.content)
      updateWallet(data.content);
    })
    .catch(error => {
      console.error('There has been a problem with your fetch operation:', error);
    });
}
function updateWallet(data) {
  data.forEach(item => {
    const materialDiv = document.createElement("div");
    materialDiv.classList.add("material-div");
    materialDiv.innerHTML = `<a target="${item.targetUrl}" href="${item.targetUrl}">${item.displayText}</a>`
    document.getElementById("imageContainer").appendChild(materialDiv);
  });
}

const imageContainer = document.getElementById('imageContainer');
const img = document.createElement('img');
img.src = img_url;
img.className = 'round-img'; // Apply the CSS class

imageContainer.appendChild(img);
fetchAndUpdateWallet()
