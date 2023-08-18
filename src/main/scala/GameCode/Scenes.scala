package GameCode

import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.{LightBlue, rgb}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, FontWeight, Text}

import scala.collection.mutable.ListBuffer
import scala.util.Random

// Game scene
object GameScreen {

  // Background images f
  val backgroundImage = new Image("/Images/includefloor.jpg")

  val backgroundImageView1 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 0
    y = 0
  }

  val backgroundImageView2 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 1000
    y = 0
  }

  // Floor images
  val floorImage = new Image("/Images/ground.png")

  val floorImageView1 = new ImageView(floorImage) {
    fitWidth = 1007
    fitHeight = 50
    x = 0
    y = 350
    mouseTransparent = true // Stop mouse event
  }

  val floorImageView2 = new ImageView(floorImage) {
    fitWidth = 1007
    fitHeight = 50
    x = 1003
    y = 350
    mouseTransparent = true // Stop mouse event
  }

  // Display score
  val scoreText: Text = new Text {
    text = "SCORE: 0"
    font = Font.font("Impact", 25)
    x = 10
    y = 30
    //fill = rgb(90, 90, 90)
    fill = rgb(90, 145, 34)
  }

  // Display High Score
  val highScoreText: Text = new Text {
    text = "HIGH SCORE: 0"
    font = Font.font("Impact", 25)
    x = 824
    y = 30
   // fill = rgb(90, 90, 90)
    fill = rgb(90, 145, 34)
    visible = true
  }

  // New high score text
  val newHighScore: Text = new Text {
    text = "NEW HIGH SCORE!"
    font = Font.font("Impact", 25)
    x = 400
    y = 63
    visible = false // set to false unless new high score
    fill = rgb(90, 90, 90)
  }

  // Game over with score
  val gameOverText: Text = new Text {
    text = "GAME OVER"
    font = Font.font("Impact", 150)
    x = 160
    y = 200
    fill = rgb(90, 145, 34)
    visible = false
  }

  // Button to restart
  val restartButton = new Button("Restart") {
    style = "-fx-background-color: #8BC34A; -fx-font-size: 18px; -fx-text-fill: white;"
    minWidth = 234
    minHeight = 40

  }

  // Button to go back
  val backButton = new Button("Back home") {
    style = "-fx-background-color: #8BC34A; -fx-font-size: 18px; -fx-text-fill: white;"
    minWidth = 234
    minHeight = 40
  }

  // Container to hold both buttons
  val buttonContainer: HBox = new HBox {
    spacing = 20
    layoutX = 248
    layoutY = 214
    children = Seq(restartButton , backButton)
    visible = false
  }

  // Create a key box for 'W'
  val wKeyBox: Rectangle = new Rectangle {
    width = 18
    height = 18
    arcWidth = 10
    arcHeight = 10
    fill = Color.Black
  }

  // Create "W" key text
  val wKeyText: Text = new Text("W") {
    font = Font.font("Arial", FontWeight.Bold, 12)
    fill = Color.White
  }

  // Position W on the rectangle
  val wKeyPane: StackPane = new StackPane {
    children = Seq(wKeyBox, wKeyText)
  }

  // Jump text
  val toJumpText: Text = new Text {
    text = "TO JUMP"
    font = Font.font("Arial", FontWeight.Bold, 15)
  }

  // Container for text and W
  val pressWBox : HBox = new HBox {
    children = Seq(wKeyPane, toJumpText)
    spacing = 10
    layoutX = 32
    layoutY = 269
  }

  // Create player object
  val player : Player = new Player

  // List of obstacles
  var obstacles: ListBuffer[Obstacle] = ListBuffer.empty[Obstacle]

  // Create the main game scene
  private val gameScene : Scene = new Scene {
    fill = LightBlue

    content = Seq(backgroundImageView1, backgroundImageView2, player, scoreText, gameOverText, highScoreText, newHighScore, buttonContainer, floorImageView1, floorImageView2, pressWBox)
  }

  // Return game scene
  def getGameScene: Scene = gameScene

  // Spawn obstacles in GameScreen
  def spawnObstacle(): Unit = {

    // Create obstacle and set image
    val obstacle: Obstacle = new Obstacle
    val obstacleImage = if (Random.nextBoolean()) obstacle.obstacleImage1 else obstacle.obstacleImage2
    obstacle.image = obstacleImage

    // Add to obstacle to list of obstacles
    obstacles += obstacle
    getGameScene.content.add(obstacle)
  }

  // Reset all scene content once restart game
  def resetScene(): Unit = {
    scoreText.text = "SCORE: 0"

    // Reset player position
    player.y = 350 - player.playerHeight

    // Clear obstacles from screen
    for (obstacle <- obstacles) {
      getGameScene.content.remove(obstacle)
    }
    obstacles.clear()

    // Hide game over elements
    gameOverText.visible = false
    newHighScore.visible = false
    buttonContainer.visible = false
    pressWBox.visible = true

    // Display gameplay elements
    highScoreText.visible = true

    // Reset score position
    scoreText.x = 10
    scoreText.y = 30

    scoreText.fill = Color.rgb(90, 145, 34)
  }

  // Display once player is dead
  def endGameScene(): Unit = {

    // Display and hide text and buttons
    gameOverText.visible = true
    buttonContainer.visible = true
    highScoreText.visible = false

    // When game ends move score to centre
    scoreText.x = 444
    scoreText.y = 285

    scoreText.fill = Color.Black
  }

  // Animate the background for the GameScreen
  def animateBackground(): Unit = {

    // Scrolling variables
    val backgroundScrollSpeed: Double = 1.0
    var backgroundX1Position: Double = 0.0
    var backgroundX2Position: Double = 0.0

    /**
    backgroundX1Position -= backgroundScrollSpeed
    backgroundX2Position -= backgroundScrollSpeed
    **/

    // Set the scrolled position
    backgroundX1Position = (backgroundImageView1.x() - backgroundScrollSpeed)
    backgroundX2Position = (backgroundImageView2.x() - backgroundScrollSpeed)

    // If the position of the bakground goes past 0 put it to the right of the other one
    if (backgroundX1Position < -1000) {
      backgroundX1Position = 1000
    }
    if (backgroundX2Position < -1000) {
      backgroundX2Position = 1000
    }

    // Shift position of background
    backgroundImageView1.x() = backgroundX1Position
    backgroundImageView2.x() = backgroundX2Position
  }

  // Animate the background for the GameScreen
  def animateFloor(): Unit = {

    // Scrolling variables
    val floorScrollSpeed: Double = 3.0
    var floor1Position: Double = 0.0
    var floor2Position: Double = 0.0

    /**
    floor1Position -= floorScrollSpeed
    floor2Position -= floorScrollSpeed
    **/


    // Set the scrolled position
    floor1Position = (floorImageView1.x() - floorScrollSpeed)
    floor2Position = (floorImageView2.x() - floorScrollSpeed)

    // If floor passes 0 send to right side of 2nd image
    if (floor1Position < -1000) {
      floor1Position = 1000
    }

    if (floor2Position < -1000) {
      floor2Position = 1000
    }

    // Shift position of the floor
    floorImageView1.x() = floor1Position
    floorImageView2.x() = floor2Position
  }
}

