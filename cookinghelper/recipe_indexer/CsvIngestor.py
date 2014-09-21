
__author__ = 'alacambra'

from models import *


class Ingestor:

    isbn_col = 0
    title_col = 1
    book_cat_col = 2
    recipe_col = 3
    page_col = 4
    ingredients_col = 5
    recipe_cat_col = 6
    recipe_rate_col = 7

    books = {}
    recipes = []

    ingredients = []
    categories = []

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
            book = Book(isbn=element[self.isbn_col], title=element[self.title_col])
            book.save()
            book = Book.objects.latest('id')
            self.books[element[self.isbn_col]] = book
            return book

        return None

    def save_recipe(self, element):
        recipe = Recipe(
            title=element[self.recipe_col],
            book=self.books[element[self.isbn_col]],
            rating=element[self.recipe_rate_col] if element[self.recipe_rate_col] is not '' else 0)

        recipe.save()
        recipe = Recipe.objects.latest("id")
        self.save_ingredients(element, recipe)
        self.save_category(element, recipe)
        self.recipes.append(recipe)

    def save_page(self, element):
        page = BookRecipe(book=self.books[element[self.isbn_col]], recipe=Recipe.objects.latest("id"),
                                 page=element[self.page_col])
        page.save()

    def save_ingredients(self, element, recipe):
        ingredients = element[self.ingredients_col]
        ingredients = ingredients.split(";")

        for ingredient_name in ingredients:

            if not self.element_exists(ingredient_name, Ingredient, self.ingredients):
                ingredient = Ingredient(name=ingredient_name.lower().strip())
                ingredient.save()

            ingredient_recipe = IngredientRecipe(ingredient=Ingredient.objects.latest("id"), recipe=recipe)
            ingredient_recipe.save()

    def save_category(self, element, recipe):
        categories = element[self.recipe_cat_col].split(";")
        for category_name in categories:

            if len(category_name) == 0:
                continue

            if not self.element_exists(category_name, Category, self.categories):
                category = Category(name=category_name.lower().strip())
                category.save()

            category_recipe = CategoryRecipe(category=Category.objects.latest("id"), recipe=recipe)
            category_recipe.save()

    @staticmethod
    def fetch():
        total_books = Book.objects.count()
        print total_books

        recipes = Recipe.objects.all()

        for r in recipes:
            print unicode(r.__str__())

    def get_ingredient(self, ingredient):
        pass

    def preload_ingredients(self):
        pass

    # def ingredient_exists(self, ingredient_name):
    #
    #     if ingredient_name in self.ingredients:
    #         return True
    #
    #     ingredient_query_set = Ingredient.objects.extra(where=['name=%s'], params=[ingredient_name])
    #
    #     if ingredient_query_set.count() > 1:
    #         print "found the same ingredient more than once: " + ingredient_name
    #
    #     self.ingredients.append(ingredient_name)
    #
    #     return ingredient_query_set.count() > 0

    @staticmethod
    def element_exists(name_to_search, orm_class, element_buffer=[]):
        if name_to_search in element_buffer:
            return True

        query_set = orm_class.objects.extra(where=['name=%s'], params=[name_to_search])

        if query_set.count() > 1:
            print "found the same ingredient more than once: " + name_to_search

        element_buffer.append(name_to_search)

        return query_set.count() > 0

