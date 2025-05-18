import logging

import jwt
from django.conf import settings
from rest_framework import permissions, exceptions
from rest_framework.exceptions import PermissionDenied

from profile_service.apps.common.constants.roles import RolesConst
from profile_service.apps.common.utils.token import extract_token

logger = logging.getLogger(__name__)

class IsAdminRole(permissions.BasePermission):

    def has_permission(self, request, view):

        try:
            # return True
            logger.debug("Request in Admin Permission")
            logger.debug(request.auth)
            payload = request.auth
            # extract_token(request)
            if payload is None or payload is False:
                return False

            scope = payload.get("roles")
            print(f'Roles: {scope}')

            if has_role(scope, RolesConst.ADMIN.value):
                return True

            raise PermissionDenied(detail="You do not have permission to access this resource.")

        except Exception as e:
            print("Error at has_permission, ", e)
            raise PermissionDenied(detail="Invalid token.")

        # return False

def has_role(roles: [str], role_name: str) -> bool:
    return any(role_name.lower() in x.lower().removeprefix('role_') for x in roles)

def has_scope(scope_string: str, scope: str) -> bool:
    return scope in scope_string.split()


