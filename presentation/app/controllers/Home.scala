package controllers

import com.typesafe.scalalogging.LazyLogging
import play.api.Play.current
import play.api.mvc._

class Home extends Controller with LazyLogging {
  def index = Action {
    Ok(views.html.home.index())
  }

  def about = Action {
    Ok(views.html.home.about())
  }

  def contact = Action {
    Ok(views.html.home.contact())
  }
}

