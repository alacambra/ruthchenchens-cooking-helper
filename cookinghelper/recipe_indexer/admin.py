from django.contrib import admin
from models import *


class BookAdmin(admin.ModelAdmin):
    fields = ['isbn', 'title']

admin.site.register(RecipesCategory)
admin.site.register(Recipe)
admin.site.register(Ingredient)
admin.site.register(Book, BookAdmin)