const queryParams = new URLSearchParams(window.location.search);
const slug = queryParams.get('slug'); // Gets the value of the 'id' parameter
const title = document.querySelector('.dashboard-title');
const editImage = document.querySelector('.edit-image');
const addLink = document.querySelector('.add-link');
const pageForward = document.getElementById('page-forward');
const pageBackward = document.getElementById('page-backward');
const addLinkBox = document.getElementById("addLinkBox");
const linkUrl = document.getElementById("linkUrl");
const linkTitle = document.getElementById("linkTitle");
const addLinkConfirmAction = document.getElementById("addLinkConfirmAction");
const addLinkCancelAction = document.getElementById("addLinkCancelAction");
const closeButton = document.getElementById("closeButton");
const confirmAction = document.getElementById("confirmAction");
const addLinkCloseButton = document.getElementById("addLinkCloseButton");
const addLinkModalTitle = document.getElementById("addLinkModal-title");
const uploadButton = document.getElementById("uploadButton");
const imageInput = document.getElementById("imageInput");
title.innerHTML = "Manage Wallet - <span id='slug' class='red-slug'>" + slug + "</span>";

const pageSize = 10;
let currentPage = 0;
let totalPages = 0;
let sortState = 0; // 0: ID Desc, 1: Z-A, 2: A-Z, 3: ID Asc


async function fetchAndPopulateTable() {
  fetch('http://localhost:8083/api/v1/collection/' + slug + '/links?page=' + currentPage + '&size=' + pageSize)
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      populateTable(data.content);
      updatePageInfo(data);
    })
    .catch(error => {
      console.error('There has been a problem with your fetch operation:', error);
    });
}

function populateTable(data) {
  const table = document.getElementById('table');
  table.innerHTML = "";
  data.forEach(item => {
    let row = table.insertRow();
    row.setAttribute('data-id', item.id);
    let cellName = row.insertCell();
    cellName.innerHTML = `${item.displayText} - <a target="_blank">${item.targetUrl}</a>`;
    cellName.className = 'col1';

    let cellEdit = row.insertCell();
    cellEdit.innerHTML = '<i class="fa-solid fa-pen"></i>';
    cellEdit.style.cursor = 'pointer';
    cellEdit.className = 'other-col';
    cellEdit.onclick = () => openEdit(item);

    let cellDelete = row.insertCell();
    cellDelete.innerHTML = '<i class="fa-solid fa-trash"></i>';
    cellDelete.style.cursor = 'pointer';
    cellDelete.className = 'other-col';
    cellDelete.onclick = () => showConfirmationBox(item.id, item.displayText);

  });
}

function showConfirmationBox(id, text) {
  confirmationBox.style.display = "block";
  confirmationBox.setAttribute("data-id", id);
  var slugElement = confirmationBox.querySelector("#linkElement");
  linkElement.innerText = text;

}

// Function to hide the confirmation box
function hideConfirmationBox() {
  confirmationBox.style.display = "none";
}

function showAddLinkBox() {
  addLinkBox.style.display = "block";
}

function hideLinkBox() {
  addLinkBox.style.display = "none";
}

function openEdit(item) {
  linkTitle.value = item.displayText;
  linkUrl.value = item.targetUrl;
  addLinkModalTitle.innerText = `Edit ${item.displayText}`;
  addLinkBox.setAttribute("data-id", item.id);
  addLinkBox.setAttribute("mode", "edit");
  showAddLinkBox();
}

async function fetchAndPopulateTableForSearch(searchTerm) {
  if (searchTerm === "") {
    fetchAndPopulateTable();
  }
  try {
    // Prepare the request body
    const requestBody = JSON.stringify({
      searchTerm: searchTerm,
      slug: slug
    });

    // Make a POST request
    const response = await fetch('http://localhost:8083/api/v1/links/search?page=' + currentPage + '&size=' + pageSize, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: requestBody
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    populateTable(data.content);
    updatePageInfo(data);
  } catch (error) {
    populateTable([]);
    updatePageInfo({
      totalPages: 1,
      pageable: {
        pageNumber: 0
      }
    });
  }
}


async function addLinkToCollection(slug, targetUrl, displayText) {
  const endpoint = `http://localhost:8083/api/v1/collection/${slug}/add-link`;
  const requestData = {
    targetUrl,
    displayText
  };

  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const responseData = await response.json();
    console.log('Link added successfully:', responseData);
    return responseData;
  } catch (error) {
    console.error('Error adding link to collection:', error);
  }
}

function isValidUrl(string) {
  try {
    new URL(string);
    return true;
  } catch (_) {
    return false;
  }
}

