import jwt
from django.conf import settings
from django.test import TestCase
from django.urls import reverse
from djangorestframework_camel_case.util import underscoreize, camelize
from rest_framework import status
from rest_framework.test import APITestCase
from ..models import UserProfile
from ..serializers.profile import ProfileReadSerializer


# Create your tests here.
class ProfileTests(APITestCase):

    def authenticate(self):
        payload = {
            "iss": "hhk.com",
            "sub": "d0b281a8-9c69-46d1-b789-912807ca8f5c",
            "scope": "ROLE_ADMIN"
        }
        token = jwt.encode(payload, settings.SECRET_KEY, algorithm='HS512')
        self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {token}')

    def test_get_profiles_admin_flow(self):
        url = '/profiles'
        self.authenticate()

        response = self.client.get(url)

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data["code"], status.HTTP_200_OK)
        self.assertEqual(response.data["data"], [])

    def test_create_profile_admin_flow(self):
        url = '/profiles'
        profile_request = {
            "uid": "z22g070d-8c4c-4f0d-9d8a-162843c10111",
            "username": "john01",
            "email": "user01@gmail.com",
            "fullName": "User John",
            "bio": ""
        }
        profile_write_serializer = underscoreize(profile_request)
        profile_instance = UserProfile(**profile_write_serializer)
        profile_response = ProfileReadSerializer(profile_instance).data

        self.authenticate()

        response = self.client.post(url, profile_request)

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.data["code"], status.HTTP_201_CREATED)
        self.assertEqual(response.data["data"]["full_name"], profile_response["full_name"])
