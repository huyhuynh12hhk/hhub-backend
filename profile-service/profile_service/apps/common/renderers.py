

from rest_framework.renderers import JSONRenderer

from profile_service.apps.common.utils.response import BaseResponse


class CustomBaseResponseRenderer(JSONRenderer):

    def render(self, data, accepted_media_type=None, renderer_context=None):
        response = renderer_context.get('response')
        # If the response indicates an exception, return data unwrapped.
        if response and response.exception:
            return super().render(data, accepted_media_type, renderer_context)
        wrapped = BaseResponse.success(response.data, response.status_code)
        return super().render(wrapped, accepted_media_type, renderer_context)