function updatePageInfo(data) {
  totalPages = data.totalPages;
  currentPage = data.pageable.pageNumber;

  if (totalPages === 1) {
    pageBackward.style.visibility = 'hidden';
    pageForward.style.visibility = 'hidden';
  } else {
    pageBackward.style.visibility = 'visible';
    pageForward.style.visibility = 'visible';
  }

  if (currentPage === 0) {
    pageBackward.style.visibility = 'hidden';
  } else {
    pageBackward.style.visibility = 'visible';
  }

  if (currentPage === totalPages - 1) {
    pageForward.style.visibility = 'hidden';
  } else {
    pageForward.style.visibility = 'visible';
  }


  document.getElementById('page-info').textContent = `Page ${currentPage + 1} of ${totalPages}`;
}

async function updateLink(itemId, targetUrl, displayText) {
  const endpoint = `http://localhost:8083/api/v1/links/update/${itemId}`;
  const requestData = {
    targetUrl: targetUrl,
    displayText: displayText
  };

  try {
    const response = await fetch(endpoint, {
      method: 'PUT',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const responseData = await response.json();
    console.log('Link updated successfully:', responseData);
    return responseData;

  } catch (error) {
    console.error('Error adding link to collection:', error);
  }
}

async function uploadImage(imageFile, slug) {
  const formData = new FormData();
  formData.append('image', imageFile);
  formData.append('slug', slug);

  try {
    const response = await fetch('http://localhost:8083/api/v1/images/upload', {
      method: 'POST',
      body: formData
      // Note: When using FormData, the 'Content-Type' header should not be set manually.
      // The browser will set it automatically, including the `boundary` parameter.
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    return null;
  }
}


search.addEventListener('input', (event) => {

  fetchAndPopulateTableForSearch(event.target.value);
});

pageForward.addEventListener('click', () => {
  console.log(currentPage);
  if (currentPage < totalPages - 1) {
    currentPage++;
    fetchAndPopulateTableForSearch(search.value);
    // fetchAndPopulateTable();
  }
});

pageBackward.addEventListener('click', () => {
  console.log(currentPage);
  if (currentPage > 0) {
    currentPage--;
    fetchAndPopulateTableForSearch(search.value);
  }
});

window.addEventListener('keydown', (event) => {
  if (event.key === 'F5') {
    document.getElementById('search').value = '';
  }
});


addLink.addEventListener("click", function () {
  linkUrl.value = ""
  linkTitle.value = ""
  addLinkBox.setAttribute("mode", "add");
  showAddLinkBox()
});


addLinkConfirmAction.addEventListener("click", async function () {
  event.preventDefault()
  if (!isValidUrl(linkUrl.value)) {
    alert("Invalid URL");
    return;
  }

  var mode = addLinkBox.getAttribute("mode");
  if (mode === "edit") {
    var itemId = addLinkBox.getAttribute("data-id");
    await updateLink(itemId, linkUrl.value, linkTitle.value);
  } else if (mode === "add") {
    await addLinkToCollection(slug, linkUrl.value, linkTitle.value);
  }
  if (search.value === "") {
    fetchAndPopulateTable()
  } else {
    fetchAndPopulateTableForSearch(search.value);
  }

  hideLinkBox();
});

confirmAction.addEventListener("click", function () {
  var itemId = confirmationBox.getAttribute("data-id");
  // Send a DELETE request to your API endpoint
  fetch(`http://localhost:8083/api/v1/links/delete/${itemId}`, {
    method: "DELETE",
  })
    .then(function (response) {
      if (response.ok) {
        if (search.value === "") {
          fetchAndPopulateTable()
        } else {
          fetchAndPopulateTableForSearch(search.value);
        }
      } else {
        // Error occurred during deletion
        alert("Error: Item could not be deleted.");
      }
    })
    .catch(function (error) {
      console.error("Error:", error);
    });

  hideConfirmationBox();
});


addLinkCloseButton.addEventListener("click", hideLinkBox);

closeButton.addEventListener("click", hideConfirmationBox);
cancelAction.addEventListener("click", hideConfirmationBox);

addLinkCancelAction.addEventListener("click", function () {
  event.preventDefault()
  hideLinkBox();
});


updatePageInfo({
  totalPages: 1, pageable: {
    pageNumber:
      0
  }
});

document.getElementById('uploadButton').addEventListener('click', async () => {
  const imageFile = document.getElementById('imageInput').files[0];
  const result = await uploadImage(imageFile, slug);
});


document.addEventListener('DOMContentLoaded', () => {
  const modal = document.getElementById('uploadModal');
  const span = document.getElementsByClassName('close')[2];
  const uploadForm = document.getElementById('uploadForm');

  // Function to reset the form
  function resetForm() {
    uploadForm.reset();
  }
  // When the user clicks on the button, open the modal
  editImage.onclick = function() {
    resetForm();
    modal.style.display = "block";
  }

  // When the user clicks on <span> (x), close the modal
  span.onclick = function() {
    modal.style.display = "none";
  }

  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }

});

fetchAndPopulateTable()
