from drf_base64.fields import Base64ImageField
from rest_framework import serializers

from profile_service.apps.profiles.models import UserProfile


class ProfileReadSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserProfile
        fields = '__all__'

class ProfileWriteSerializer(serializers.ModelSerializer):
    profile_picture = serializers.CharField(required=False, allow_blank=True)
    profile_cover = serializers.CharField(required=False, allow_blank=True)

    class Meta:
        model = UserProfile
        fields = [
            'uid',
            'username',
            'email',
            'full_name',
            'profile_picture',
            'profile_cover',
            'bio',
        ]
        read_only_fields = [
            'is_active',
            'date_joined'
        ]

class ToggleActiveSerializer(serializers.ModelSerializer):

    class Meta:
        fields = [
            'active',

        ]