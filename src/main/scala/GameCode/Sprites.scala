package GameCode

import scalafx.animation.AnimationTimer
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.media.{Media, MediaPlayer}

// Player object
class Player extends ImageView {

  // Frame images
  val playerImage1 = new Image("/Images/player_walk_1.png")
  val playerImage2 = new Image("/Images/player_walk_2.png")
  val playerJumpImage = new Image("/Images/jump.png")

  // Variables
  val playerWidth: Double = 40.0
  val playerHeight: Double = 40.0

  // Hold player variables
  val jumpSpeed: Double = -11.0
  var verticalVelocity: Double = 0.0
  var isJumping: Boolean = false
  var isMovingLeft: Boolean = false
  var isMovingRight: Boolean = false

  // Set initial image
  image = playerImage1

  // Initialize the ImageView
  fitWidth = playerWidth
  fitHeight = playerHeight
  x = 60
  y = 350 - playerHeight
  mouseTransparent = true // Stop mouse event

  // Move the playter to the left
  def moveLeft(): Unit = {

    x() -= 8

    // If reaches left border stop moving
    if (x() <= 0) {
      x = 0
      isMovingLeft = false
    }

    // Change the players facing direction
    scaleX = -1

    isMovingLeft = false
    stopWalking
  }

  // Move player to the right
  def moveRight(): Unit = {

    x() += 8

    // If reached right border stop movement
    if (x() + playerWidth >= 1000) {
      x = 1000 - playerWidth - 10
      isMovingRight = false
    }

    // Change the players facing direction
    scaleX = 1

    isMovingRight = false
    stopWalking()
  }

  // Method to animate jumping
  def jump(): Unit = {

    // Set the jumping image
    this.image = playerJumpImage

    //
    verticalVelocity += 0.3 // Grabivty by constantly adding 0.3
    y() += verticalVelocity // Update the dinosaur's vertical position

    // Check if the dinosaur has landed on the ground (bottom of the screen)
    if (y() >= 350 - playerHeight) {
      y = 350 - playerHeight
      isJumping = false
      verticalVelocity = 0.0
      this.image = playerImage1
    }
  }

  // Animation timer to swap between images to look like walking
  private var walkingAnimationTimer: AnimationTimer = _

  def animateWalking(): Unit = {
    this.image = playerImage1

    if (walkingAnimationTimer == null) {
      walkingAnimationTimer = AnimationTimer { _ =>
        val walkingImage = if (System.currentTimeMillis() % 400 < 200) playerImage1 else playerImage2 // Swap image every
        this.image = walkingImage
      }
      walkingAnimationTimer.start()
    }
  }

  // Stop the walking animation
  def stopWalking(): Unit = {
    if (walkingAnimationTimer != null) {
      walkingAnimationTimer.stop()
      walkingAnimationTimer = null
    }
  }

  // Sound effect on death
  def playDeathSound(): Unit = {

    // Load file
    val deathSoundFile = getClass.getResource("/Audio/willhelm.mp3")
    val deathSound = new Media(deathSoundFile.toURI.toString)
    val deathPlayer = new MediaPlayer(deathSound)

    deathPlayer.volume = 0.1 // Adjust the volume

    deathPlayer.play()
  }

  // Sound effect for jump
  def playJumpSound(): Unit = {

    // Load file
    val jumpSoundFile = getClass.getResource("/Audio/jump.mp3")
    val jumpSound = new Media(jumpSoundFile.toURI.toString)
    val jumpPlayer = new MediaPlayer(jumpSound)

    jumpPlayer.volume = 0.1 // Adjust the volume

    jumpPlayer.play()
  }
}

// Obstacles object
class Obstacle extends ImageView {

  // Obstacle Images
  val obstacleImage1 = new Image("/Images/barrier.png")
  val obstacleImage2 = new Image("/Images/cactus.png")

  // Obstacle variables
  val obstacleSpeed: Double = 3.0
  val obstacleWidth: Double = 30.0
  val obstacleHeight: Double = 40.0

  // Initialize Image View
  fitWidth = obstacleWidth
  fitHeight = obstacleHeight
  x = 1000
  y = 350 - obstacleHeight
  mouseTransparent = true // This ensures that the ImageView does not consume mouse

}



