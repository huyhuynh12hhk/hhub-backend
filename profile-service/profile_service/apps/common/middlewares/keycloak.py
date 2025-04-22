import logging
import re

import json
from django.conf import settings
from django.http.response import JsonResponse
from django.utils.deprecation import MiddlewareMixin
from keycloak import KeycloakOpenID
from keycloak.exceptions import KeycloakInvalidTokenError
from rest_framework.exceptions import PermissionDenied, AuthenticationFailed, NotAuthenticated

logger = logging.getLogger(__name__)

class KeycloakMiddleware(MiddlewareMixin):
    def __init__(self, get_response):
        """
        :param get_response:
        """

        # self.config = settings.KEYCLOAK_CONFIG

        # # Read configurations
        # try:
        #     self.server_url = self.config['KEYCLOAK_SERVER_URL']
        #     self.client_id = self.config['KEYCLOAK_CLIENT_ID']
        #     self.realm = self.config['KEYCLOAK_REALM']
        # except KeyError as e:
        #     raise Exception("KEYCLOAK_SERVER_URL, KEYCLOAK_CLIENT_ID or KEYCLOAK_REALM not found.")
        #
        # self.client_secret_key = self.config.get('KEYCLOAK_CLIENT_SECRET_KEY', None)
        # self.client_public_key = self.config.get('KEYCLOAK_CLIENT_PUBLIC_KEY', None)
        # self.default_access = self.config.get('KEYCLOAK_DEFAULT_ACCESS', "DENY")
        # self.method_validate_token = self.config.get('KEYCLOAK_METHOD_VALIDATE_TOKEN', "INTROSPECT")
        # self.keycloak_authorization_config = self.config.get('KEYCLOAK_AUTHORIZATION_CONFIG', None)

        # Create Keycloak instance
        self.keycloak = KeycloakOpenID(server_url=settings.KEYCLOAK_URL,
                                  client_id=settings.KEYCLOAK_CLIENT,
                                  realm_name=settings.KEYCLOAK_REALM,
                                  client_secret_key=settings.KEYCLOAK_CLIENT_SECRET)


        # # Read policies
        # if self.keycloak_authorization_config:
        #     self.keycloak.load_authorization_config(self.keycloak_authorization_config)

        # Django
        self.get_response = get_response

    @property
    def keycloak(self):
        return self._keycloak

    @keycloak.setter
    def keycloak(self, value):
        self._keycloak = value

    def __call__(self, request):
        """
        :param request:
        :return:
        """
        return self.get_response(request)

    def process_view(self, request, view_func, view_args, view_kwargs):
        """
        Validate only the token introspect.
        :param request: django request
        :param view_func:
        :param view_args: view args
        :param view_kwargs: view kwargs
        :return:
        """

        if hasattr(settings, 'KEYCLOAK_BEARER_AUTHENTICATION_EXEMPT_PATHS'):
            path = request.path_info.lstrip('/')

            if any(re.match(m, path) for m in
                   settings.KEYCLOAK_BEARER_AUTHENTICATION_EXEMPT_PATHS):
                print('** exclude path found, skipping')
                return None

        try:
            view_scopes = view_func.cls.keycloak_scopes
            print("Scopes")
            print(view_scopes)
        except AttributeError as e:
            print(
                'Allowing free acesss, since no authorization configuration (keycloak_scopes) found for this request route :%s',
                request)
            return None

        if 'HTTP_AUTHORIZATION' not in request.META:
            return JsonResponse({"detail": NotAuthenticated.default_detail},
                                status=NotAuthenticated.status_code)

        auth_header = request.META.get('HTTP_AUTHORIZATION').split()
        token = auth_header[1] if len(auth_header) == 2 else auth_header[0]

        # Get default if method is not defined.
        required_scope = view_scopes.get(request.method, None) \
            if view_scopes.get(request.method, None) else view_scopes.get('DEFAULT', None)

        print("required_scope")
        print(required_scope)

        # # DEFAULT scope not found and DEFAULT_ACCESS is DENY
        # if not required_scope and self.default_access == 'DENY':
        #     return JsonResponse({"detail": PermissionDenied.default_detail},
        #                         status=PermissionDenied.status_code)

        try:
            user_permissions = self.keycloak.get_permissions(token,
                                                             method_token_info=self.method_validate_token.lower(),
                                                             key=self.client_public_key)

            print("user_permissions")
            print(user_permissions)
        except KeycloakInvalidTokenError as e:
            return JsonResponse({"detail": AuthenticationFailed.default_detail},
                                status=AuthenticationFailed.status_code)

        for perm in user_permissions:
            if required_scope in perm.scopes:
                return None

        # User Permission Denied
        return JsonResponse({"detail": PermissionDenied.default_detail},
                            status=PermissionDenied.status_code)
