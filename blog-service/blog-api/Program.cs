using blog_api.Configuration;
using blog_api.Entities;
using blog_api.Services;

using MongoDB.Driver;

var builder = WebApplication.CreateBuilder(args);
var config = builder.Configuration;

var mongoConfig = config.GetSection("MongoDB")!.Get<MongoDBSettings>()!;
var env = builder.Environment.EnvironmentName;

string connectionString = env.ToLower().Equals("production") ? config["DB_CONNECT"]! : mongoConfig.ConnectionString!;
// Add services to the container.



builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
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
app.UseSwagger();
app.UseSwaggerUI();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();