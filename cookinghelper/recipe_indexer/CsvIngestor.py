
__author__ = 'alacambra'

from models import *


class Ingestor:

    isbn_col = 0
    book_title_col = 1
    book_key_words_col = 2
    recipe_title_col = 3
    page_col = 4
    ingredients_col = 5
    recipe_cat_col = 6
    recipe_rate_col = 7

    books = {}
    recipes = {}

    ingredients = {}
    categories = {}

    def __init__(self):
        pass

    def load(self, filename="data/receptes-v01.csv"):
        f = open(filename)

        i = 0
        for line in f:
            if i == 0:
                i += 1
                continue

            element = line[:-1].split(",")

            self.save_book(element)
            self.save_recipe(element)

    def save_book(self, element):

        if element[self.isbn_col] not in self.books:
            book = Book(isbn=element[self.isbn_col], title=element[self.book_title_col])
            book.save()
            self.books[element[self.isbn_col]] = book
            return book

        return None

    def save_recipe(self, element):
        print element
        recipe = Recipe(
            title=element[self.recipe_title_col],
            book=self.books[element[self.isbn_col]],
            rating=element[self.recipe_rate_col] if element[self.recipe_rate_col] is not '' else 0,
            page=element[self.page_col])

        recipe.save()
        self.recipes[element[self.recipe_title_col]] = recipe

        self.save_ingredients(element, recipe)
        self.save_category(element, recipe)

    def save_ingredients(self, element, recipe):
        ingredients = element[self.ingredients_col]
        ingredients = ingredients.split(";")

        for ingredient_name in ingredients:

            if not self.element_exists(ingredient_name, Ingredient, element_buffer=self.ingredients):
                ingredient = Ingredient(name=ingredient_name.lower().strip())
                ingredient.save()
                self.ingredients[ingredient_name] = ingredient

            recipe.ingredients.add(self.ingredients[ingredient_name])

    def save_category(self, element, recipe):
        categories = element[self.recipe_cat_col].split(";")

        for category_name in categories:

            if len(category_name) == 0:
                continue

            if not self.element_exists(category_name, RecipesCategory, element_buffer=self.categories):
                category = RecipesCategory(name=category_name.lower().strip())
                category.save()
                self.categories[category_name] = category

            recipe.categories.add(self.categories[category_name])

    @staticmethod
    def element_exists(key_to_search, orm_class, key="name", element_buffer={}):
        if key_to_search in element_buffer:
            return True

        query_set = orm_class.objects.extra(where=[key + '=%s'], params=[key_to_search])

        if query_set.count() == 0:
            return False

        if query_set.count() > 1:
            print "found the same element more than once: " + key_to_search

        element_buffer[key_to_search] = query_set.iterator().next()

        return True