// Start scene
object StartScreen {

  // Background images
  val backgroundImage = new Image("/Images/includefloor.jpg")

  val backgroundImageView1 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 0
    y = 0
  }

  val backgroundImageView2 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 1000
    y = 0
  }

  // Main title text
  val titleText: Text = new Text {
    text = "STICKMAN JUMPER"
    font = Font.font("Impact", 100)
    x = 120
    y = 160
    fill = rgb(90, 145, 34)
  }

  // Button to go to GameScreen
  val playButton = new Button ("Play Game") {
    style = "-fx-background-color: #8BC34A; -fx-font-size: 18px; -fx-text-fill: white;"
    minWidth = 250
    minHeight = 40
  }

  // Button to go to about screen
  val aboutButton = new Button ("About Us") {
    style = "-fx-background-color: #8BC34A; -fx-font-size: 18px; -fx-text-fill: white;"
    minWidth = 250
    minHeight = 40
  }

  // Hold both buttons
  val buttonContainer: HBox = new HBox {
    spacing = 65
    layoutX = 207
    layoutY = 200
    children = Seq(playButton, aboutButton)
  }

  // Create player object on start screen
  val player : Player = new Player

  // Create start scene
  private val startScene : Scene = new Scene {
    fill = LightBlue

    content = Seq(backgroundImageView1, backgroundImageView2, player, buttonContainer, titleText)
  }

  // Return start scene
  def getStartScene : Scene = startScene
}

// About us screen
object AboutScreen {

