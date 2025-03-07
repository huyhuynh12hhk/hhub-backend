from django.contrib import admin

from profile_service.apps.profiles.models import UserProfile

# Register your models here.
admin.site.register(UserProfile)
