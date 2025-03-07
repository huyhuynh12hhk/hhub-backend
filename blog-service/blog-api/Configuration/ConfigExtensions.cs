using System.IdentityModel.Tokens.Jwt;
using System.Text;

using blog_api.Entities;
using blog_api.Repositories;

using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;

using MongoDB.Driver;

namespace blog_api.Configuration
{
    public static class ConfigExtensions
    {
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

        public static IServiceCollection AddJWTAuthorization(this IServiceCollection services, IConfiguration configuration)
        {
            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Clear();

            services.AddAuthentication(o =>
            {

                o.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                o.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
                o.DefaultSignInScheme = JwtBearerDefaults.AuthenticationScheme;
            })
                .AddJwtBearer(o =>
                {
                    o.IncludeErrorDetails = true;
                    o.RequireHttpsMetadata = false;
                    o.TokenValidationParameters = new()
                    {
                        ValidateAudience = false,
                        ValidateIssuer = false,
                        ValidateLifetime = true,
                        ValidateIssuerSigningKey = true,
                        //ValidIssuer = configuration["ISSUER"]!,
                        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["SIGNER_KEY"]!))
                    };
                    o.MapInboundClaims = false;
                });



            return services;
        }
    }
}