
__author__ = 'alacambra'


# f = open("../data/receptes-v01.csv")
# for line in f:
#     element = line[:-1].split(",")
#     print element[7]

import models


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

            if element[self.isbn_col] not in self.books:

                book = models.Book(isbn=element[self.isbn_col], title=element[self.title_col])
                book.save()
                book = models.Book.objects.latest('id')
                self.books[element[self.isbn_col]] = book

            recipe = models.Recipe(
                title=element[self.recipe_col],
                book=self.books[element[self.isbn_col]],
                rating=element[self.recipe_rate_col] if element[self.recipe_rate_col] is not '' else 0)

            recipe.save()

            self.recipes.append(models.Recipe.objects.latest("id"))

    def fetch(self):
        total_books = models.Book.objects.count()
        print total_books

        recipes = models.Recipe.objects.all()

        for r in recipes:
            print unicode(r.__str__())

