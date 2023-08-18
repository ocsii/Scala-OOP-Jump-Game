package GameCode


import scalafx.animation.AnimationTimer
import scalafx.scene.input.{KeyCode, KeyEvent}

import scala.util.Random

class Game () {

  // Game variables
  private var isGameStarted: Boolean = false
  var isGameOver: Boolean = false
  var inAboutScreen: Boolean = false
  private var score: Int = 0
  private var highScore: Int = 0
  private var isHighScore: Boolean = false
  var obstacleSpawnTimer: Int = Random.nextInt(180 - 60 + 1) + 60


  // Handle user inputs
  def handleKeyPress(event: KeyEvent): Unit = {

    // Check if player is jumping while game is running (game screen)
    if (event.code == KeyCode.W && !GameScreen.player.isJumping && !isGameOver) {
      GameScreen.player.isJumping = true
      GameScreen.player.verticalVelocity = GameScreen.player.jumpSpeed
      GameScreen.player.playJumpSound()
      GameScreen.pressWBox.visible = false
    }

    // Move left (about screen)
    if (event.code == KeyCode.A) {
      AboutScreen.player.isMovingLeft = true
    }

    // Move right (about screen)
    if (event.code == KeyCode.D) {
      AboutScreen.player.isMovingRight = true
    }

    // Jump (about screen)
    else if (event.code == KeyCode.W) {
      if (!AboutScreen.player.isJumping) {
        AboutScreen.player.isJumping = true
        AboutScreen.player.verticalVelocity = AboutScreen.player.jumpSpeed
        AboutScreen.player.playJumpSound()
      }
    }
  }

  // Display game over and buttons once dead
  def endGame(): Unit = {

    // Reset game variables
    isGameStarted = false

    // Show end game screen
    GameScreen.endGameScene()
  }

  // Function to reset game variables and positions
  def restartGame(): Unit = {

    // Reset game variables
    isGameOver = false
    isGameStarted = true
    score = 0
    isHighScore = false
    GameScreen.scoreText.text = "SCORE: 0"

    // Reset everything on the scene
    GameScreen.resetScene()
  }


  // Section for main game loop > GameScreen
  // Update the main Game Loop (done)
  def updateGame(): Unit = {

    // As long as game is running
    if (!isGameOver) {

      // Animations
      GameScreen.animateBackground()
      GameScreen.animateFloor()
      GameScreen.player.animateWalking()

      // If Player is jumping
      if (GameScreen.player.isJumping) {
        GameScreen.player.stopWalking()
        GameScreen.player.jump()
      }

      // Update obstacle position
      for (obstacle <- GameScreen.obstacles) {

        // Move the obstacle to the left
        obstacle.translateX = obstacle.translateX() - obstacle.obstacleSpeed

        // If an obstacle intersects player / collision
        if (obstacle.getBoundsInParent.intersects(GameScreen.player.getBoundsInParent)) {
          GameScreen.player.playDeathSound()
          isGameOver = true

          // If new high score is set
          if (isHighScore) {
            GameScreen.newHighScore.visible = true
          }
          //println("Collision detected!")
        }

        // If player passed an obstacle update score
        if (obstacle.translateX() < -1050) {
          score += 1
          GameScreen.scoreText.text = s"SCORE: $score"

          // If new high score update high score
          if (score > highScore) {
            isHighScore = true
            highScore = score

            GameScreen.highScoreText.text = s"HIGH SCORE: $highScore"
          }

          // Remove passed obstacles
          GameScreen.obstacles -= obstacle
          GameScreen.getGameScene.content.remove(obstacle)
        }
      }

      // Spawn new obstacles
      obstacleSpawnTimer -= 1
      if (obstacleSpawnTimer <= 0) {
        GameScreen.spawnObstacle()
        obstacleSpawnTimer = Random.nextInt(180 - 60 + 1) + 60
      }
    }
  }

