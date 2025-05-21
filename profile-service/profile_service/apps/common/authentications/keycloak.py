# import logging
#
# import jwt
# from django.contrib.auth.models import User
# from rest_framework.authentication import BaseAuthentication
# from keycloak import KeycloakOpenID, keycloak_openid
#
# from django.conf import settings
#
# logger = logging.getLogger(__name__)
#
#
# class KeycloakAuthentication(BaseAuthentication):
#     def authenticate(self, request):
#         token = request.META.get('HTTP_AUTHORIZATION', '').split('Bearer ')[-1]
#         # print("Token")
#         # print(token)
#
#         payload = jwt.decode(
#             jwt=token,
#             options={"verify_signature": False}
#         )
#         print("React token ")
#         print(payload)
#
#         keycloak = KeycloakOpenID(server_url=settings.KEYCLOAK_URL,
#                                   client_id=settings.KEYCLOAK_CLIENT,
#                                   realm_name=settings.KEYCLOAK_REALM,
#                                   client_secret_key=settings.KEYCLOAK_CLIENT_SECRET)
#
#         # Token exchange request
#         token_response = keycloak.decode_token(token=token)
#
#
#
#         return (None, token_response)
#         # return False
