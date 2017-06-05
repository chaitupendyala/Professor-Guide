from django.contrib import admin
from .models import Professor_Tables,Students_Table,Courses_Table,Teacher_Course,Student_course,Student_attendance,Date_wise_attendance,periodicals_marks
# Register your models here.
admin.site.register(Professor_Tables)
admin.site.register(Students_Table)
admin.site.register(Courses_Table)
admin.site.register(Teacher_Course)
admin.site.register(Student_course)
admin.site.register(Student_attendance)
admin.site.register(Date_wise_attendance)
admin.site.register(periodicals_marks)
