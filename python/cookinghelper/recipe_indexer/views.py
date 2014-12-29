from libxml2mod import name
from django.shortcuts import render
from django.http import HttpResponse
from models import *
from django.db.models import Q


def search_from_url(request, criteria, params):

    if criteria == "ingredient":
        ingredients = params.split(",")
        q = Q()

        for ingredient in ingredients:
            q.add(Q(ingredients__name__startswith=ingredient.lower()), Q.OR)

        recipes = Recipe.objects.all().filter(q)

        result = []
        for recipe in recipes:
            result.append((unicode(recipe.book.title), str(recipe.page), unicode(recipe.title)))

        return __prepare_results__(request, recipes)

    elif criteria == "category":
        categories = params.split(",")
        q = Q()

        for category in categories:
            q.add(Q(categories__name__startswith=category.lower()), Q.OR)

        recipes = Recipe.objects.all().filter(q)

        return __prepare_results__(request, recipes)
    else:
        return HttpResponse(criteria + " not already implemented or inexistent :p")


def search_full_intersection(request):
    recipes = []
    categories_post = ""
    ingredients_post = ""

    if request._get_post():
        categories_post = request._get_post()["category"]
        ingredients_post = request._get_post()["ingredients"]

        categories = categories_post.split(",")
        ingredients = ingredients_post.split(",")

        q_ingredients = Q()

        rs = None
        for ingredient in ingredients:
            if ingredient is not "":
                if rs is None:
                    rs = Recipe.objects.filter(ingredients__name__startswith=ingredient.lower())
                else:
                    rs &= Recipe.objects.filter(ingredients__name__startswith=ingredient.lower())

                q_ingredients.add(Q(ingredients__name__startswith=ingredient.lower()), Q.OR)

        q_categories = Q()

        for category in categories:

            if category is not "":
                if rs is None:
                    rs = Recipe.objects.filter(categories__name__startswith=category.lower())
                else:
                    rs &= Recipe.objects.filter(categories__name__startswith=category.lower())

                q_categories.add(Q(categories__name__startswith=category.lower()), Q.OR)

        q = Q(q_categories & q_ingredients)
        recipes = Recipe.objects.all().filter(q)
        recipes = rs
        print recipes.query

    return __prepare_results__(request, recipes,
                               context={"type": "intersection", "cs": categories_post, "ins": ingredients_post})


def search_standard(request):
    recipes = []
    categories_post = ""
    ingredients_post = ""
    if request._get_post():
        categories_post = request._get_post()["category"]
        ingredients_post = request._get_post()["ingredients"]

        categories = categories_post.split(",")
        ingredients = ingredients_post.split(",")

        q_ingredients = Q()

        for ingredient in ingredients:
            if ingredient is not "":
                q_ingredients.add(Q(ingredients__name__startswith=ingredient.lower()), Q.OR)

        q_categories = Q()

        for category in categories:

            if category is not "":
                q_categories.add(Q(categories__name__startswith=category.lower()), Q.OR)

        q = Q(q_categories & q_ingredients)
        recipes = Recipe.objects.all().filter(q)
        print recipes.query

    return __prepare_results__(request, recipes,
                               context={"type": "standard", "cs": categories_post, "ins": ingredients_post})


def __prepare_results__(request, query_set, context={}):

    recipes = []

    for recipe in query_set:
        recipes.append((unicode(recipe.book.title), str(recipe.page), unicode(recipe.title)))

    context['recipes'] = recipes
    return render(request, 'recipe_indexer/list.html', context)






















