This is a project acts as an aid for professors in a college
============================================================

There are mainly two parts of the project:
------------------------------------------

1. [AUPMS] - which is the android project
2. [Project] - which is a django server which takes care of the backend


Project
=======

	* This acts as the front-end for the project and a user can access the facilities using this app.
	* This app send a request to a django server and the server in return sends a response based on the request.
	* The user interface is kept basic and plain for the ease of access.

AUPMS
=====
	
	* This is the back-end server that is deployed using django.
	* It takes a request and responds with the requested information.

How to run
==========

	AUPMS
	-----
		
		1. Install Django on the local system
		2. Migrate
			> '''sh ### python3 manage.py makemigrations Validate '''
			> '''sh ### python3 manage.py migrate '''
		3. Run the server
			> ### '''sh python3 manage.py runserver <<SYSTEM-IP>>:<<PORT-NO>> '''
	
	Project
	-------
		
		1. Open the project with Android Studio
		2. Navagate to /Project/app/src/main/java/com/example/chaitanya/project folder
		3. Open the following files:
			
			* Complete_Mark_Report.java
			* Get_attendance_report.java
			* Get_Complete_Stud_List.java
			* GetStudentList.java
			* Send_Attendance_Details.java
			* Send_Marks_Details.java
			* Validate.java
		4. Replace http://<<SYSTEM-IP>>:<<PORT-NO>>/* with the ip and port number the django server is running on
		5. Now run the project either on the emulator or generate an apk and install it
		6. Now its ready
