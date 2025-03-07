import jwt
from colorlog import exception
from django.conf import settings
from rest_framework import permissions
from rest_framework.exceptions import PermissionDenied

from profile_service.apps.common.utils.token import extract_token
from profile_service.apps.profiles.models import UserProfile


class IsOwner(permissions.BasePermission):

    def has_permission(self, request, view):

        try:
            payload = extract_token(request)

            sub = payload.get("sub") or ''
            print(f'Sub: {sub}')

            profile = UserProfile.objects.filter(uid=sub).first()

            if profile:
                return True

        except Exception:
            raise PermissionDenied(detail="Invalid token.")

        raise PermissionDenied(detail="You do not have permission to access this resource.")  # Explicit denial
