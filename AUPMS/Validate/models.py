from django.db import models

# Create your models here.
class Professor_Tables(models.Model):
    username = models.CharField(max_length = 10 , unique = True)
    Name = models.CharField(max_length=50)
    password = models.CharField(max_length=15)
    designation = models.CharField(max_length=30)
    date_of_birth = models.DateField()
    def __str__(self):
        return self.username

class Courses_Table(models.Model):
    course_id = models.CharField(max_length = 6,unique = True)
    course_name = models.CharField(max_length=50)
    course_credits = models.IntegerField()
    course_alias = models.CharField(max_length = 15,default = "None")
    def __str__(self):
        return self.course_name

class Students_Table(models.Model):
    student_dept = models.CharField(max_length = 3)
    student_year = models.CharField(max_length=4)
    student_section = models.CharField(max_length=1)
    student_rollno = models.CharField(max_length = 2)
    student_name = models.CharField(max_length=50)
    class Meta:
        unique_together = ['student_dept','student_year','student_section','student_rollno']
    def __str__(self):
        return self.student_rollno

class Teacher_Course(models.Model):
    prof = models.ForeignKey(Professor_Tables,on_delete=models.CASCADE)
    courses_taken = models.ForeignKey(Courses_Table,on_delete=models.CASCADE)
    batch = models.CharField(max_length=4)
    section = models.CharField(max_length=1)
    class Meta:
        unique_together = ['prof','courses_taken','batch','section']
    def __str__(self):
        return str(self.prof) + ":" + str(self.courses_taken)

class Student_course(models.Model):
    student = models.ForeignKey(Students_Table,on_delete=models.CASCADE)
    course = models.ForeignKey(Courses_Table,on_delete=models.CASCADE)
    class Meta:
        unique_together = ['student','course']
    def __str__(self):
        return str(self.student) + " " + str(self.course)

class Student_attendance(models.Model):
    student_course = models.OneToOneField(Student_course,on_delete=models.CASCADE)
    classes_held = models.IntegerField(default = 0)
    classes_attended = models.IntegerField(default = 0)
    attendance_persentage = models.FloatField(default = 0.0)
    def __str__(self):
        return str(self.student_course) + ":" + str(self.attendance_persentage)

class Date_wise_attendance(models.Model):
    student_attendance = models.ForeignKey(Student_attendance,on_delete=models.CASCADE)
    date = models.DateField()
    status = models.CharField(max_length = 8,default = "Absent")
    number_of_hours = models.IntegerField(default=1)
    def __str__(self):
        return str(self.student_attendance) + ":" + str(self.date)

class periodicals_marks(models.Model):
    student_course = models.OneToOneField(Student_course,on_delete=models.CASCADE)
    periodicals_1 = models.FloatField(default=0.0)
    periodicals_2 = models.FloatField(default=0.0)
    def __str__(self):
        return str(Student_course)
