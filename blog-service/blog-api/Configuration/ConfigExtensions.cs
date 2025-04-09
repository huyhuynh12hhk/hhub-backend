using System.Text.Encodings.Web;
using System.Text.Json;
using System.Text.Json.Serialization;

using blog_api.Entities;
using blog_api.Repositories;

using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

using MongoDB.Driver;

using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace blog_api.Configuration
{
    public static class ConfigExtensions
    {
        //service
        public static IServiceCollection AddRepository<T>(this IServiceCollection services, string collectionName)
            where T : BaseEntity
        {
            services.AddSingleton<IMongoDBRepository<T>>(serviceProvider =>
            {
                var database = serviceProvider.GetService<IMongoDatabase>();
                return new MongoDBRepository<T>(database, collectionName);
            });

            return services;
        }

        public static IServiceCollection DefaultConfig(this IServiceCollection services)
        {
            services.Configure<JsonOptions>(options =>
            {
                options.JsonSerializerOptions.PropertyNamingPolicy = JsonNamingPolicy.CamelCase;
                options.JsonSerializerOptions.DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull;
                options.JsonSerializerOptions.WriteIndented = false;
                options.JsonSerializerOptions.Encoder = JavaScriptEncoder.Default;
                options.JsonSerializerOptions.AllowTrailingCommas = true;
                options.JsonSerializerOptions.MaxDepth = 3;
                options.JsonSerializerOptions.NumberHandling = JsonNumberHandling.AllowReadingFromString;
            });

            return services;
        }

        public static void SetupNewtonsoftJson()
        {
            JsonConvert.DefaultSettings = () => new JsonSerializerSettings
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver(),
                Formatting = Formatting.Indented,
                NullValueHandling = NullValueHandling.Ignore,
                DateFormatString = "dd-MM-yyyy",
                DefaultValueHandling = DefaultValueHandling.Ignore,
                MaxDepth = 3
            };
        }

        public static IServiceCollection AddServiceSwagger(this IServiceCollection services, IConfiguration configuration)
        {
            services.AddEndpointsApiExplorer();
            services.AddSwaggerGen(c =>
            {
                c.CustomSchemaIds(type => type.ToString());
                var securityScheme = new OpenApiSecurityScheme
                {
                    Name = "KEYCLOAK",
                    Type = SecuritySchemeType.OAuth2,
                    In = ParameterLocation.Header,
                    BearerFormat = "JWT",
                    Scheme = "bearer",
                    Flows = new OpenApiOAuthFlows
                    {
                        AuthorizationCode = new OpenApiOAuthFlow
                        {
                            AuthorizationUrl = new Uri(configuration["Jwt:AuthorizationUrl"] ?? ""),
                            TokenUrl = new Uri(configuration["Jwt:TokenUrl"] ?? ""),
                            Scopes = new Dictionary<string, string> { }
                        }
                    },
                    Reference = new OpenApiReference
                    {
                        Id = JwtBearerDefaults.AuthenticationScheme,
                        Type = ReferenceType.SecurityScheme
                    }
                };
                c.AddSecurityDefinition(securityScheme.Reference.Id, securityScheme);
                c.AddSecurityRequirement(new OpenApiSecurityRequirement{
                    {securityScheme, new string[] { }}
                });
            });

            return services;
        }

        public static IServiceCollection AddJWTAuthorization(this IServiceCollection services, IConfiguration configuration)
        {

            services.AddAuthentication(o =>
            {

                o.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                o.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;

            })
                .AddJwtBearer(o =>
                {
                    o.Authority = configuration["Jwt:Authority"];
                    o.Audience = configuration["Jwt:Audience"];

                    o.IncludeErrorDetails = true;
                    o.RequireHttpsMetadata = false;
                    o.TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuer = true,
                        ValidIssuer = configuration["Jwt:Authority"], // Or the actual issuer from your Keycloak setup
                        ValidateAudience = false,
                        ValidAudience = configuration["Jwt:Audience"],
                        ValidateLifetime = true
                    };

                    o.Events = new JwtBearerEvents()
                    {
                        OnAuthenticationFailed = c =>
                        {
                            c.NoResult();

                            c.Response.StatusCode = 401;
                            //c.Response.ContentType = "application/json";
                            c.Response.ContentType = "plain/text";

                            return c.Response.WriteAsync(c.Exception.ToString());

                            //var result = BaseResponse.Error("Error occured when request to IDP.", 401);

                            //return c.Response.WriteAsync(JsonConvert.SerializeObject(result));
                            //await c.Response.WriteAsync("Error occured when request to IDP.");
                            //throw new Exception("Error occured when request to IDP.");
                        }
                    };
                });



            return services;
        }


        // app
        public static IApplicationBuilder UseServiceSwagger(this IApplicationBuilder app, IConfiguration configuration)
        {
            app.UseSwagger();
            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", "MyAppAPI");
                c.OAuthClientId(configuration["Jwt:ClientId"]);
                c.OAuthClientSecret(configuration["Jwt:ClientSecret"]);
                c.OAuthRealm(configuration["Jwt:Realm"]);
                c.OAuthAppName("KEYCLOAK");
            });

            return app;
        }
    }
}