  // Timer for game loop
  private var gameLoopTimer: AnimationTimer = _
  // Main Game loop for GameScreen
  def startGameLoop(): Unit = {
    // restart game to prevent animation timer form stacking and increasing speed
    restartGame()

    if (gameLoopTimer == null) {
      gameLoopTimer = AnimationTimer { _ =>
        if (!isGameOver) {
          updateGame()
        }
        else {
          endGame()
        }
      }
      gameLoopTimer.start()
    }
  }

  // Stop the game loop if you click back to main menu button
  def stopGameLoop(): Unit = {
    // Stop the game loop if it's running
    if (gameLoopTimer != null) {
      gameLoopTimer.stop()
      gameLoopTimer = null
    }

    GameScreen.player.stopWalking()
  }


  // Section for about page loop > AboutScreen
  // Update the about animations
  private def updateAbout(): Unit = {

    // If in the about screen and not in game
    if (inAboutScreen && !isGameStarted) {

      // Move player to the left
      if (AboutScreen.player.isMovingLeft) {
        AboutScreen.moveBackground()
        AboutScreen.player.moveLeft()
        AboutScreen.player.animateWalking()
      }

      // Move player to the right
      if (AboutScreen.player.isMovingRight) {
        AboutScreen.moveBackground()
        AboutScreen.player.moveRight()
        AboutScreen.player.animateWalking()
      }

      // Jump
      if (AboutScreen.player.isJumping) {
        AboutScreen.player.stopWalking()
        AboutScreen.player.jump()
      }
    }
  }

  // Timer for about screen Animtaions
  private var aboutScreenTimer: AnimationTimer = _
  // Loop for updating animations
  def startAboutScreenLoop(): Unit = {

    inAboutScreen = true

    if (aboutScreenTimer == null) {
      aboutScreenTimer = AnimationTimer { _ =>

        // If game has not started and in about screen
        if (!isGameStarted && inAboutScreen) {
          updateAbout()
        }
        else {
          stopAboutScreen()
        }
      }
      aboutScreenTimer.start()
    }
  }

  // Stop the about screen timer
  private def stopAboutScreen(): Unit = {
    inAboutScreen = false


    // Stop the animations leave about screen
    if (aboutScreenTimer != null) {
      aboutScreenTimer.stop()
      aboutScreenTimer = null
    }
  }


  // Section for start screen loop > StartScreen
  // Animate the background and player in the StartScreen
  private def updateStartScreen(): Unit = {

    val backgroundScrollSpeed: Double = 1.0 // Adjust the scrolling speed as needed
    var backgroundX1Position: Double = 0.0
    var backgroundX2Position: Double = 0.0

    backgroundX1Position -= backgroundScrollSpeed
    backgroundX2Position -= backgroundScrollSpeed

    backgroundX1Position = (StartScreen.backgroundImageView1.x() - backgroundScrollSpeed)
    backgroundX2Position = (StartScreen.backgroundImageView2.x() - backgroundScrollSpeed)

    // If the position of the bakground goes past 0 put it to the right of the other one
    if (backgroundX1Position < -1000) {
      backgroundX1Position = 1000
    }
    if (backgroundX2Position < -1000) {
      backgroundX2Position = 1000
    }

    StartScreen.backgroundImageView1.x() = backgroundX1Position
    StartScreen.backgroundImageView2.x() = backgroundX2Position

    // Animate the player in the start screen to walk
    StartScreen.player.animateWalking()
  }

  // Timer for start screen animations
  private var startScreenTimer: AnimationTimer = _
  // Loop for updating animations
  def startStartScreenLoop(): Unit = {

    if (startScreenTimer == null) {
      startScreenTimer = AnimationTimer { _ =>

        // If game has not started
        if (!isGameStarted) {
          updateStartScreen()
        }
        else {
          stopStartScreen()
        }
      }
      startScreenTimer.start()
    }
  }

  // Stop the animations once game starts
  private def stopStartScreen(): Unit = {

    // Reset the screen timer
    if (startScreenTimer != null) {
      startScreenTimer.stop()
      startScreenTimer = null
    }
    StartScreen.player.stopWalking()
  }
}