  // Background images
  val backgroundImage = new Image("/Images/includefloor.jpg")
  val backgroundImageView = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = -997
    y = 0
  }
  val backgroundImageView1 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 0
    y = 0
  }
  val backgroundImageView2 = new ImageView(backgroundImage) {
    fitWidth = 1003
    fitHeight = 400
    x = 1000
    y = 0
  }

  // Button to go back to start screen
  val backButton = new Button("Back to main menu") {
    style = "-fx-background-color: #8BC34A; -fx-font-size: 18px; -fx-text-fill: white;"
    layoutX = 20
    layoutY = 20
  }

  // Arrow pointing on player
  val arrowImage = new Image("/Images/downarrow.png")
  val arrowImageView = new ImageView(arrowImage) {
    fitWidth = 30
    fitHeight = 30
    x = 67
    y = 265
  }

  // Keycap looking box for A
  val aKeyBox: Rectangle = new Rectangle {
    width = 20
    height = 20
    arcWidth = 10
    arcHeight = 10
    fill = Color.Black
  }

  // Keycap looking box for D
  val dKeyBox: Rectangle = new Rectangle {
    width = 20
    height = 20
    arcWidth = 10
    arcHeight = 10
    fill = Color.Black
  }

  // A text in box
  val aKeyText: Text = new Text("A") {
    font = Font.font("Arial", FontWeight.Bold, 14)
    fill = Color.White
  }

  // D text in box
  val dKeyText: Text = new Text("D") {
    font = Font.font("Arial", FontWeight.Bold, 14)
    fill = Color.White
  }

  // Position A texts on their boxes
  val aKeyPane: StackPane = new StackPane {
    children = Seq(aKeyBox, aKeyText)
  }

  // Position D texts on their boxes
  val dKeyPane: StackPane = new StackPane {
    children = Seq(dKeyBox, dKeyText)
  }

  // Arrange keycaps next to each other
  val keyBox: HBox = new HBox {
    spacing = 10
    children = Seq(aKeyPane, dKeyPane)
  }

  // Pointer text
  val youText : Text = new Text("THIS IS YOU, USE") {
    font = Font.font("Arial", FontWeight.Bold, 15)
  }

  // Pointer text
  val moveText: Text = new Text("TO MOVE") {
    font = Font.font("Arial", FontWeight.Bold, 15)
  }

  // Pointer box
  val youBox : HBox = new HBox {
    spacing = 10
    layoutX = 60
    layoutY = 240
    children = Seq(youText, keyBox, moveText)
  }

  // About project heading
  val aboutProject: Text = new Text("ABOUT PROJECT") {
    font = Font.font("Impact", FontWeight.Bold, 40)
    fill = Color.Black
  }

  // Assignment title
  val projectTitle : Text = new Text("TITLE      : PRG2104 - OOP FINAL ASSIGNMENT") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Author and student ID
  val authorTitle: Text = new Text("AUTHOR  : CHRISTIAN FINTA CHAM - 20045472") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Version of game
  val versionTitle: Text = new Text("VERSION : 1.0.0") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Everything in ABOUT section
  val aboutBox : VBox = new VBox {
    spacing = 10
    layoutX = 400
    layoutY = 100
    visible = false

    children = Seq(aboutProject, projectTitle, authorTitle, versionTitle)
  }

  // Tutorial heading
  val tutorialTitle: Text = new Text("HOW TO PLAY?") {
    font = Font.font("Impact", FontWeight.Bold, 40)
    fill = Color.Black
  }

  // Tutorial text
  val tutorialText1 : Text = new Text("1) THE PLAYER WILL AUTOMATICALLY RUN") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Tutorial text
  val tutorialText2: Text = new Text("AND YOU WILL FACE OBSTACLES LIKE") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Arrow to obstacle
  val arrowImageViewObstacle = new ImageView(arrowImage) {
    fitWidth = 30
    fitHeight = 30
    x = 67
    y = 265
  }

  // Pane to position the arrow in the middle
  val arrowPane: StackPane = new StackPane {
    children = Seq(arrowImageViewObstacle)
  }

  // Create the obstacle
  val obstacle: Obstacle = new Obstacle {
    x = 965
    y = 310
    image = obstacleImage1
    visible = false
  }

  // First tutorial box
  val tutorialBox: VBox = new VBox {
    spacing = 10
    layoutX = 820
    layoutY = 100
    visible = false

    children = Seq(tutorialTitle, tutorialText1, tutorialText2, arrowPane)
  }

  // Tutorial heading
  val tutorialTitle1: Text = new Text("HOW TO PLAY?") {
    font = Font.font("Impact", FontWeight.Bold, 40)
    fill = Color.Black
  }

  // Tutorial heading
  val tutorialText4: Text = new Text("2) IF YOU TOUCH AN OBSTACLE YOU DIE YOU ") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Tutorial text
  val tutorialText5: Text = new Text("WILL HAVE TO JUMP OVER OBSTACLES WITH") {
    font = Font.font("Arial", FontWeight.Bold, 15)
    fill = rgb(55, 55, 55)
  }

  // Create a key box for 'D'
  val wKeyBox: Rectangle = new Rectangle {
    width = 20
    height = 20
    arcWidth = 10
    arcHeight = 10
    fill = Color.Black
  }

  // Create "D" key text
  val wKeyText: Text = new Text("W") {
    font = Font.font("Arial", FontWeight.Bold, 14)
    fill = Color.White
  }

  // Pane to position W in the middle
  val wKeyPane: StackPane = new StackPane {
    children = Seq(wKeyBox, wKeyText)
  }

  // Second tutorial box
  val tutorialBox2: VBox = new VBox {
    spacing = 10
    layoutX = 1220
    layoutY = 100
    visible = false

    children = Seq(tutorialTitle1, tutorialText4, tutorialText5, wKeyPane)
  }

  // Have fun text
  val haveFunTitle: Text = new Text("THATS IT! HAVE FUN!") {
    font = Font.font("Impact", FontWeight.Bold, 40)
    fill = Color.Black
    x = 1650
    y = 200
  }

  // Left side text
  val nothingText: Text = new Text("NOTHING THIS WAY") {
    font = Font.font("Impact", FontWeight.Bold, 40)
    fill = Color.Black
    x = -400
    y = 200
  }

  // Create player on About Screen
  val player: Player = new Player

  // Create the about scene
  private val aboutScene: Scene = new Scene {
    fill = LightBlue

    content = Seq(backgroundImageView, backgroundImageView1, backgroundImageView2, backButton, obstacle, player, arrowImageView, youBox, aboutBox, tutorialBox, tutorialBox2, haveFunTitle, nothingText)
  }

  // Return about scene
  def getAboutScene: Scene = aboutScene

  // Move everything if player is moving
  def moveBackground(): Unit = {

    if (player.isMovingLeft) {
      // Move background images
      backgroundImageView.x() += 5
      backgroundImageView1.x() += 5
      backgroundImageView2.x() += 5

      // Move all the elements on the screen
      arrowImageView.x() += 14
      youBox.layoutX() += 14
      tutorialBox.layoutX() += 14
      obstacle.x() += 14
      tutorialBox2.layoutX() += 14
      aboutBox.layoutX() += 14
      haveFunTitle.x() += 14
      nothingText.x() += 14

      // If reach end of background stop scrolling
      if (backgroundImageView.x() >= -10) {
        backgroundImageView.x() -= 5
        backgroundImageView1.x() -= 5
        backgroundImageView2.x() -= 5

        player.isMovingLeft = false
        player.stopWalking()
      }
    }

    if (player.isMovingRight) {
      // Move background
      backgroundImageView.x() -= 5
      backgroundImageView1.x() -= 5
      backgroundImageView2.x() -= 5

      // Move all elements on the screen
      arrowImageView.x() -= 14
      youBox.layoutX() -= 14
      tutorialBox.layoutX() -= 14
      obstacle.x() -= 14
      tutorialBox2.layoutX() -= 14
      aboutBox.layoutX() -= 14
      haveFunTitle.x() -= 14
      nothingText.x() -= 14

      // Show the elements on screen once player near
      if (player.x() >= 80) {
        aboutBox.visible = true
      }

      if (player.x() >= 200) {
        tutorialBox.visible = true
        obstacle.visible = true
      }

      if (player.x() >= 400) {
        tutorialBox2.visible = true
      }

      // Stop scrolling background if reach the end
      if (backgroundImageView2.x() <= 10) {
        backgroundImageView.x() += 5
        backgroundImageView1.x() += 5
        backgroundImageView2.x() += 5

        player.isMovingRight = false
        player.stopWalking()
      }
    }
  }

  // Reset all elements (not using for now / prefer it not reset)
  def resetAbout(): Unit = {

    player.stopWalking()

    player.x = 60
    player.y = 350 - player.playerHeight

    // Reset background and text positions
    backgroundImageView.x = -997
    backgroundImageView1.x = 0
    backgroundImageView2.x = 1000
    obstacle.x = 965
    player.x = 60
    arrowImageView.x = 67
    youBox.layoutX = 60
    aboutBox.layoutX = 400
    tutorialBox.layoutX = 820
    tutorialBox2.layoutX = 1220
    haveFunTitle.x = 1650
    nothingText.x = -400




    // Stop all moving of player
    player.isMovingRight = false
    player.isMovingLeft = false

  }
}