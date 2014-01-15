package controllers

import play.api._
import play.api.mvc._


import anorm._
import play.api.db.DB
import play.api.Play.current

import anorm.SqlParser._

class Post(val id: Int, val text: String, val author: String)

object Hello extends Controller {

  def world = Action {

    var result : SqlRow = null

    DB.withConnection { implicit c =>

      val selectPosts = SQL("""
        Select  posts.id,
                posts.text,
                users.login
        from    posts
        join    users
             on users.id = posts.u_id
        order by date desc, time desc, id desc
                                """)

      var posts = selectPosts().map(row =>
        new Post(row[Int]("id"), row[String]("text"), row[String]("users.login"))
      ).toList

      var menuPosts = posts

      Ok(views.html.myviews.shit2(
        "Üdv az első Play frameworkös Scala projektemben! - Haszprus",
        posts,
        menuPosts
      ))
    }
  }

}