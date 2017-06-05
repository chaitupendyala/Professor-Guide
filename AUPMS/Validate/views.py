from django.shortcuts import render,HttpResponse
from .models import Professor_Tables,Students_Table,Courses_Table,Teacher_Course,Student_course,Student_attendance,Date_wise_attendance,periodicals_marks
from django.views.decorators.csrf import csrf_exempt
# Create your views here.

@csrf_exempt
def index(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        try:
            user = Professor_Tables.objects.get(username = username)
        except (Professor_Tables.DoesNotExist,Professor_Tables.MultipleObjectsReturned):
            user = None
            return HttpResponse("false")

        if (user != None):
            if (user.username == username and user.password == password):
                return HttpResponse("true")
            else:
                return HttpResponse("false")
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def Get_Course_List(request):
    if request.method == 'POST':
        prof_name = request.POST['prof_name']
        prof = Professor_Tables.objects.get(username = prof_name)
        courses = Teacher_Course.objects.filter(prof = prof)
        a = ""
        for x in courses:
            a += (x.courses_taken.course_id + ":" + x.courses_taken.course_alias + ":" + x.batch + ":" + x.section)
            a += ";"
        return HttpResponse(a)
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def get_complete_student_list(request):
    if request.method == 'POST':
        complete = request.POST['complete_string']
        array = complete.split(":")
        course = Courses_Table.objects.get(course_id = array[0])
        student = Students_Table.objects.filter(student_year = array[2],student_section = array[3])
        student_list = []
        obj = []
        for i in student:
            obj = Student_course.objects.filter(student = i,course = course)
            if obj.exists():
                student_list.append(obj[0])
            else:
                continue
        a = ""
        for x in student_list:
            a += x.student.student_dept + x.student.student_rollno + ":" + x.student.student_name
            a += ';'
        return HttpResponse(a)
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def update_attendance(request):
    if request.method == 'POST':
        roll_no = request.POST['roll_name'].split(":")[0]
        name = request.POST['roll_name'].split(":")[1]
        course_id = request.POST['course'].split(":")[0]
        batch = request.POST['course'].split(":")[2]
        section = request.POST['course'].split(":")[3]
        date = request.POST['date']
        reason = request.POST['reason']
        status = request.POST['status']
        student = Students_Table.objects.get(student_dept = roll_no[:3],student_year = batch,student_section = section,student_rollno = roll_no[3:5],student_name = name)
        course = Courses_Table.objects.get(course_id = course_id)
        student_course = Student_course.objects.get(student = student,course = course)
        student_attendance = Student_attendance.objects.filter(student_course = student_course)
        if len(student_attendance) == 0:
            student_att = Student_attendance(student_course = student_course,classes_held = 0,classes_attended = 0,attendance_persentage = 0)
            student_att.save()
        else:
            student_att = student_attendance[0]

        date_wise = Date_wise_attendance.objects.filter(student_attendance = student_att,date = date)
        if len(date_wise) == 0:
            date_w = Date_wise_attendance(student_attendance = student_att,date = date,status = status)
            date_w.save()
            student_att.classes_held += 1
            if status == "Present" or status == "OD":
                student_att.classes_attended += 1
                student_att.attendance_persentage = (student_att.classes_attended/student_att.classes_held)*100
                student_att.save()
            else:
                student_att.attendance_persentage = (student_att.classes_attended/student_att.classes_held)*100
                student_att.save()
        else:
            if status == "Present" or status == "OD":
                if date_wise[0].status == "Absent":
                    student_att.classes_attended += 1
                    student_att.attendance_persentage = (student_att.classes_attended/student_att.classes_held)*100
                    student_att.save()
                    date_wise[0].status = status
                    date_wise[0].save()
                else:
                    pass
            else:
                if date_wise[0].status == "Present":
                    student_att.classes_attended -= 1
                    student_att.attendance_persentage = (student_att.classes_attended/student_att.classes_held)*100
                    student_att.save()
                    date_wise[0].status = status
                    date_wise[0].save()
        return HttpResponse("true")
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def update_marks(request):
    if request.method == 'POST':
        roll_no = request.POST['roll_name'].split(":")[0]
        name = request.POST['roll_name'].split(":")[1]
        array = request.POST['course'].split(":")
        periodical = request.POST['periodicals']
        marks = int(request.POST['marks'])
        course = Courses_Table.objects.get(course_id = array[0])
        student = Students_Table.objects.get(student_dept = roll_no[:3],student_year = array[2],student_section = array[3],student_rollno = roll_no[3:5],student_name = name)
        student_course = Student_course.objects.get(student = student,course = course)
        student_marks = periodicals_marks.objects.filter(student_course = student_course)
        if len(student_marks) == 0:
            student_m = periodicals_marks(student_course = student_course)
            if periodical == "Periodicals 1":
                student_m.periodicals_1 = marks
            else:
                student_m.periodicals_2 = marks
            student_m.save()
        else:
            if periodical == "Periodicals 1":
                student_marks[0].periodicals_1 = marks
            else:
                student_marks[0].periodicals_2 = marks
            student_marks[0].save()
        return HttpResponse("true")
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def complete_mark_report(request):
    if request.method == 'POST':
        array = request.POST['course'].split(':')
        course = Courses_Table.objects.get(course_id = array[0])
        students = Students_Table.objects.filter(student_section = array[3],student_year = array[2])
        all_Students = []
        for i in students:
            s = Student_course.objects.filter(student = i,course = course)
            if len(s) != 0:
                all_Students.append(s[0])
        final_string = ""
        for i in all_Students:
            k = periodicals_marks.objects.filter(student_course = i)
            if len(k) != 0:
                final_string += (k[0].student_course.student.student_dept + str(k[0].student_course.student.student_rollno) + ":" + str(k[0].student_course.student.student_name) + "/" + str(k[0].periodicals_1) + "/" + str(k[0].periodicals_2) + ";")
        print(final_string)
        return HttpResponse(final_string)
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")

@csrf_exempt
def complete_attendance_report(request):
    if request.method == 'POST':
        array = request.POST['course'].split(':')
        course = Courses_Table.objects.get(course_id = array[0])
        students = Students_Table.objects.filter(student_section = array[3],student_year = array[2])
        all_Students = []
        for i in students:
            s = Student_course.objects.filter(student = i,course = course)
            if len(s) != 0:
                all_Students.append(s[0])
        final_string = ""
        for i in all_Students:
            k = Student_attendance.objects.filter(student_course = i)
            if len(k) != 0:
                final_string += k[0].student_course.student.student_dept + str(k[0].student_course.student.student_rollno) + ":" + str(k[0].student_course.student.student_name) + "/" + str(k[0].attendance_persentage) + ";"
        print(final_string)
        return HttpResponse(final_string)
    else:
        return HttpResponse("<h1>Get a Life!!!<br/>Ochi sanka naakura puka</h1>")
