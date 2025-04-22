FROM python:3.10.4-buster AS base

ENV POETRY_HOME=/opt/poetry
ENV PATH=${POETRY_HOME}/bin:${PATH}
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
ENV PYTHONPATH .

RUN set -xe \
    && apt-get update \
    && apt-get install --no-install-recommends -y \
    curl \
    && pip install virtualenvwrapper poetry==2.0.1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*



FROM base AS dependencies

# Copy and install Python dependencies
COPY ["poetry.lock", "pyproject.toml", "./"]
RUN poetry install --no-root


FROM base AS final

COPY --from=dependencies . .


# Copy project files
# COPY .env ./
COPY .env.production ./
COPY .env.staging ./
COPY Makefile .
COPY profile_service profile_service
COPY scripts/entrypoint.sh /entrypoint.sh

# Ensure the entrypoint script is executable
RUN chmod a+x /entrypoint.sh

# Expose the Django development server port
EXPOSE 8000


ENTRYPOINT ["/entrypoint.sh"]
