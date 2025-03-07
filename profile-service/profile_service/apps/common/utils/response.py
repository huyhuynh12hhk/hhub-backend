
class BaseResponse:
    @staticmethod
    def success(data, code=200):
        return {
            'data': data,
            'code': code
        }

    @staticmethod
    def fail(message, code=400):
        return {
            'message': message,
            'code': code
        }