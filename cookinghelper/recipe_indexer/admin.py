from django.contrib import admin
from models import Category, Book, Recipe, Ingredient, BookRecipe, CategoryRecipe, IngredientRecipe


class BookAdmin(admin.ModelAdmin):
    fields = ['isbn', 'title']

admin.site.register(Category)
admin.site.register(Recipe)
admin.site.register(Ingredient)
admin.site.register(Book, BookAdmin)
admin.site.register(BookRecipe)
admin.site.register(CategoryRecipe)
admin.site.register(IngredientRecipe)