from django.db import models


class Book(models.Model):
    fields = ['isbn', 'title']
    isbn = models.CharField(max_length=17)
    title = models.CharField(max_length=256)


class Recipe(models.Model):
    title = models.CharField(max_length=256)
    book = models.ForeignKey(Book)
    rating = models.IntegerField(default=0)

    def __str__(self):
        return "recipe " + self.title + " from " + self.book.isbn

    def __repr__(self):
        return self.__str__()


class BookRecipe(models.Model):
    book = models.ForeignKey(Book)
    recipe = models.ForeignKey(Recipe)
    page = models.IntegerField(default=0)


class Ingredient(models.Model):
    name = models.CharField(max_length=256)


class IngredientRecipe(models.Model):
    recipe = models.ForeignKey(Recipe)
    ingredient = models.ForeignKey(Ingredient)


class Category(models.Model):
    name = models.CharField(max_length=256)


class CategoryRecipe(models.Model):
    recipe = models.ForeignKey(Recipe)
    category = models.ForeignKey(Category)

