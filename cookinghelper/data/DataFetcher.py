__author__ = 'alacambra'
import oauth2 as oauth
import gdata.spreadsheet as service
import urllib
from oauth2client.client import flow_from_clientsecrets

CLIENT_ID = "963685441868-8ag91gircafje6okgd31l6ubguh0r6sf.apps.googleusercontent.com"
CLIENT_SECRET = open('secret', 'r').read()
SCOPE = "https://www.googleapis.com/auth/drive.file"
URL = "https://docs.google.com/spreadsheets/export?id=12xcOBKvheKzFjZMjQ5w38USbjjoCbeDBupjpB-Cp5C0&exportFormat=csv"
REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"
# REDIRECT_URI = "http://gmail.com"

params = {
    'oauth_version': "1.0",
    'ClientId': CLIENT_ID,
    'ClientSecret': CLIENT_SECRET,
    'RedirectUri': REDIRECT_URI,
    'Scope': SCOPE
}

flow = flow_from_clientsecrets(
    'client_secret_963685441868-6ffrl5cljq2blsicrfq91vg0f3lrcdv0.apps.googleusercontent.com.json',
    scope=SCOPE,
    redirect_uri=REDIRECT_URI)
#
# from oauth2client.client import OAuth2WebServerFlow
# flow = OAuth2WebServerFlow(client_id=CLIENT_ID,
#                            client_secret=CLIENT_SECRET,
#                            scope=SCOPE,
#                            redirect_uri=REDIRECT_URI)

#
# auth_uri = flow.step1_get_authorize_url()
# print auth_uri
# print urllib.urlopen(auth_uri).read()


# credentials = flow.step2_exchange("4/p7LTOTHaPGpH3uHqbPUPFhzqIg9OjV98NgxT2lnXZik.8lWZf5DTT_odYFZr95uygvW5eQw4kwI")
# print credentials
#
# import httplib2
# http = httplib2.Http()
# http = credentials.authorize(http)
