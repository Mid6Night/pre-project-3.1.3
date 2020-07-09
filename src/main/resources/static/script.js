
//create user
function subButton() {
    var data = $("#myForm").serialize();
    $.ajax({
        type: "POST",
        data: data,
        url: "http://localhost:8080/admin/user/add",
        success: function () {
            updateTable()
        }
    });
}
//table
function editModal(button) {
    document.getElementById('eFirstName').disabled = null;
    document.getElementById('eLastName').disabled = null;
    document.getElementById('eEmail').disabled = null;
    document.getElementById('ePassword').disabled = null;
    document.getElementById('eRole').disabled = null;
    document.getElementById('edit-save').value = "Edit";
    document.getElementById('form-edit').action = 'http://localhost:8080/admin/user/update';
    document.getElementById('edit-save').classList.add("btn-info");
    document.getElementById('edit-save').classList.remove("btn-danger");
    $.ajax({
            url: 'http://localhost:8080/admin/user/findOne',
            type: 'POST',
            data: {"id": button.value},
            dataType: 'json',
            success: function (data) {
                console.log(data)
                $('#eId').val(data.id);
                $('#eFirstName').val(data.firstName);
                $('#eLastName').val(data.lastName);
                $('#eEmail').val(data.email);
                $('#ePassword').val(data.password);
                var select = data.roles.map(function(item) {
                    return item['name'];
                });
                var value;
                if (/USER/.test(select)) {
                    value = 1;
                    document.getElementById('eRole').options[value].selected = true;
                }
                if (/ADMIN/.test(select)) {
                    value = 0;
                    document.getElementById('eRole').options[value].selected = true;
                }
            }
        }
    );
}
function editSave() {
    var data = $("#form-edit").serialize();
    console.log(document.getElementById('form-edit').action);
    $.ajax({
        method: "POST",
        data: data,
        url: document.getElementById('form-edit').action,
        success: function () {
            updateTable();
        }
    });
}

function theFunction(button) {
    editModal(button)
    document.getElementById('eFirstName').disabled = 'true';
    document.getElementById('eLastName').disabled = 'true';
    document.getElementById('eEmail').disabled = 'true';
    document.getElementById('ePassword').disabled = 'true';
    document.getElementById('eRole').disabled = 'true';
    document.getElementById('edit-save').value = 'Delete';
    document.getElementById('form-edit').action = 'http://localhost:8080/admin/user/delete';
    document.getElementById('edit-save').classList.add("btn-danger");
    document.getElementById('edit-save').classList.remove("btn-info");
};
$(document).ready(function () {
    updateTable()
});

function updateTable() {
    $("#tbodyid").empty();
    $.ajax({
        url: 'http://localhost:8080/admin/user/getAll',
        type: 'POST',
        data: null,
        dataType: 'json',
        success: function (data) {
            $.each(data, function (index, doc) {
                var select = doc.roles.map(function(item) {
                    return item['name'];
                });
                $('#myTable > tbody:last-child')
                    .append('<tr>\n' +
                        '                <td id="uId">' + doc.id + '</td>\n' +
                        '                <td id="uFirstName">' + doc.firstName + '</td>\n' +
                        '                <td id="uLastName">' + doc.lastName + '</td>\n' +
                        '                <td id="uEmail">' + doc.email + '</td>\n' +
                        '                <td id="uRole">' + select + '</td>\n' +
                        '                <td><button type="button" onclick="editModal(this)" id="editButton" data-toggle="modal" value="' + doc.id + '" data-target="#staticBackdrop"\n' +
                        '                            class="btn btn-info editBtn">Edit</button></td>' +
                        '                <td>' +
                        '                    <button id="delete_button" onclick="theFunction(this)" data-toggle="modal" value="' + doc.id + '" data-target="#staticBackdrop" class="btn btn-danger">Delete</button>' +
                        '                </td>' +
                        '            </tr>');

            })
        }
    })
}