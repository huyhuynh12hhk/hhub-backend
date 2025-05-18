import json

import jwt
from jwt.algorithms import RSAAlgorithm
from rest_framework import authentication, exceptions
from django.conf import settings
import requests

import logging

logger = logging.getLogger(__name__)

class ResourceServerJWTAuthentication(authentication.BaseAuthentication):
    def __init__(self):
        self.jwks_url = f"{settings.AUTH_SERVER}/oauth2/jwks"
        self._jwks_cache = None

        if not self.jwks_url:
            raise exceptions.AuthenticationFailed('JWKS_URL setting is not configured.')

    def fetch_jwks(self):
        """Fetches and caches the JWKS."""
        if self._jwks_cache:
            return self._jwks_cache

        try:
            response = requests.get(self.jwks_url)
            response.raise_for_status()
            self._jwks_cache = response.json()
            return self._jwks_cache
        except requests.exceptions.RequestException as e:
            raise exceptions.AuthenticationFailed(f'Error fetching JWKS: {e}')
        return None

    def get_key_from_jwks(self, jwks, kid):
        """Finds the key with the matching KID in the JWKS."""
        for key_data in jwks.get('keys', []):
            if key_data.get('kid') == kid:
                return key_data
        return None

    def authenticate(self, request):
        auth_header = authentication.get_authorization_header(request).split()

        if not auth_header or auth_header[0].lower() != b'bearer':
            return None


        if len(auth_header) == 1:
            raise exceptions.AuthenticationFailed('Invalid token header. No credentials provided.')
        elif len(auth_header) > 2:
            raise exceptions.AuthenticationFailed('Invalid token header. Token string should not contain spaces.')

        token = auth_header[1]

        try:
            headers = jwt.get_unverified_header(token)
            kid = headers.get('kid')
            if not kid:
                raise exceptions.AuthenticationFailed('JWT header missing "kid".')

            jwks = self.fetch_jwks()
            if not jwks:
                raise exceptions.AuthenticationFailed('Failed to retrieve JWKS.')

            key_data = self.get_key_from_jwks(jwks, kid)
            if not key_data:
                raise exceptions.AuthenticationFailed(f'No matching key found for KID: {kid}')

            public_key = RSAAlgorithm.from_jwk(json.dumps(key_data))
            payload = jwt.decode(
                token,
                public_key,
                algorithms=[headers.get('alg', 'RS256')],
                options = {"verify_aud": False, "verify_signature": True}
            )
            logger.debug(payload)
            # You might want to fetch and associate a user based on the payload
            # For example, if the payload contains a 'sub' (subject) claim:
            # user = self.get_user_from_payload(payload)
            return (None, payload)  # Return (user, auth) - we don't have a Django user here

            # return (user_id, decoded_token) # Or (user, decoded_token) after fetching user
        except jwt.ExpiredSignatureError:
            raise exceptions.AuthenticationFailed('Token has expired.')
        except jwt.InvalidTokenError as e:
            logger.error(f'Error at {e}')
            raise exceptions.AuthenticationFailed('Invalid token.')
        except Exception as e:
            raise exceptions.AuthenticationFailed(f'Authentication error: {e}')