from libxml2mod import name
from django.shortcuts import render
from django.http import HttpResponse
from models import *
from django.db.models import Q


# Create your views here.
def index(request):
    ingredients = Ingredient.objects.all(name="chicoree")
    context = {'ingredients': [ingredients]}
    return render(request, 'recipe_indexer/index.html', context)


def search(request, criteria, params):

    if criteria == "ingredient":
        ingredients = params.split(",")
        q = Q()

        for ingredient in ingredients:
            q.add(Q(ingredients__name__startswith=ingredient.lower()), Q.OR)

        recipes = Recipe.objects.all().filter(q)

        r = "<br>"
        for recipe in recipes:
            r += unicode(recipe.book.title + " : " + str(recipe.page) + " : " + recipe.title) + "<br>"

        return HttpResponse(criteria + " : " + params + r)
    elif criteria == "category":
        cats = params.split(",")
        q = Q()

        for cat in cats:
            q.add(Q(categories__name__startswith=cat.lower()), Q.OR)

        recipes = Recipe.objects.all().filter(q)

        r = "<br>"
        for recipe in recipes:
            r += unicode(recipe.book.title + " : " + str(recipe.page) + " : " + recipe.title) + "<br>"

        return HttpResponse(criteria + " : " + params + r)
    else:
        return HttpResponse(criteria + " not already implemented or inexistent :p")
