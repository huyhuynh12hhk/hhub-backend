using blog_api.Configuration;
using blog_api.Entities;
using blog_api.Services;

using MongoDB.Driver;

var builder = WebApplication.CreateBuilder(args);
var config = builder.Configuration;

var mongoConfig = config.GetSection("MongoDB")!.Get<MongoDBSettings>()!;
var env = builder.Environment.EnvironmentName;

string connectionString = env.ToLower().Equals("production") ? config["DB_CONNECT"]! : mongoConfig.ConnectionString!;

builder.Services.AddControllers()
    //.AddNewtonsoftJson()
    ;

ConfigExtensions.SetupNewtonsoftJson();

builder.Services.AddServiceSwagger(config);
builder.Services.AddJWTAuthorization(config);

builder.Services.AddSingleton<IMongoClient>(_ =>
{
    return new MongoClient(connectionString);
});
builder.Services.AddSingleton(_ =>
{
    var mongoClient = new MongoClient(connectionString);
    return mongoClient.GetDatabase(mongoConfig.DatabaseName);
});

builder.Services.AddAutoMapper(typeof(Program));
builder.Services.AddRepository<Post>(mongoConfig.PostsCollectionName);
builder.Services.AddRepository<Comment>(mongoConfig.CommentsCollectionName);
builder.Services.AddScoped<IPostService, PostService>();
builder.Services.AddScoped<ICommentService, CommentService>();

var app = builder.Build();

app.Logger.LogInformation($"Start application in \"{env}\" environment.");
app.UseServiceSwagger(config);

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();