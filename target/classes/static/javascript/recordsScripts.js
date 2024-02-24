/*
This function initiates GET request with row ID path parameter on "/records" endpoint.
 */
function rowClicked(id) {
    location.href = "/record/"+id;
}

/*
This function opens modal window with form when a row which needs to be updated clicked
, pre-populates form with row's data (including row ID as hidden field) and send AJAX PUT request to server.
Once, response received from server row data updated.
 */
function closeOpenModal(id) {

    if(!(document.getElementById('modal').classList.contains('visible'))) {
        document.getElementById('modal').classList.add('visible');
        const closeBtn = document.getElementById("close-btn");
        closeBtn.addEventListener('click', function () {
            if (document.getElementById('modal').classList.contains('visible')) {
                document.getElementById('modal').classList.remove('visible');
            }
        });
    }
    $('#name').val($('#'+id+'dentistName').text());
    $('#date').val($('#'+id+'updatedDate').html().split("/").reverse().join("-"));
    $('#time').val($('#'+id+'updatedTime').text());
    $('#patientName').val($('#'+id+'updatedPname').text());
    $('#comment').val($('#'+id+'updatedComment').text());

    $('#param').val(id);
    $('#submitBtn').click(function () {
        changeRecord($('#sendForm').serializeArray());

    });
}



/*
This function sends AJAX DELETE request to server when row delete button clicked and once response received from server
the row with passed ID is deleted from the table.
 */
function deleteRecord (id) {
    $.ajax({
            url: '/records',
            method: 'delete',
            type: 'json',
            data: {"id": id},
            success: function (data) {
                if(data["message"]==='Success'){
                    console.log(data);
                    $('#'+id).closest('tr').remove();
                }
            },
            error: function () {
                console.log("There was an error deleting the row");
            }
        }
    )}

/*
This function is called in closeOpenModal() function and is meant to send updated form in AJAX PUT request to server.
Once server response received, specified table row is updated.
 */
function changeRecord ( form) {

    const formData = {
        id: form[0].value,
        dentistName: form[1].value,
        date: form[2].value,
        time: form[3].value,
        patientName: form[4].value,
        comment: form[5].value
    }
    $.ajax({
            url: '/records',
            method: 'put',
            type: 'json',
            contentType: 'application/json',
            data: (JSON.stringify(formData)),
            success: function (data) {
                if(data["message"]==='Success'){
                    if (document.getElementById('modal').classList.contains('visible')) {
                        document.getElementById('modal').classList.remove('visible');
                    }
                    $('#'+form[0].value+'dentistName').html(form[1].value);
                    $('#'+form[0].value+'updatedDate').html(new Date(form[2].value).toLocaleDateString('fr-FR'));
                    $('#'+form[0].value+'updatedTime').html(form[3].value);
                    $('#'+form[0].value+'updatedPname').html(form[4].value);
                    $('#'+form[0].value+'updatedComment').html(form[5].value);
                }
                else {
                    $('#message').addClass('visible');
                }
            },
            error: function () {
                console.log("There was an error updating the row");
            }
        }
    )
}

/*
This function search a record based on given dentist name - sends AJAX GET request to server and renders new table rows based on received data.
 */
function search () {

    const searchWord = $('#form1').val();
    console.log(searchWord);

   $.ajax({
       url: '/records/search',
       method: 'get',
       type: 'json',
       data: {"word": searchWord},
       success: function (data) {

           console.log(data);
           let row = "";
            for(let i = 0; i<data.length; i++) {
                let ind = data[i].id;
                let className = (i%2===0)?'list-group-item-success':'list-group-item-danger';
                row += '<tr style="cursor: pointer">' +
                    '<td><a class="btn btn-warning btn-block" onclick="closeOpenModal(\''+ind+'\')">Muuta</a></td>'+
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+'>'+data[i].id+'</td>' +
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+' id='+ind+'dentistName>'+data[i].dentistName+'</td>' +
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+' id='+ind+'updatedDate>'+data[i].date+'</td>' +
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+' id='+ind+'updatedTime>'+data[i].time+'</td>' +
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+' id='+ind+'updatedPname>'+data[i].patientName+'</td>' +
                    '<td onclick="rowClicked(\''+ind+'\')" class='+className+' id='+ind+'updatedComment>'+data[i].comment+'</td>' +
                    '<td><a class="btn btn-danger btn-block" onclick="deleteRecord(\''+ind+'\')">Kustuta</a></td></tr>'

            }
           $('#tableBody').html(row);
       },
       error: function (){
            console.log("Search error")
       }

   })

}