import logging

import jwt
from django.conf import settings
from keycloak import KeycloakOpenID
from rest_framework import exceptions

logger = logging.getLogger(__name__)

def extract_token(request):
    authorization_header = request.headers.get('Authorization')

    if not authorization_header:
        return None

    try:
        access_token = authorization_header.split(' ')[1]
        # print(f'Access Token: {access_token}')
        # print(f'Secret Token: {settings.SECRET_KEY}')
        keycloak = KeycloakOpenID(server_url=settings.KEYCLOAK_URL,
                                  client_id=settings.KEYCLOAK_CLIENT,
                                  realm_name=settings.KEYCLOAK_REALM,
                                  client_secret_key=settings.KEYCLOAK_CLIENT_SECRET

                                  )

        payload = keycloak.decode_token(
            access_token
        )

        return payload
    except jwt.ExpiredSignatureError as e:
        raise exceptions.AuthenticationFailed('Token has expired')
    except IndexError as e:
        print("Invalid at: ")
        print(e)
        raise exceptions.AuthenticationFailed('Invalid token')
    except Exception as e:
        print("Something went wrong: ")
        print(e)


    return False
