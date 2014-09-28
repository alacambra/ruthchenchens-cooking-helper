__author__ = 'alacambra'
from django.conf.urls import patterns, url
from . import views

urlpatterns = patterns(
    '',
    # url(r'^$', views.index, name='index'),
    url(r'^/(\w+)/([\w,]+)$', views.search_from_url, name='search_from_url'),
    url(r'^/intersection$', views.search_full_intersection, name='search_full_intersection'),
    url(r'^/standard$', views.search_standard, name='search_standard'),
    )