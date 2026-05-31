    $("#curriculumForm").submit(function(event) {
        event.preventDefault(); 

        $.ajax({
            url: 'http://localhost:8080/api/curriculum/add', 
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                name: $("#currName").val(),
                schoolYear: $("#schoolYear").val(),
                description: $("#description").val()
            }),
            success: function(response) {
                alert("Backend Success! " + response);
                $("#curriculumForm")[0].reset(); 
            },
            error: function(error) {
                alert("Something went wrong connecting to the backend.");
            }
        });
    });