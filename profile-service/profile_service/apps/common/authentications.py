import jwt

from django.conf import settings
from django.contrib.auth import get_user_model
from django.middleware.csrf import CsrfViewMiddleware

from rest_framework import authentication, exceptions

from profile_service.apps.common.utils.token import extract_token


class JWTAuthentication(authentication.BaseAuthentication):
    def authenticate(self, request):

        print("Start access resource")
        # extract token
        data = extract_token(request)
        if data is None:
            return None
        if data is False:
            raise exceptions.AuthenticationFailed('Invalid Token')

        # check claim
        print("Jwt payload: ",data )
        # return (user, None)

    # def
