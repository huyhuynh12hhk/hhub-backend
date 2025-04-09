import os
from pathlib import Path
import environ

env = environ.Env()
environment = os.environ.get('ENVIRONMENT', "development")

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ROOT_DIR = os.path.dirname(BASE_DIR)

# Take environment variables from .env file
env_file = '.env'

if environment == "production":
    env_file = '.env.prod'

environ.Env.read_env(os.path.join(ROOT_DIR, env_file))

KEYCLOAK_URL=env.str('KEYCLOAK_URL')
KEYCLOAK_REALM=env.str('KEYCLOAK_REALM')
KEYCLOAK_CLIENT=env.str('KEYCLOAK_CLIENT')
KEYCLOAK_CLIENT_SECRET=env.str('KEYCLOAK_CLIENT_SECRET')

DEBUG = env.bool('DEBUG', default=False)

ALLOWED_HOSTS = env.list('ALLOWED_HOSTS', default=['localhost', '127.0.0.1'])


INTERNAL_IPS = [
    '127.0.0.1',
]

INSTALLED_APPS = [
    "django.contrib.admin",
    "django.contrib.auth",
    "django.contrib.contenttypes",
    "django.contrib.sessions",
    "django.contrib.messages",
    "django.contrib.staticfiles",
]

LOCAL_APPS = [
    'profile_service.apps.profiles.apps.ProfilesConfig',
    'profile_service.apps.common.apps.CommonConfig',
]

THIRD_PARTY_APPS = [
    'daphne',
    'rest_framework',
]

INSTALLED_APPS = THIRD_PARTY_APPS + INSTALLED_APPS + LOCAL_APPS

THIRD_PARTY_MIDDLEWARE = [
    'djangorestframework_camel_case.middleware.CamelCaseMiddleWare',
]
LOCAL_MIDDLEWARE = [
    # 'profile_service.apps.common.middlewares.keycloak.KeycloakMiddleware',
]

MIDDLEWARE = LOCAL_MIDDLEWARE + [
                 "django.middleware.security.SecurityMiddleware",
                 "django.contrib.sessions.middleware.SessionMiddleware",
                 "django.middleware.common.CommonMiddleware",
                 "django.middleware.csrf.CsrfViewMiddleware",
                 "django.contrib.auth.middleware.AuthenticationMiddleware",
                 "django.contrib.messages.middleware.MessageMiddleware",
                 "django.middleware.clickjacking.XFrameOptionsMiddleware",
             ] + THIRD_PARTY_MIDDLEWARE

ROOT_URLCONF = "profile_service.core.urls"

TEMPLATES = [
    {
        "BACKEND": "django.template.backends.django.DjangoTemplates",
        "DIRS": [],
        "APP_DIRS": True,
        "OPTIONS": {
            "context_processors": [
                "django.template.context_processors.debug",
                "django.template.context_processors.request",
                "django.contrib.auth.context_processors.auth",
                "django.contrib.messages.context_processors.messages",
            ],
        },
    },
]

WSGI_APPLICATION = "profile_service.core.wsgi.application"
ASGI_APPLICATION = 'profile_service.core.asgi.application'

# Database
# https://docs.djangoproject.com/en/5.1/ref/settings/#databases




DATABASES = {
    "default": env.db()
}

# Password validation
# https://docs.djangoproject.com/en/5.1/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        "NAME": "django.contrib.auth.password_validation.UserAttributeSimilarityValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.MinimumLengthValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.CommonPasswordValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.NumericPasswordValidator",
    },
]

# Internationalization
# https://docs.djangoproject.com/en/5.1/topics/i18n/

LANGUAGE_CODE = "en-us"

TIME_ZONE = env('TIME_ZONE', default='UTC')

USE_I18N = True

USE_TZ = True

MEDIA_URL = '/media/'
MEDIA_ROOT = os.path.join(ROOT_DIR, 'media')  # type: ignore # noqa: F821

STATIC_URL = '/static/'
STATIC_ROOT = os.path.join(ROOT_DIR, 'staticfiles')  # type: ignore # noqa: F821

DEFAULT_AUTO_FIELD = "django.db.models.BigAutoField"

REST_FRAMEWORK = {
    'DEFAULT_RENDERER_CLASSES': [
        'djangorestframework_camel_case.render.CamelCaseJSONRenderer',
        'djangorestframework_camel_case.render.CamelCaseBrowsableAPIRenderer',
        # 'profile_service.apps.common.renderers.CustomBaseResponseRenderer',
    ],
    'EXCEPTION_HANDLER': 'profile_service.apps.common.exceptions.global.custom_exception_handler',
    'DEFAULT_SCHEMA_CLASS': 'drf_spectacular.openapi.AutoSchema',

    # keycloak oauth2 mechanism
    'DEFAULT_AUTHENTICATION_CLASSES': (
        'profile_service.apps.common.authentications.keycloak.KeycloakAuthentication',
    ),

    'DEFAULTPARSERCLASSES': (
        'djangorestframework_camel_case.parser.CamelCaseFormParser',
        'djangorestframework_camel_case.parser.CamelCaseMultiPartParser',
        'djangorestframework_camel_case.parser.CamelCaseJSONParser',
    ),
    'JSON_UNDERSCOREIZE': {
        'no_underscore_before_number': True,
    },
    'TEST_REQUEST_DEFAULT_FORMAT': 'json'
}

LOGGING = {
    "version": 1,
    "disable_existing_loggers": False,
    "handlers": {
        "console": {
            "class": "logging.StreamHandler",
        },
    },
    "root": {
        "handlers": ["console"],
        "level": "WARNING",
    },
}
