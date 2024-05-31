getUserInfo();
showTab();
getData();
setFormRole();
setFormRole1();
setDeleteFormRole();

//getUserData

function setFormData(id, firstname, lastname, age, email, password) {
    document.getElementById("id").value = id;
    document.getElementById("firstname").value = firstname;
    document.getElementById("lastname").value = lastname;
    document.getElementById("age").value = age;
    document.getElementById("email").value = email;
    document.getElementById("password").value = password;
}

function setFormRole() {
    fetch("http://localhost:8080/admin/getRoles").then(
        (res) => res.json()
    ).then((response) => {

        let fragment = document.createDocumentFragment();

        response.forEach((role) => {
            let optionElem = new Option(role.name.replace('ROLE_', ''), role.name);
            fragment.appendChild(optionElem);
        })
        document.getElementById("roles").appendChild(fragment);
    })
}

function setFormRole1() {
    fetch("http://localhost:8080/admin/getRoles").then(
        (res) => res.json()
    ).then((response) => {

        let fragment = document.createDocumentFragment();

        response.forEach((role) => {
            let optionElem = new Option(role.name, role.name);

            fragment.appendChild(optionElem);
        })
        document.getElementById("roles_n").appendChild(fragment);
    })
}

function clearFormData() {
    document.getElementById("firstname_n").value = "";
    document.getElementById("lastname_n").value = "";
    document.getElementById("age_n").value = "";
    document.getElementById("email_n").value = "";
    document.getElementById("password_n").value = "";
}


function editDataCall(id) {
    // call get user details by id API
    fetch("http://localhost:8080/admin/getUser/" + id, {
        method: "GET"
    }).then((res) => res.json()).then((response) => {
        setFormData(response.id, response.firstname, response.lastname, response.age, response.email, response.password);
    })
}


function addForm() {
    fetch("http://localhost:8080/admin/getRoles")
        .then(res => res.json())
        .then(response => {
            console.log("Получены роли:", response);

            let select = document.getElementById("roles_n");
            let selected = [...select.options]
                .filter(option => option.selected)
                .map(option => option.value);

            let filteredRoles = [];

            if (selected.length === 1 && selected.includes(response[0].name)) {
                filteredRoles.push(response[0]);
            }

            if (selected.length === 1 && selected.includes(response[1].name)) {
                filteredRoles.push(response[1]);
            }

            if (selected.length === 2 && selected.includes(response[0].name) && selected.includes(response[1].name)) {
                filteredRoles.push(response[0]);
                filteredRoles.push(response[1]);
            }

            let user = {
                firstname: document.getElementById("firstname_n").value,
                lastname: document.getElementById("lastname_n").value,
                age: document.getElementById("age_n").value,
                email: document.getElementById("email_n").value,
                password: document.getElementById("password_n").value,
                roles: filteredRoles
            };

            fetch("http://localhost:8080/admin/addUser", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            }).then(r => {
                    console.log("До очистки формы данных:");
                    clearFormData();
                    console.log("После очистки формы данных:");
                    getData(); // Обновляем таблицу после успешного добавления пользователя
                    console.log("После обновления таблицы:");
                    // window.location.href = '/admin'; // Перенаправляем на страницу админ-панели
                    console.error("Failed to add user");
                }
            )


        });
}


// edit data - called this function when user click on button Edit
function submitForm() {
    fetch("http://localhost:8080/admin/getRoles"
    ).then(
        (res) => res.json()
    ).then((response) => {

        let select = document.getElementById('roles');
        let selected = [...select.options]
            .filter(option => option.selected)
            .map(option => option.value);

        let filteredRoles = [];

        if (selected.length === 1 && selected.includes(response[0].name)) {
            filteredRoles.push(response[0])
        }

        if (selected.length === 1 && selected.includes(response[1].name)) {
            filteredRoles.push(response[1])
        }

        if (selected.length === 2 && selected.includes(response[0].name) && selected.includes(response[1].name)) {
            filteredRoles.push(response[0])
            filteredRoles.push(response[1])
        }


        let user = {
            id: document.getElementById("id").value,
            firstname: document.getElementById("firstname").value,
            lastname: document.getElementById("lastname").value,
            age: document.getElementById("age").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            roles: filteredRoles
        }

        fetch("http://localhost:8080/admin/updateUser/" + document.getElementById("id").value, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
            .then((response) => {
                getData(); // reload the table
                getUserInfo(); // reload the panel
            })
    })
}


function setDeleteFormRole() {
    fetch("http://localhost:8080/admin/getRoles").then(
        (res) => res.json()
    ).then((response) => {
        let fragment = document.createDocumentFragment();
        response.forEach((role) => {
            let optionElem = new Option(role.name.replace('ROLE_', ''), role.name);
            fragment.appendChild(optionElem);
        });
        document.getElementById("roles_d").appendChild(fragment);
    });
}

function deleteDataCall(id) {
    fetch("http://localhost:8080/admin/getUser/" + id, {
        method: "GET"
    }).then((res) => res.json()).then((response) => {
        // Преобразование ролей в нужный формат
        let uniqueNames = response.roles.map(role => role.name.replace('ROLE_', '')).join(' ');
        setDeleteFormData(response.id, response.firstname, response.lastname, response.age, response.email, uniqueNames);
    });
}

function setDeleteFormData(id, firstname, lastname, age, email, roles) {
    document.getElementById("id_d").value = id;
    document.getElementById("firstname_d").value = firstname;
    document.getElementById("lastname_d").value = lastname;
    document.getElementById("age_d").value = age;
    document.getElementById("email_d").value = email;
    document.getElementById("roles_d").value = roles;
    document.getElementById("roles_d").disabled = true;
}

function deleteData(id) {
    fetch("http://localhost:8080/admin/deleteUser/" + id, {
        method: "DELETE"
    })
        .then(() => {
            getData(); // Обновляем данные в таблице после удаления пользователя
            $('#delete').modal('hide'); // Закрываем модальное окно после удаления
        })
        .catch(error => console.error('Error:', error));
}

function getData() {
    fetch("http://localhost:8080/admin/getUsers").then(
        (res) => res.json()
    ).then((response) => {
        let tmpData = '';

        response.forEach((user) => {
            let uniqueNames = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');

            tmpData += "<tr>"
            tmpData += "<td>" + user.id + "</td>";
            tmpData += "<td>" + user.firstname + "</td>";
            tmpData += "<td>" + user.lastname + "</td>";
            tmpData += "<td>" + user.age + "</td>";
            tmpData += "<td>" + user.email + "</td>";
            tmpData += "<td>" + uniqueNames + "</td>";
            tmpData += "<td><button class='btn btn-primary' data-toggle='modal' data-target='#edit' onclick='editDataCall(" + user.id + ")'>Edit</button></td>";
            tmpData += "<td><button class='btn btn-danger' data-toggle='modal' data-target='#delete' onclick='deleteDataCall(" + user.id + ")'>Delete</button></td>";

            tmpData += "</tr>";
        })
        document.getElementById("tbData").innerHTML = tmpData;

    })
}


function getUserInfo() {
    fetch("http://localhost:8080/user1")
        .then(response => response.json())
        .then(user => {
            let email = user.email;
            let roles = user.roles.map(role => role.name.replace('ROLE_', '')).join(' ');
            document.getElementById("adminPage").innerHTML = `${email} with roles ${roles}`;
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