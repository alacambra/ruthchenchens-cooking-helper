from libxml2mod import xmlIsBlankNode

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

        i=0
        for line in f:
            if i == 0:
                i+=1
                continue

            element = line.split(",")

            if element[self.isbn_col] not in self.books:
                self.books[element[self.isbn_col]] = \
                    models.Book(isbn=element[self.isbn_col], title=element[self.title_col])

                self.recipes.append(models.Recipe(
                    title=element[self.recipe_col],
                    book=self.books[element[self.isbn_col]],
                    rating=element[self.recipe_rate_col]))

        for b in self.books.values():
            b.save()

        # print self.recipes
        #
        for r in self.recipes:
            print r
            r.save()

