function getUserData() {
    fetch("http://localhost:8080/user1")
        .then(response => response.json())
        .then(user => {
            let tmpData = '';

            // Преобразование ролей в нужный формат
            let uniqueNames = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

            tmpData += "<tr>"
            tmpData += "<td>" + user.id + "</td>";
            tmpData += "<td>" + user.firstname + "</td>";
            tmpData += "<td>" + user.lastname + "</td>";
            tmpData += "<td>" + user.age + "</td>";
            tmpData += "<td>" + user.email + "</td>";
            tmpData += "<td>" + uniqueNames + "</td>";

            tmpData += "</tr>";

            document.getElementById("tbDataUser").innerHTML = tmpData;
        })
        .catch(error => console.error('Error:', error));
}

function getPage() {
    fetch("http://localhost:8080/user1")
        .then(response => response.json())
        .then(user => {
            let tmpData = '';

            // Преобразование ролей в нужный формат
            let uniqueNames = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

            tmpData += "<tr>"
            tmpData += "<td>" + user.email + "</td>";
            tmpData += "<td> </td>";
            tmpData += "<td>with roles</td>";
            tmpData += "<td> </td>";
            tmpData += "<td>" + uniqueNames + "</td>";

            tmpData += "</tr>";

            document.getElementById("adminPage").innerHTML = tmpData;
        })
        .catch(error => console.error('Error:', error));
}

function showTab() {
    fetch("http://localhost:8080/user1")
        .then(response => response.json())
        .then(user => {
            // Проверяем, есть ли у пользователя роль ADMIN
            if (!user.roles.some(role => role.name === 'ROLE_ADMIN')) {
                // Скрываем вкладку Admin, если нет роли ADMIN
                hideTab('admin-tab');
            }
        })
        .catch(error => console.error('Error:', error));
}

function hideTab(tabId) {
    // Скрываем указанную вкладку
    document.getElementById(tabId).style.display = 'none';
}

showTab();
getPage();
getUserData()