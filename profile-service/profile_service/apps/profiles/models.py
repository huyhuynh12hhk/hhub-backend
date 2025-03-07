import uuid

from django.db import models
from django.utils import timezone


# Create your models here.
class UserProfile(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    uid = models.CharField(max_length=36, unique=True)
    username = models.CharField(max_length=100, unique=True)
    email = models.EmailField(blank=True)
    full_name = models.CharField(max_length=100, blank=True)
    profile_picture = models.ImageField(upload_to='user/profile_picture/%Y/%m/%d/', blank=True)
    profile_cover = models.ImageField(upload_to='user/cover_picture/%Y/%m/%d/', blank=True)
    bio = models.CharField(max_length=150, blank=True)
    is_active = models.BooleanField(default=True)
    date_joined = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return f'{self.uid} | @{self.username} - {self.full_name} | active {self.is_active}'
