from rest_framework.routers import SimpleRouter, DefaultRouter

from django.conf import settings

from profile_service.apps.profiles.views import ProfileViewSet

# print(f"Path: {settings.BASE_DIR}")

router = SimpleRouter(trailing_slash=False)
# router = DefaultRouter()
router.register(r'profiles', ProfileViewSet, basename='profiles')
# router.register('accounts', AccountViewSet)
urlpatterns = router.urls
