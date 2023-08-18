package GameCode

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._


object Main extends JFXApp {

  // Game object
  private val game : Game = new Game

  // Set the initial scene
  var currentScene : Scene = StartScreen.getStartScene
  game.startStartScreenLoop()

  // Switch scenes
  def switchScene (newScene: Scene) : Unit = {
    stage.scene = new Scene
    currentScene = newScene

    if (newScene == StartScreen.getStartScene) {
      game.stopGameLoop()
    }
    else if (newScene == GameScreen.getGameScene) {
      game.startGameLoop()
    }
    else if (newScene == AboutScreen.getAboutScene) {
      game.startAboutScreenLoop()
    }

  }

  def start(): Unit = {

    // Main stage
    stage = new PrimaryStage {

      // Initialize the stage
      title = "2D Scroller - OOP Final Assignment - Christian Cham"
      width = 1000
      height = 400
      resizable = false

      // Start screen play button
      StartScreen.playButton.onAction = () => {
        switchScene(GameScreen.getGameScene)
        scene = currentScene

        currentScene.onKeyPressed = game.handleKeyPress
      }

      // Start screen about button
      StartScreen.aboutButton.onAction = () => {
        switchScene(AboutScreen.getAboutScene)
        scene = currentScene

        currentScene.onKeyPressed = game.handleKeyPress
      }

      // About screen back button
      AboutScreen.backButton.onAction = () => {
        switchScene(StartScreen.getStartScene)
        scene = currentScene
      }

      // Game screen back button
      GameScreen.backButton.onAction = () => {
        switchScene(StartScreen.getStartScene)
        scene = currentScene
      }

      // Game screen restart button
      GameScreen.restartButton.onAction = () => {
        game.restartGame()
      }


      // Set the scene
      scene = currentScene
      currentScene.onKeyPressed = game.handleKeyPress
    }
    stage.show()
  }

  start()
}