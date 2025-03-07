import logging

import jwt
from django.conf import settings
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

        payload = jwt.decode(
            jwt=access_token,
            key=settings.SECRET_KEY,
            algorithms=["HS512"],
        )

        return payload
    except jwt.ExpiredSignatureError as e:
        raise exceptions.AuthenticationFailed('Token has expired')
    except IndexError as e:
        raise exceptions.AuthenticationFailed('Invalid token')
    except Exception as e:
        logger.error("Something went wrong: ", e)

    return False
