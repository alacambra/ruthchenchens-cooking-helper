from django.db import models


class Book(models.Model):
    question = models.CharField(max_length=13)
    title = models.CharField(max_length=256)


class Recipe(models.Model):
    title = models.CharField(max_length=256)
    book = models.ForeignKey(Book)
    rating = models.IntegerField(default=0)


class BookRecipe(models.Model):
    book = models.ForeignKey(Book)
    recipe = models.ForeignKey(Recipe)
    page = models.IntegerField(default=0)


class Ingredient(models.Model):
    recipe = models.ForeignKey(Recipe)
    name = models.CharField(max_length=256)


class Category(models.Model):
    recipe = models.ForeignKey(Recipe)
    name = models.CharField(max_length=256)

