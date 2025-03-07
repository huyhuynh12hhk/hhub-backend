from rest_framework.views import exception_handler

from profile_service.apps.common.utils.response import BaseResponse


def custom_exception_handler(exc, context):
    # Call drf default exception handler.
    response = exception_handler(exc, context)

    if response is not None:
        if isinstance(response.data, dict):
            flat_errors = []
            for field, errors in response.data.items():
                if isinstance(errors, list):
                    for err in errors:
                        flat_errors.append(f"{field}: {err}")
                else:
                    flat_errors.append(f"{field}: {errors}")
            response.data = flat_errors

        response.data = BaseResponse.fail(
            message=response.data,
            code=response.status_code
        )
    return response
