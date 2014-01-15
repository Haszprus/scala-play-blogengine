package controllers

import play.api._
import play.api.mvc._


import anorm._
import play.api.db.DB
import play.api.Play.current

import anorm.SqlParser._
import java.util.Date

class Comment(
  val id: Int,
  val date: Date,
  val text: String,
  val login: String
)

object Posts extends Controller {

  def index(id: Int) = Action {

    DB.withConnection { implicit c =>


      val selectMenuPosts = SQL("""
        Select  posts.id,
                posts.text,
                users.login
        from    posts
        join    users
             on users.id = posts.u_id
        order by date desc, time desc, id desc
                            """)

      var menuPosts = selectMenuPosts().map(row =>
        new Post(row[Int]("id"), row[String]("text"), row[String]("users.login"))
      ).toList


      val selectPosts = SQL("""
        Select  posts.id,
                posts.text,
                users.login
        from    posts
        join    users
             on users.id = posts.u_id
        where   posts.id = """ + id + """
                            """)

      var posts = selectPosts().map(row =>
        new Post(row[Int]("id"), row[String]("text"), row[String]("users.login"))
      ).toList


      val selectComments = SQL("""
        Select   comments.id,
                 comments.date,
                 comments.time,
                 comments.text,
                 users.login
        from     comments
        join     users
              on users.id = comments.u_id
        where    comments.p_id = """ + id + """
        order by date asc, time asc, id asc
                                           """)

      var comments = selectComments().map(row =>
        new Comment(row[Int]("id"), row[Date]("date"), row[String]("text"), row[String]("users.login"))
      ).toList

      Ok(views.html.myviews.posts(
        "",
        posts,
        menuPosts,
        comments
      ))
    }
  }

}