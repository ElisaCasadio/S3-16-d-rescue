package it.unibo.drescue.controller

import java.util.concurrent.{ExecutorService, Executors, Future}

import it.unibo.drescue.communication.messages.Message
import it.unibo.drescue.communication.messages.requests.LoginMessageImpl
import it.unibo.drescue.connection.{RabbitMQImpl, RequestHandler}
import it.unibo.drescue.model.ObjectModel

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object LoginControllerImpl {
  val ErrorDialogTitle = "Login Error"
  val ErrorDialogHeader = "Empty login."
  val ErrorDialogContent = "Please, insert username and password."
}

class LoginControllerImpl(private var model: List[ObjectModel],
                          private var mainController: MainControllerImpl,
                          //TODO channel
                          val channel: RabbitMQImpl
                         ) {

  val pool: ExecutorService = Executors.newFixedThreadPool(1)

  def loginPress(username: String, password: String) = {
    println(username)
    println(password)
    //TODO start info dialog without buttons
    //TODO do request with thread and wait future
    val message: Message = new LoginMessageImpl("", "") //TODO
    val task: Future[String] = pool.submit(new RequestHandler(channel, message))
    val response: String = task.get()
    println(response)
    //TODO when future returns
    // OK -> stop dialog and change view
    //ERROR -> change dialog

    pool.shutdown() //TODO verify
  }

  def onResponse() = {
    mainController.changeView("Home")
  }

  def emptyLogin() = {
    new Alert(AlertType.Error) {
      initOwner(mainController._view._stage)
      title = LoginControllerImpl.ErrorDialogTitle
      headerText = LoginControllerImpl.ErrorDialogHeader
      contentText = LoginControllerImpl.ErrorDialogContent
    }.showAndWait()
  }

}
