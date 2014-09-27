from libxml2mod import name
from django.shortcuts import render
from django.http import HttpResponse
from models import *


# Create your views here.
def index(request):
    ingredients = Ingredient.objects.get(name="chicoree")
    context = {'ingredients': [ingredients]}
    return render(request, 'recipe_indexer/index.html', context)