from django.db import models


class Book(models.Model):
    isbn = models.CharField(max_length=17, unique=True)
    title = models.CharField(max_length=254)


class Ingredient(models.Model):
    name = models.CharField(max_length=254, unique=True)


class RecipesCategory(models.Model):
    name = models.CharField(max_length=254, unique=True)


class Recipe(models.Model):
    title = models.CharField(max_length=254, unique=True)
    book = models.ForeignKey(Book)
    rating = models.IntegerField(default=0)
    page = models.IntegerField(default=-1)
    ingredients = models.ManyToManyField(Ingredient)
    categories = models.ManyToManyField(RecipesCategory)

    def __str__(self):

        return unicode("recipe " + self.title + " from " + self.book.isbn)

    def __repr__(self):
        return self.__str__()
