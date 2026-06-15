$(document).ready(function() {

    //Navigation logic for tab switching
    $(".nav-link").click(function(e) {
        e.preventDefault();
        
        $(".nav-link").removeClass("active");
        $(this).addClass("active");

        $(".module-view").hide();

       let targetId = $(this).attr("id");
        if (targetId === "tab-curriculum") {
            $("#view-curriculum").fadeIn();
        } else if (targetId === "tab-students") {
            $("#view-students").fadeIn();
            loadStudents(); 
        } else if (targetId === "tab-faculty") {
            $("#view-faculty").fadeIn();
            loadFaculties(); 
        } else if (targetId === "tab-subjects") {
            $("#view-subjects").fadeIn();
            loadSubjects(); 
        } else if (targetId === "tab-sections") {
            $("#view-sections").fadeIn();
            loadSections(); 
        }
    });

    //Curriculum form submission 
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
                alert("Curriculum Saved Successfully!");
                $("#curriculumForm")[0].reset(); 
            },
            error: function(error) {
                alert("Error connecting to backend.");
            }
        });
    });

    //Student logic
    function loadStudents() {
        $.ajax({
            url: "http://localhost:8080/api/student/all",
            type: "GET",
            success: function(students) {
                let rows = "";
                for(let i = 0; i < students.length; i++) {
                    let s = students[i];
                    
                    let displayNumber = i + 1; 
                    
                    rows += `<tr>
                        <td>${displayNumber}</td>
                        <td>${s.firstName} ${s.lastName}</td>
                        <td>${s.course}</td>
                        <td style="color: blue; font-weight: bold;">${s.username}</td>
                        <td><button class="delete-btn" onclick="deleteStudent(${s.id})">Drop</button></td>
                    </tr>`;
                }
                $("#studentTableBody").html(rows);
            }
        });
    }

    $("#studentForm").submit(function(event) {
        event.preventDefault();
        let studentData = {
            firstName: $("#stuFirstName").val(),
            lastName: $("#stuLastName").val(),
            email: $("#stuEmail").val(),
            course: $("#stuCourse").val()
        };

        $.ajax({
            url: "http://localhost:8080/api/student/add",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(studentData),
            success: function(response) {
                alert("Student Enrolled! Generated Username: " + response.username);
                $("#studentForm")[0].reset();
                loadStudents(); 
            }
        });
    });

    window.deleteStudent = function(id) {
        if(confirm("Remove this student?")) {
            $.ajax({
                url: "http://localhost:8080/api/student/delete/" + id,
                type: "DELETE",
                success: function() {
                    loadStudents(); 
                }
            });
        }
    }

    //Faculty logic
     function loadFaculties() {
        $.ajax({
            url: "http://localhost:8080/api/faculty/all",
            type: "GET",
            success: function(faculties) {
                let rows = "";
                for(let i = 0; i < faculties.length; i++) {
                    let f = faculties[i];
                    
                    let displayNumber = i + 1; 
                    
                    rows += `<tr>
                        <td>${displayNumber}</td>
                        <td>${f.firstName} ${f.lastName}</td>
                        <td>${f.department}</td>
                        <td>${f.preferredSubjects}</td>
                        <td style="color: blue; font-weight: bold;">${f.username}</td>
                        <td><button class="delete-btn" onclick="deleteFaculty(${f.id})">Remove</button></td>
                    </tr>`;
                }
                $("#facultyTableBody").html(rows);
            }
        });
    }

    $("#facultyForm").submit(function(event) {
        event.preventDefault();
        let facultyData = {
            firstName: $("#facFirstName").val(),
            lastName: $("#facLastName").val(),
            email: $("#facEmail").val(),
            department: $("#facDepartment").val(),
            preferredSubjects: $("#facSubjects").val()
        };

        $.ajax({
            url: "http://localhost:8080/api/faculty/add",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(facultyData),
            success: function(response) {
                alert("Faculty Added! Generated Username: " + response.username);
                $("#facultyForm")[0].reset();
                loadFaculties(); 
            }
        });
    });

    window.deleteFaculty = function(id) {
        if(confirm("Remove this faculty member?")) {
            $.ajax({
                url: "http://localhost:8080/api/faculty/delete/" + id,
                type: "DELETE",
                success: function() {
                    loadFaculties(); 
                }
            });
         }
     }
 });

    //Subject logic
   function loadSubjects() {
        $.ajax({
            url: "http://localhost:8080/api/subject/all",
            type: "GET",
            success: function(subjects) {
                let rows = "";
                for(let i = 0; i < subjects.length; i++) {
                    let s = subjects[i];
                    
                    let displayNumber = i + 1; 
                    
                    rows += `<tr>
                        <td>${displayNumber}</td>
                        <td><b>${s.subjectCode}</b></td>
                        <td>${s.description}</td>
                        <td>${s.units}</td>
                        <td>${s.courseAssigned}</td>
                        <td><button class="delete-btn" onclick="deleteSubject(${s.id})">Delete</button></td>
                    </tr>`;
                }
                $("#subjectTableBody").html(rows);
            }
        });
    }

    $("#subjectForm").submit(function(event) {
        event.preventDefault();
        let subjectData = {
            subjectCode: $("#subCode").val(),
            description: $("#subDesc").val(),
            units: $("#subUnits").val(),
            courseAssigned: $("#subCourse").val()
        };
        $.ajax({
            url: "http://localhost:8080/api/subject/add",
            type: "POST", contentType: "application/json", data: JSON.stringify(subjectData),
            success: function() {
                alert("Subject Added Successfully!");
                $("#subjectForm")[0].reset();
                loadSubjects();
            }
        });
    });

    window.deleteSubject = function(id) {
        if(confirm("Remove this subject?")) {
            $.ajax({ url: "http://localhost:8080/api/subject/delete/" + id, type: "DELETE", success: function() { loadSubjects(); } });
        }
    }

    //Section logic
    function loadSections() {
        $.ajax({
            url: "http://localhost:8080/api/section/all",
            type: "GET",
            success: function(sections) {
                let rows = "";
                
                for(let i = 0; i < sections.length; i++) {
                    let s = sections[i];
                    
                    let displayNumber = i + 1; 

                    rows += `<tr>
                        <td>${displayNumber}</td> 
                        
                        <td><b>${s.sectionName}</b></td>
                        <td>${s.course}</td>
                        <td>Year ${s.yearLevel}</td>
                        <td>${s.maxCapacity} Students</td>
                        
                        <td><button class="delete-btn" onclick="deleteSection(${s.id})">Delete</button></td>
                    </tr>`;
                }
                $("#sectionTableBody").html(rows);
            }
        });
    }

    $("#sectionForm").submit(function(event) {
        event.preventDefault();
        let sectionData = {
            sectionName: $("#secName").val(),
            course: $("#secCourse").val(),
            yearLevel: $("#secYear").val(),
            maxCapacity: $("#secCapacity").val()
        };
        $.ajax({
            url: "http://localhost:8080/api/section/add",
            type: "POST", contentType: "application/json", data: JSON.stringify(sectionData),
            success: function() {
                alert("Section Block Created!");
                $("#sectionForm")[0].reset();
                loadSections();
            }
        });
    });

    window.deleteSection = function(id) {
        if(confirm("Remove this section block?")) {
            $.ajax({ url: "http://localhost:8080/api/section/delete/" + id, type: "DELETE", success: function() { loadSections(); } });
        }
    };