package routers

type _AppRouter struct{
	Friend FriendRouter
	Follow FollowRouter
}

var AppRouter = new(_AppRouter)