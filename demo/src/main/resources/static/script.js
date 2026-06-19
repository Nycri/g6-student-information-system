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
         else if (targetId === "tab-sections") {
            $("#view-sections").fadeIn();
            loadSections(); 
        }
        else if (targetId === "tab-schedules") {
            $("#view-schedules").fadeIn();
            loadSchedules(); 
            populateScheduleDropdowns(); // <--- ADD THIS LINE!
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
    // ==========================================
    // SPRINT 3: SCHEDULE MANAGEMENT LOGIC
    // ==========================================

    function loadSchedules() {
        $.ajax({
            url: "http://localhost:8080/api/schedules/all",
            type: "GET",
            success: function(schedules) {
                let rows = "";
                for(let i = 0; i < schedules.length; i++) {
                    let s = schedules[i];
                    let displayNumber = i + 1; 

                    // Extract actual human-readable Names!
                    let facName = s.faculty ? `${s.faculty.firstName} ${s.faculty.lastName}` : 'N/A';
                    let subName = s.subject ? s.subject.subjectCode : 'N/A';
                    let secName = s.section ? s.section.sectionName : 'N/A';

                    rows += `<tr>
                        <td>${displayNumber}</td>
                        <td><b>${facName}</b></td>
                        <td>${subName}</td>
                        <td>${secName}</td>
                        <td>${s.room}</td>
                        <td>${s.dayOfWeek}</td>
                        <td>${s.startTime} - ${s.endTime}</td>
                        <td><button class="delete-btn" onclick="deleteSchedule(${s.id})">Delete</button></td>
                    </tr>`;
                }
                $("#scheduleTableBody").html(rows);
            }
        });
    }

    $("#scheduleForm").submit(function(event) {
        event.preventDefault();
        
        // We package the data exactly how we did in Postman!
        let scheduleData = {
            faculty: { id: $("#schedFacultyId").val() },
            subject: { id: $("#schedSubjectId").val() },
            section: { id: $("#schedSectionId").val() },
            room: $("#schedRoom").val(),
            dayOfWeek: $("#schedDay").val(),
            startTime: $("#schedStartTime").val(),
            endTime: $("#schedEndTime").val()
        };

        $.ajax({
            url: "http://localhost:8080/api/schedules/add",
            type: "POST", 
            contentType: "application/json", 
            data: JSON.stringify(scheduleData),
            success: function() {
                alert("Schedule Block Created Successfully!");
                $("#scheduleForm")[0].reset();
                loadSchedules();
            },
            error: function(xhr) {
                // THE BOUNCER CATCH: If the Java Controller sends back a 409 Conflict,
                // we trigger an alert box with the exact text message you wrote in the backend!
                if (xhr.status === 409) {
                    alert("⚠️ " + xhr.responseText);
                } else {
                    alert("Error saving schedule. Please check your inputs.");
                }
            }
        });
    });

    window.deleteSchedule = function(id) {
        if(confirm("Remove this schedule block?")) {
            $.ajax({ 
                url: "http://localhost:8080/api/schedules/delete/" + id, 
                type: "DELETE", 
                success: function() { loadSchedules(); } 
            });
        }
    };
    // Fetches live data from Sprint 2 to populate the Sprint 3 Dropdowns
    function populateScheduleDropdowns() {
        $.ajax({
            url: "http://localhost:8080/api/faculty/all",
            type: "GET",
            success: function(faculties) {
                let options = '<option value="" disabled selected>Select Faculty</option>';
                for(let i = 0; i < faculties.length; i++) {
                    options += `<option value="${faculties[i].id}">${faculties[i].firstName} ${faculties[i].lastName}</option>`;
                }
                $("#schedFacultyId").html(options);
            }
        });

        $.ajax({
            url: "http://localhost:8080/api/subject/all",
            type: "GET",
            success: function(subjects) {
                let options = '<option value="" disabled selected>Select Subject</option>';
                for(let i = 0; i < subjects.length; i++) {
                    options += `<option value="${subjects[i].id}">${subjects[i].subjectCode} - ${subjects[i].description}</option>`;
                }
                $("#schedSubjectId").html(options);
            }
        });

        $.ajax({
            url: "http://localhost:8080/api/section/all",
            type: "GET",
            success: function(sections) {
                let options = '<option value="" disabled selected>Select Section</option>';
                for(let i = 0; i < sections.length; i++) {
                    options += `<option value="${sections[i].id}">${sections[i].sectionName}</option>`;
                }
                $("#schedSectionId").html(options);
            }
        });
    }