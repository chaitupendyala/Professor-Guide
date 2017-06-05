from django.conf.urls import url
from django.contrib import admin
from . import views
urlpatterns = [
    url(r'^$',views.index),
    url(r'^get_course_list$',views.Get_Course_List),
    url(r'^get_complete_student_list$',views.get_complete_student_list),
    url(r'^update_attendance$',views.update_attendance),
    url(r'^update_marks$',views.update_marks),
    url(r'^complete_mark_report$',views.complete_mark_report),
    url(r'^complete_attendance_report$',views.complete_attendance_report),
]
