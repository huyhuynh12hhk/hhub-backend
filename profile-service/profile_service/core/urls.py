from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static
from drf_spectacular.views import SpectacularAPIView, SpectacularRedocView, SpectacularSwaggerView

import profile_service.apps.profiles.urls
# from profile_service.apps.profiles.views import MyAPIView

urlpatterns = [
    path("admin/", admin.site.urls),
    path('', include(profile_service.apps.profiles.urls)),
    # path('dummy', MyAPIView.as_view(), name="dummy")
]

# if settings.DEBUG:
urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
