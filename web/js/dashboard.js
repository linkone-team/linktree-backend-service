// Global variable for size
const pageSize = 10;
let currentPage = 0;
let totalPages = 0;
let sortState = 0; // 0: ID Desc, 1: Z-A, 2: A-Z, 3: ID Asc
const sortToggle = document.querySelector('.sort-toggle');
const pageForward = document.getElementById('page-forward');
const pageBackward = document.getElementById('page-backward');
const search = document.getElementById('search');
const confirmationBox = document.getElementById("confirmationBox");
const closeButton = document.getElementById("closeButton");
const confirmAction = document.getElementById("confirmAction");
const cancelAction = document.getElementById("cancelAction");

function fetchAndPopulateTable() {
  fetch('http://localhost:8083/api/v1/collection/get/all?page=' + currentPage + '&size=' + pageSize)
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      populateTable(data.content);
      updatePageInfo(data)
    })
    .catch(error => {
      console.error('There has been a problem with your fetch operation:', error);
    });
}

async function fetchAndPopulateTableForSearch(slugPart) {
  if (slugPart === "") {
    fetchAndPopulateTable();
  }
  try {
    const response = await fetch('http://localhost:8083/api/v1/collection/search/' + slugPart + '?page=' + currentPage + '&size=' + pageSize);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    populateTable(data.content);
    updatePageInfo(data);
  } catch (error) {
    populateTable([]);
    updatePageInfo({
      totalPages: 1, pageable: {
        pageNumber:
          0
      }
    });
  }
}


function populateTable(data) {
  const table = document.getElementById('table');
  table.innerHTML = "";
  const baseUrl = window.location.origin;

  // Construct the complete URL with the slug
  const completeUrl = baseUrl + '/wallet.html?slug=';
  data.forEach(item => {
    let row = table.insertRow();
    row.setAttribute('data-id', item.id);
    let cellName = row.insertCell();
    cellName.innerHTML = `${completeUrl}<a target="_blank">${item.slug}</a>`;
    cellName.className = 'col1';

    let cellCopy = row.insertCell();
    cellCopy.innerHTML = '<i class="fa-solid fa-copy"></i>';
    cellCopy.style.cursor = 'pointer';
    cellCopy.className = 'other-col';
    cellCopy.onclick = () => {
      copyToClipboard(completeUrl + item.slug);
    };


    let cellEdit = row.insertCell();
    cellEdit.innerHTML = '<i class="fa-solid fa-pen"></i>';
    cellEdit.style.cursor = 'pointer';
    cellEdit.className = 'other-col';
    cellEdit.onclick = () => setWindow('wallet-editor.html?slug=' + item.slug);

    let cellDelete = row.insertCell();
    cellDelete.innerHTML = '<i class="fa-solid fa-trash"></i>';
    cellDelete.style.cursor = 'pointer';
    cellDelete.className = 'other-col';
    cellDelete.onclick = () => showConfirmationBox(item.id, item.slug);

  });
}

function setWindow(windowUrl) {
  window.location.href = windowUrl;
}

function copyToClipboard(text) {
  navigator.clipboard.writeText(text).then(() => {
    console.log('Text copied to clipboard');
  }).catch(err => {
    console.error('Could not copy text: ', err);
  });
}

function sortTable(ascending) {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("table");
  switching = true;

  while (switching) {
    switching = false;
    rows = table.rows;

    for (i = 0; i < (rows.length - 1); i++) {
      shouldSwitch = false;
      x = rows[i].getElementsByTagName("TD")[0];
      y = rows[i + 1].getElementsByTagName("TD")[0];
      if (ascending ? (x.innerText.toLowerCase() > y.innerText.toLowerCase()) : (x.innerText.toLowerCase() < y.innerText.toLowerCase())) {
        shouldSwitch = true;
        break;
      }
    }

    if (shouldSwitch) {
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}

function sortTableById(ascending) {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("table");
  switching = true;

  while (switching) {
    switching = false;
    rows = table.rows;

    for (i = 0; i < (rows.length - 1); i++) {
      shouldSwitch = false;
      x = rows[i].getAttribute('data-id');
      y = rows[i + 1].getAttribute('data-id');

      if (ascending ? (x > y) : (x < y)) {
        shouldSwitch = true;
        break;
      }
    }

    if (shouldSwitch) {
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
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

function showConfirmationBox(id, slug) {
  confirmationBox.style.display = "block";
  confirmationBox.setAttribute("data-id", id);
  var slugElement = confirmationBox.querySelector("#slug");

  // Set the text content of the slug element
  slugElement.innerText = slug;
}

// Function to hide the confirmation box
function hideConfirmationBox() {
  confirmationBox.style.display = "none";
}

sortToggle.addEventListener('click', () => {
  console.log(sortState)
  switch (sortState) {
    case 0: // Currently ID Desc, switch to Z-A
      sortTableById(false); // Sort Z-A (assuming this sorts by slug in desc order)
      sortToggle.innerHTML = '<i class="fa-solid fa-arrow-up-1-9"></i>';
      sortState = 1;
      break;
    case 1:
      sortTable(true);
      // Sort A-Z (assuming this sorts by slug in asc order)
      sortToggle.innerHTML = '<i class="fa-solid fa-arrow-down-a-z"></i>';
      sortState = 2;
      break;
    case 2: // Currently A-Z, switch to ID Asc
      sortTable(false); // Sort by ID Asc
      sortToggle.innerHTML = '<i class="fa-solid fa-arrow-up-a-z"></i>';
      sortState = 3;
      break;
    case 3: // Currently ID Asc, switch to ID Desc
      sortTableById(true);
      sortToggle.innerHTML = '<i class="fa-solid fa-arrow-down-1-9"></i>';
      sortState = 0;
      break;
  }
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

search.addEventListener('input', (event) => {
  let slugified = event.target.value
    .toLowerCase() // Convert to lowercase
    .replace(/[^a-z0-9-]/g, '-') // Replace non-slug characters with a hyphen
    .replace(/-+/g, '-'); // Replace multiple hyphens with a single hyphen

  event.target.value = slugified;
  fetchAndPopulateTableForSearch(slugified);
});


window.addEventListener('keydown', (event) => {
  if (event.key === 'F5') {
    document.getElementById('search').value = '';
  }
});

closeButton.addEventListener("click", hideConfirmationBox);
cancelAction.addEventListener("click", hideConfirmationBox);

confirmAction.addEventListener("click", function () {
  var itemId = confirmationBox.getAttribute("data-id");
  // Send a DELETE request to your API endpoint
  fetch(`http://localhost:8083/api/v1/collection/delete/${itemId}`, {
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

fetchAndPopulateTable();
