from django.contrib.auth.models import User
from django.db import models
from oauth2client.django_orm import FlowField
from oauth2client.django_orm import CredentialsField
from oauth2client.django_orm import Storage

class FlowModel(models.Model):
    id = models.ForeignKey(User, primary_key=True)
    flow = FlowField()

class CredentialsModel(models.Model):
    id = models.ForeignKey(User, primary_key=True)
    credential = CredentialsField()


user = User()
user.username = "alacambra"
storage = Storage(CredentialsModel, 'id', user, 'credential')
credential = storage.get()
storage.put(credential)