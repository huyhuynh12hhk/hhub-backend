from django.http import Http404

# Create your views here.

import django_filters.rest_framework
from rest_framework import viewsets, status, permissions, exceptions
from rest_framework.decorators import action
from rest_framework.response import Response

from profile_service.apps.common.parsers.camel_case import NoUnderscoreBeforeNumberCamelCaseJSONParser
from profile_service.apps.common.permissions.privacy import IsOwner
from profile_service.apps.common.permissions.role import IsAdminRole
from profile_service.apps.common.utils.response import BaseResponse
from profile_service.apps.profiles.models import UserProfile
from profile_service.apps.profiles.serializers.profile import ProfileWriteSerializer, ProfileReadSerializer, \
    ToggleActiveSerializer


# Create your views here.


class ProfileViewSet(viewsets.ModelViewSet):
    queryset = UserProfile.objects.all()
    parser_classes = (NoUnderscoreBeforeNumberCamelCaseJSONParser,)
    # filter_backends = (django_filters.rest_framework.DjangoFilterBackend,)

    def get_serializer_class(self):

        if self.action in ['list', 'retrieve']:
            return ProfileReadSerializer

        return ProfileWriteSerializer

    def get_permissions(self):
        if self.action in ['retrieve', 'create']:
            permission_classes = [permissions.AllowAny]
        elif self.action in ['update', 'partial_update', 'set_active_by_path']:
            permission_classes = [IsAdminRole | IsOwner]
        elif self.action in ['destroy', 'list']:
            permission_classes = [IsAdminRole]
        else:
            permission_classes = [permissions.IsAuthenticated]
        return [perm() for perm in permission_classes]

    def finalize_response(self, request, response, *args, **kwargs):
        response = super().finalize_response(request, response, *args, **kwargs)
        if not response.exception:
            response.data = BaseResponse.success(response.data, response.status_code)
        return response

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data, context=self.get_serializer_context())
        serializer.is_valid(raise_exception=True)
        instance = serializer.save()
        read_serializer = ProfileReadSerializer(instance)
        headers = self.get_success_headers(read_serializer.data)
        return Response(read_serializer.data, status=status.HTTP_201_CREATED, headers=headers)

    def get_object(self):
        uid = self.kwargs.get('pk')
        try:
            # Lookup the profile by the related user's username.
            return UserProfile.objects.get(uid=uid)
        except UserProfile.DoesNotExist:
            raise exceptions.NotFound("Profile not found.")

    def retrieve(self, request, *args, **kwargs):
        print("got here")
        instance = self.get_object()  # Uses our custom lookup
        serializer = self.get_serializer(instance)
        return Response(serializer.data)


    @action(
        detail=False,
        methods=['post'],
        url_path='set-activate/(?P<uid>[^/.]+)',
        serializer_class=ToggleActiveSerializer
    )
    def set_activate(self, request, uid=None):
        active_value = request.query_params.get('active')

        if not uid or active_value is None:
            raise Http404

        try:
            profile = UserProfile.objects.get(uid=uid)
        except UserProfile.DoesNotExist:
            return Response(
                data=BaseResponse.fail(
                    message="Profile not exist",
                    code=status.HTTP_404_NOT_FOUND
                ),
                status=status.HTTP_404_NOT_FOUND
            )

        if isinstance(active_value, str):
            active_bool = active_value.lower() in ('true', '1', 'yes')
        else:
            active_bool = bool(active_value)

        profile.is_active = active_bool
        profile.save()

        serializer = ProfileReadSerializer(profile)
        return Response(
            BaseResponse.success(
                data=serializer.data,
                code=status.HTTP_404_NOT_FOUND
            )
            , status=status.HTTP_202_ACCEPTED
        )


