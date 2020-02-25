import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // default constructor 
  Cell(int x, int y, Color color, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left; 
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  // constructor is used to make a cell with no current data
  // so that it can be updated in assign cells 
  Cell() { }

  /*
   * TEMPLATE 
   * -------- 
   * Fields 
   * ... this.x ...                                     -- int
   * ... this.y ...                                     -- int
   * ... this.color ...                                 -- Color
   * ... this.flooded ...                               -- boolean
   * ... this.left ...                                  -- Cell
   * ... this.top ...                                   -- Cell
   * ... this.right ...                                 -- Cell
   * ... this.bottom ...                                -- Cell
   * Methods
   * ... this.drawCell() ...                            -- WorldImage
   * ... this.drawCellBoard(WorldImage) ...             -- WorldImage
   * ... this.randomColor(int) ...                      -- Color
   * Methods on Fields
   * ... this.left.drawCell() ...                       -- WorldImage
   * ... this.left.drawCellBoard(WorldImage) ...        -- WorldImage
   * ... this.left.randomColor(int) ...                 -- Color
   * ... this.top.drawCell() ...                        -- WorldImage
   * ... this.top.drawCellBoard(WorldImage) ...         -- WorldImage
   * ... this.top.randomColor(int) ...                  -- Color
   * ... this.right.drawCell() ...                      -- WorldImage
   * ... this.right.drawCellBoard(WorldImage) ...       -- WorldImage
   * ... this.right.randomColor(int) ...                -- Color
   * ... this.bottom.drawCell() ...                     -- WorldImage
   * ... this.bottom.drawCellBoard(WorldImage) ...      -- WorldImage
   * ... this.bottom.randomColor(int) ...               -- Color
   */

  // draws the cell 
  WorldImage drawCell() {
    return new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE, 
        OutlineMode.SOLID, this.color);
  }

  // draws the cell on the board 
  WorldImage drawCellBoard(WorldImage bg) {
    return new OverlayOffsetImage(this.drawCell(), (FloodItWorld.BOARD_SIZE / 2) - this.x, 
        (FloodItWorld.BOARD_SIZE / 2) - y, bg);
  }

  // random color generator 
  public Color randomColor(int num) {
    if (num == 0) {
      return Color.RED;
    }
    else if (num == 1) {
      return Color.BLUE;
    }
    else if (num == 2) {
      return Color.GREEN;
    }
    else if (num == 3) {
      return Color.GRAY;
    }
    else if (num == 4) {
      return Color.ORANGE;
    }
    else if (num == 5) {
      return Color.PINK;
    }
    else if (num == 6) {
      return Color.CYAN;
    }
    else {
      return Color.MAGENTA;
    }
  }
}

// represents the FloodIt game program
class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;
  Random rand;
  int boardSize;
  int colorNum;
  int numClicks;
  int goalClicks;
  Double timer;

  // Constants 
  static final int BOARD_SIZE = 900;
  static final int CELL_SIZE = 10;
  static final int TILE_SIZE = 20;
  static final int NUMBER_COLORS = 8;
  static final double TICK_RATE = 0.01;

  // default constructor  
  FloodItWorld(ArrayList<Cell> board) {
    this.board = board;
    this.boardSize = BOARD_SIZE;
    this.colorNum = NUMBER_COLORS;
  }

  // constructor is used to play the game 
  FloodItWorld() {
    this.board = new ArrayList<Cell>();
    this.boardSize = 14;
    this.colorNum = 6;
    this.assignCells(this.board, new Random());
    this.touchingCells(this.board);
  }

  // constructor to test randomness 
  FloodItWorld(Random rand) {
    this.rand = rand;
    this.board = new ArrayList<Cell>();
    this.boardSize = CELL_SIZE;
    this.colorNum = NUMBER_COLORS;
    this.assignCells(this.board, rand);
    this.touchingCells(this.board);
  }

  // constructor for testing (takes in a board, boardSize,
  // colorNum, numClicks, goalClicks, timer) 
  FloodItWorld(ArrayList<Cell> board, int boardSize, int colorNum,
      int numClicks, int goalClicks, Double timer) {
    this.board = board;
    this.boardSize = boardSize;
    this.colorNum = colorNum;
    this.numClicks = numClicks;
    this.goalClicks = goalClicks;
    this.timer = timer;
  }

  /*
   * TEMPLATE 
   * -------- 
   * Fields 
   * ... this.board ...                                 -- ArrayList<Cell>
   * ... this.rand ...                                  -- Random
   * ... this.boardSize ...                             -- int
   * ... this.colorNum ...                              -- int
   * ... this.numClicks ...                             -- int
   * ... this.goalClicks ...                            -- int
   * ... this.timer ...                                 -- Double 
   * Methods
   * ... this.makeScene() ...                           -- WorldScene
   * ... this.boardText() ...                           -- WorldImage
   * ... this.scoreText() ...                           -- WorldImage
   * ... this.timer() ...                               -- WorldImage
   * ... this.title() ...                               -- WorldImage
   * ... this.winLoseText() ...                         -- WorldImage
   * ... this.allFlooded() ...                          -- boolean
   * ... this.assignCells(ArrayList<Cell>, Random) ...  -- void
   * ... this.assignCellsX(int) ...                     -- int
   * ... this.assignCellsY(int) ...                     -- int
   * ... this.touchingCells(ArrayList<Cell>) ...        -- void
   * ... this.assignLeft(Cell) ...                      -- Cell
   * ... this.assignTop(Cell) ...                       -- Cell
   * ... this.assignRight(Cell) ...                     -- Cell
   * ... this.assignBottom(Cell) ...                    -- Cell
   * ... this.onKeyEvent(String) ...                    -- void
   * ... this.onMouseClicked(Posn) ...                  -- void
   * ... this.mouseClickColors(Posn) ...                -- void
   * ... this.mouseClickSize(Posn) ...                  -- void
   * ... this.mouseClickNewGame(Posn) ...               -- void
   * ... this.onTick() ...                              -- void
   * ... this.updateFlooded() ...                       -- void 
   * ... this.updateColros() ...                        -- void
   * ... this.updateTimer() ...                         -- void
   * ... this.listOfFlooded() ...                       -- ArrayList<Cell>
   * ... this.sortFloodedList(ArrayList<Cell>) ...      -- void 
   * Methods on Fields: N/A
   */

  // draws the board for the game 
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(BOARD_SIZE, BOARD_SIZE);
    WorldImage bg = new RectangleImage(BOARD_SIZE, BOARD_SIZE, OutlineMode.OUTLINE, Color.WHITE);
    for (Cell c : this.board) {
      bg = c.drawCellBoard(bg);
    }
    ws.placeImageXY(bg, BOARD_SIZE / 2, BOARD_SIZE / 2);
    ws.placeImageXY(this.boardText(), BOARD_SIZE / 2, BOARD_SIZE / 8);
    ws.placeImageXY(this.scoreText(), BOARD_SIZE / 2, (BOARD_SIZE / 8) * 7);
    ws.placeImageXY(this.winLoseText(), BOARD_SIZE / 2, (BOARD_SIZE / 8) * 13 / 2);
    ws.placeImageXY(this.timer(), BOARD_SIZE / 2, BOARD_SIZE / 16);
    ws.placeImageXY(this.title(), BOARD_SIZE / 2, BOARD_SIZE / 48);
    ws.placeImageXY(this.instructText(), BOARD_SIZE / 2, BOARD_SIZE / 24);
    ws.placeImageXY(this.changeColorSizeText(), BOARD_SIZE / 2, BOARD_SIZE / 12);
    return ws;
  }

  // draws the text on the board
  public WorldImage boardText() {
    return new AboveImage(new TextImage("Colors: " + this.colorNum, Color.BLACK),
        new ComputedPixelImage(20, 20),
        new TextImage("Size: " + this.boardSize, Color.BLACK),
        new ComputedPixelImage(20, 20),
        new TextImage("New Game", Color.GREEN));
  }

  // draws the score on the board
  public WorldImage scoreText() {
    return new TextImage("Score: " + this.numClicks + " / " + this.goalClicks, Color.BLACK);
  }

  // draws the timer on the board 
  public WorldImage timer() {
    return new TextImage("Timer: " + (this.timer.intValue()), Color.BLACK);
  } 

  // draws the title on the board 
  public WorldImage title() {
    return new TextImage("FLOOD IT", Color.BLUE);
  }

  // draws the winner/loser text on the board
  public WorldImage winLoseText() {
    if (this.numClicks <= this.goalClicks
        && this.allFlooded()) {
      return new TextImage("You Win", Color.BLACK);
    }
    if ((this.numClicks == this.goalClicks
        && !this.allFlooded()) || this.numClicks > this.goalClicks) {
      return new TextImage("You Lose", Color.BLACK);
    }
    else {
      return new EmptyImage();
    }
  }

  // determines if all cells are flooded
  public boolean allFlooded() {
    int n = 0;
    for (Cell c : this.board) {
      if (!c.flooded) {
        n = n + 1;
      }
    }
    return n == 0;
  }

  // draws the instructions on the board
  public WorldImage instructText() {
    return new TextImage("Click cells. Fill the board with a single color.",
        13, FontStyle.ITALIC, Color.BLACK);
  }

  // draws instructions for changing colors and size on the board
  public WorldImage changeColorSizeText() {
    return new TextImage("Click below to change number of colors and board size.",
        13, FontStyle.ITALIC, Color.BLACK);
  }

  // EFFECT: assigns game pieces to the board 
  public void assignCells(ArrayList<Cell> arr, Random rand) {
    for (int i = 0; i < (this.boardSize * this.boardSize); i++) {
      Cell c1 = new Cell();
      c1.x = this.assignCellsX(i);
      c1.y = this.assignCellsY(i);
      c1.color = c1.randomColor(rand.nextInt(this.colorNum));
      c1.flooded = false;
      arr.add(c1);
    }
    this.numClicks = 0;
    Double val = (1.64 * this.boardSize) + (4.19 * colorNum) - 23.54;
    this.goalClicks = Math.max(val.intValue(), this.boardSize);
    this.timer = 0.0;

  }

  // helper method for assignCells (x-coordinate)
  public int assignCellsX(int num) {
    return ((num % this.boardSize) * TILE_SIZE)
        + ((TILE_SIZE / 2) + (BOARD_SIZE / 2) - ((this.boardSize / 2) * TILE_SIZE));
  }

  // helper method for assignCells (y-coordinate)
  public int assignCellsY(int num) {
    return (((num / this.boardSize) * TILE_SIZE)
        + ((TILE_SIZE / 2)) + (BOARD_SIZE / 2) - ((this.boardSize / 2) * TILE_SIZE));
  }

  // EFFECT: assigns left, right, top, bottom
  public void touchingCells(ArrayList<Cell> arr) {
    for (Cell c1 : arr) {
      c1.left = this.assignLeft(c1);
      c1.top = this.assignTop(c1);
      c1.right = this.assignRight(c1);
      c1.bottom = this.assignBottom(c1);
    }
    arr.get(0).flooded = true;
  }

  // assigns the cell to the left of this cell
  public Cell assignLeft(Cell c) {
    if (this.board.indexOf(c) % this.boardSize == 0) {
      return null;
    }
    else {
      return this.board.get(this.board.indexOf(c) - 1);
    }
  }

  // assigns the cell to the top of this cell
  public Cell assignTop(Cell c) {
    if (this.board.indexOf(c) < this.boardSize) {
      return null;
    }
    else {
      return this.board.get(this.board.indexOf(c) - this.boardSize);
    }
  }

  // assigns the cell to the right of this cell
  public Cell assignRight(Cell c) {
    if (this.board.indexOf(c) % this.boardSize == this.boardSize - 1) {
      return null;
    }
    else {
      return this.board.get(this.board.indexOf(c) + 1);
    }
  }

  // assigns the cell to the bottom of this cell
  public Cell assignBottom(Cell c) {
    if (this.board.indexOf(c) >= ((this.boardSize * this.boardSize) - this.boardSize)) {
      return null;
    }
    else {
      return this.board.get(this.board.indexOf(c) + this.boardSize);
    }
  }

  // EFFECT: creates a new board for the FloodItWorld when the "r" key is pressed
  public void onKeyEvent(String s) {
    if (s.equals("r")) {
      this.board = new ArrayList<Cell>();
      this.assignCells(this.board, new Random());
      this.touchingCells(this.board);
    }
  }

  // EFFECT: updates the FloodItWorld based on the mouse click 
  public void onMouseClicked(Posn p) {
    this.mouseClickColors(p);
    this.mouseClickSize(p);
    this.mouseClickNewGame(p);
    this.mouseClickColorChange(p);
  }

  // EFFECT: updates the FloodItWorld number of colors from appropriate clicks
  public void mouseClickColors(Posn p) {
    if (this.colorNum == 8
        && p.x < (BOARD_SIZE / 2) + 30
        && p.x > (BOARD_SIZE / 2) - 30
        && p.y > (BOARD_SIZE / 8) - 20 - 5
        && p.y < (BOARD_SIZE / 8) - 20 + 5) {
      this.colorNum = 3;
    }
    else if (p.x < (BOARD_SIZE / 2) + 30
        && p.x > (BOARD_SIZE / 2) - 30
        && p.y > (BOARD_SIZE / 8) - 20 - 5
        && p.y < (BOARD_SIZE / 8) - 20 + 5) {
      this.colorNum = this.colorNum + 1;
    }
  }

  // EFFECT: updates the FloodItWorld size from appropriate clicks
  public void mouseClickSize(Posn p) {
    if (this.boardSize == 26
        && p.x < (BOARD_SIZE / 2) + 25
        && p.x > (BOARD_SIZE / 2) - 25
        && p.y > (BOARD_SIZE / 8)
        && p.y < (BOARD_SIZE / 8) + 10) {
      this.boardSize = 2;
    }
    else if (p.x < (BOARD_SIZE / 2) + 25
        && p.x > (BOARD_SIZE / 2) - 25
        && p.y > (BOARD_SIZE / 8)
        && p.y < (BOARD_SIZE / 8) + 10) {
      this.boardSize = this.boardSize + 1;
    }
  }

  // EFFECT: updates the game board from a click on New Game
  public void mouseClickNewGame(Posn p) {
    if (p.x < (BOARD_SIZE / 2) + 35
        && p.x > (BOARD_SIZE / 2) - 35
        && p.y > (BOARD_SIZE / 8) + 30
        && p.y < (BOARD_SIZE / 8) + 40) {
      this.board = new ArrayList<Cell>();
      this.assignCells(this.board, new Random());
      this.touchingCells(this.board);
    }
  }

  // EFFECT: updates the upper left cell color based on the mouse click
  public void mouseClickColorChange(Posn p) {
    if (this.listOfFlooded().size() == 0) {
      for (Cell c : this.board) {
        if ((Math.abs(c.x - p.x) < TILE_SIZE / 2)
            && (Math.abs(c.y - p.y) < TILE_SIZE / 2)) {
          if (this.board.get(0).color != c.color) {
            this.numClicks = this.numClicks + 1;
          }
          this.board.get(0).color = c.color;
          this.board.get(0).flooded = true;
        }
      }
    }
  }

  // EFFECT: updates the game based on the on tick 
  public void onTick() {
    this.updateFlooded();
    this.updateColors();
    this.updateTimer();
  }

  // EFFECT: updates the flooded cells based on the color change
  public void updateFlooded() {
    for (Cell c : this.board) {
      if (!c.flooded) {
        if ((c.left != null && c.left.flooded && c.left.color == c.color)
            || (c.right != null && c.right.flooded && c.right.color == c.color)
            || (c.top != null && c.top.flooded && c.top.color == c.color)
            || (c.bottom != null && c.bottom.flooded && c.bottom.color == c.color)) {
          c.flooded = true;
        }
      }
    }
  }

  //EFFECT: updates the colors based on the selected color
  public void updateColors() {
    ArrayList<Cell> arr = this.listOfFlooded();
    this.sortFloodedList(arr);
    if (arr.size() > 0) {
      arr.get(0).color = this.board.get(0).color;
    }
  }

  //EFFECT: updates the timer based on the tick rate 
  public void updateTimer() {
    this.timer = this.timer + TICK_RATE;
  } 

  // produces a list of the flooded cells 
  public ArrayList<Cell> listOfFlooded() {
    ArrayList<Cell> floodedlist = new ArrayList<Cell>();
    for (Cell c : this.board) {
      if (c.flooded && c.color != board.get(0).color) {
        floodedlist.add(c);
      }
    }
    return floodedlist;
  }

  // EFFECT: sorts the list of the flooded cells 
  public void sortFloodedList(ArrayList<Cell> c) {
    c.sort(new DistFrom0());
  }
}

// a class to compare cells distance from the origin 
class DistFrom0 implements Comparator<Cell> {

  // comparator to determine cells' distance from the origin in relation to each other
  public int compare(Cell c1, Cell c2) {
    return ((c1.x * c1.x) + (c1.y * c1.y))
        - ((c2.x * c2.x) + (c2.y * c2.y));
  }
}

// test and examples for the FloodIt Game 
class FloodItExamples {

  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;
  Cell c5;
  Cell c6;
  Cell c7;
  Cell c8;

  ArrayList<Cell> l1;
  ArrayList<Cell> l2;
  ArrayList<Cell> l3;

  WorldImage bg;

  WorldScene ws1;

  DistFrom0 dist;

  FloodItWorld fiw1;
  FloodItWorld fiw2;
  FloodItWorld fiw3;
  FloodItWorld fiw4;
  FloodItWorld fiw5;

  void initData() {
    this.c1 = new Cell(20, 20, Color.RED, true, null, null, null, null);
    this.c2 = new Cell(120, 120, Color.BLUE, false, null, null, null, null);
    this.c3 = new Cell(200, 200, Color.ORANGE, false, null, null, null, null);
    this.c4 = new Cell(40, 40, Color.PINK, true, null, null, null, null);

    this.c5 = new Cell(20, 20, Color.RED, true, null, null, null, null);
    this.c6 = new Cell(120, 120, Color.BLUE, true, null, null, null, null);
    this.c7 = new Cell(200, 200, Color.ORANGE, true, null, null, null, null);
    this.c8 = new Cell(40, 40, Color.PINK, true, null, null, null, null);

    this.l1 = new ArrayList<Cell>(Arrays.asList(this.c1, this.c2, this.c3, this.c4));
    this.l2 = new ArrayList<Cell>(Arrays.asList(this.c5, this.c6, this.c7, this.c8));
    this.l3 = new ArrayList<Cell>(Arrays.asList(this.c2, this.c4, this.c3, this.c1));

    this.bg = new RectangleImage(FloodItWorld.BOARD_SIZE, FloodItWorld.BOARD_SIZE,
        OutlineMode.OUTLINE, Color.WHITE);

    this.ws1 = new WorldScene(FloodItWorld.BOARD_SIZE, FloodItWorld.BOARD_SIZE);

    this.dist = new DistFrom0();

    // takes in: board
    this.fiw1 = new FloodItWorld(this.l1);
    // takes in: random
    this.fiw2 = new FloodItWorld(new Random(1));
    // takes in: board, boardSize, colorNum, numClicks, goalClicks, timer
    this.fiw3 = new FloodItWorld(this.l1, 2, 4, 4, 5, 0.1);
    // takes in: board, boardSize, colorNum, numClicks, goalClicks, timer
    this.fiw4 = new FloodItWorld(this.l2, 2, 4, 5, 5, 0.1);
    // takes in: board, boardSize, colorNum, numClicks, goalClicks, time
    this.fiw5 = new FloodItWorld(this.l1, 2, 8, 5, 5, 0.2);
  }

  // test the method drawCell
  void testDrawCell(Tester t) {
    this.initData();
    t.checkExpect(c1.drawCell(), new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
        OutlineMode.SOLID, Color.RED));
    t.checkExpect(c2.drawCell(), new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
        OutlineMode.SOLID, Color.BLUE));
    t.checkExpect(c3.drawCell(), new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
        OutlineMode.SOLID, Color.ORANGE));
    t.checkExpect(c4.drawCell(), new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
        OutlineMode.SOLID, Color.PINK));
  }

  // test the method drawCellBoard
  void testDrawCellBoard(Tester t) {
    this.initData();
    t.checkExpect(c1.drawCellBoard(bg), new OverlayOffsetImage(
        new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
            OutlineMode.SOLID, Color.RED), (FloodItWorld.BOARD_SIZE / 2) - 20, 
        (FloodItWorld.BOARD_SIZE / 2) - 20, bg));
    t.checkExpect(c2.drawCellBoard(bg), new OverlayOffsetImage(
        new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
            OutlineMode.SOLID, Color.BLUE), (FloodItWorld.BOARD_SIZE / 2) - 120, 
        (FloodItWorld.BOARD_SIZE / 2) - 120, bg));
    t.checkExpect(c3.drawCellBoard(bg), new OverlayOffsetImage(
        new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
            OutlineMode.SOLID, Color.ORANGE), (FloodItWorld.BOARD_SIZE / 2) - 200, 
        (FloodItWorld.BOARD_SIZE / 2) - 200, bg));
    t.checkExpect(c4.drawCellBoard(bg), new OverlayOffsetImage(
        new RectangleImage(FloodItWorld.TILE_SIZE, FloodItWorld.TILE_SIZE,
            OutlineMode.SOLID, Color.PINK), (FloodItWorld.BOARD_SIZE / 2) - 40, 
        (FloodItWorld.BOARD_SIZE / 2) - 40, bg));
  }

  // test the method randomColor
  void testRandomColor(Tester t) {
    this.initData();
    t.checkExpect(c1.randomColor(0), Color.RED);
    t.checkExpect(c2.randomColor(0), Color.RED);
    t.checkExpect(c1.randomColor(1), Color.BLUE);
    t.checkExpect(c4.randomColor(2), Color.GREEN);
    t.checkExpect(c4.randomColor(3), Color.GRAY);
    t.checkExpect(c4.randomColor(4), Color.ORANGE);
    t.checkExpect(c4.randomColor(5), Color.PINK);
    t.checkExpect(c4.randomColor(6), Color.CYAN);
    t.checkExpect(c4.randomColor(7), Color.MAGENTA);
  }

  // test the method makeScene 
  void testMakeScene(Tester t) { 
    this.initData();
    ws1.placeImageXY(        
        new OverlayOffsetImage(this.c4.drawCell(),
            (FloodItWorld.BOARD_SIZE / 2) - this.c4.x, 
            (FloodItWorld.BOARD_SIZE / 2) - this.c4.y, 
            new OverlayOffsetImage(this.c3.drawCell(),
                (FloodItWorld.BOARD_SIZE / 2) - this.c3.x, 
                (FloodItWorld.BOARD_SIZE / 2) - this.c3.y,                
                new OverlayOffsetImage(this.c2.drawCell(),
                    (FloodItWorld.BOARD_SIZE / 2) - this.c2.x, 
                    (FloodItWorld.BOARD_SIZE / 2) - this.c2.y, 
                    new OverlayOffsetImage(this.c1.drawCell(),
                        (FloodItWorld.BOARD_SIZE / 2) - this.c1.x, 
                        (FloodItWorld.BOARD_SIZE / 2) - this.c1.y, this.bg)))),
        FloodItWorld.BOARD_SIZE / 2, FloodItWorld.BOARD_SIZE / 2);  
    ws1.placeImageXY(fiw3.boardText(), FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE / 8);
    ws1.placeImageXY(fiw3.scoreText(), FloodItWorld.BOARD_SIZE / 2,
        (FloodItWorld.BOARD_SIZE / 8) * 7);
    ws1.placeImageXY(fiw3.winLoseText(), FloodItWorld.BOARD_SIZE / 2,
        (FloodItWorld.BOARD_SIZE / 8) * 13 / 2);
    ws1.placeImageXY(fiw3.timer(), FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE / 16);
    ws1.placeImageXY(fiw3.title(), FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE / 48);
    ws1.placeImageXY(fiw3.instructText(), FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE / 24);
    ws1.placeImageXY(fiw3.changeColorSizeText(), FloodItWorld.BOARD_SIZE / 2,
        FloodItWorld.BOARD_SIZE / 12);
    t.checkExpect(fiw3.makeScene(), ws1);
  }

  // test the method boardText
  void testBoardText(Tester t) {
    this.initData();
    t.checkExpect(fiw1.boardText(), 
        new AboveImage(new TextImage("Colors: " + fiw1.colorNum, Color.BLACK),
            new ComputedPixelImage(20, 20),
            new TextImage("Size: " + fiw1.boardSize, Color.BLACK),
            new ComputedPixelImage(20,20), 
            new TextImage("New Game", Color.GREEN)));
    t.checkExpect(fiw2.boardText(),
        new AboveImage(new TextImage("Colors: " + fiw2.colorNum, Color.BLACK),
            new ComputedPixelImage(20, 20),
            new TextImage("Size: " + fiw2.boardSize, Color.BLACK),
            new ComputedPixelImage(20,20), 
            new TextImage("New Game", Color.GREEN)));
    t.checkExpect(fiw4.boardText(), 
        new AboveImage(new TextImage("Colors: " + fiw4.colorNum, Color.BLACK),
            new ComputedPixelImage(20, 20),
            new TextImage("Size: " + fiw4.boardSize, Color.BLACK),
            new ComputedPixelImage(20,20), 
            new TextImage("New Game", Color.GREEN)));
  }

  // test the method scoreText
  void testScoreText(Tester t) {
    this.initData();
    t.checkExpect(fiw3.scoreText(), new TextImage("Score: "
        + fiw3.numClicks + " / " + 5, Color.BLACK));
    t.checkExpect(fiw4.scoreText(), new TextImage("Score: "
        + fiw4.numClicks + " / " + 5, Color.BLACK));
    t.checkExpect(fiw5.scoreText(), new TextImage("Score: "
        + fiw5.numClicks + " / " + 5, Color.BLACK));
  }

  // test the method timer
  void testTimer(Tester t) {
    this.initData();
    t.checkExpect(fiw3.timer(), new TextImage("Timer: "
        + 0, Color.BLACK));
    t.checkExpect(fiw4.timer(), new TextImage("Timer: "
        + 0, Color.BLACK));
    t.checkExpect(fiw5.timer(), new TextImage("Timer: "
        + 0, Color.BLACK));
  }

  // test the method title
  void testTitle(Tester t) {
    this.initData();
    t.checkExpect(fiw2.title(), new TextImage("FLOOD IT", Color.BLUE));
    t.checkExpect(fiw4.title(), new TextImage("FLOOD IT", Color.BLUE));
    t.checkExpect(fiw5.title(), new TextImage("FLOOD IT", Color.BLUE));
  }

  // test the method winLoseText
  void testWinLoseText(Tester t) {
    this.initData();
    t.checkExpect(fiw4.winLoseText(), new TextImage("You Win", Color.BLACK));
    t.checkExpect(fiw5.winLoseText(), new TextImage("You Lose", Color.BLACK));
    t.checkExpect(fiw3.winLoseText(), new EmptyImage());
  }

  // test the method allFlooded
  void testAllFlooded(Tester t) {
    this.initData();
    t.checkExpect(fiw1.allFlooded(), false);
    t.checkExpect(fiw3.allFlooded(), false);
    t.checkExpect(fiw4.allFlooded(), true);
    t.checkExpect(fiw5.allFlooded(), false);
  }

  // test the method instructText
  void testInstructText(Tester t) {
    this.initData();
    t.checkExpect(fiw2.instructText(),
        new TextImage("Click cells. Fill the board with a single color.",
            13, FontStyle.ITALIC, Color.BLACK));
    t.checkExpect(fiw3.instructText(),
        new TextImage("Click cells. Fill the board with a single color.",
            13, FontStyle.ITALIC, Color.BLACK));
    t.checkExpect(fiw4.instructText(),
        new TextImage("Click cells. Fill the board with a single color.",
            13, FontStyle.ITALIC, Color.BLACK));
  }

  // test the method changeColorSizeText
  void testChangeColorSizeText(Tester t) {
    this.initData();
    t.checkExpect(fiw2.changeColorSizeText(),
        new TextImage("Click below to change number of colors and board size.",
            13, FontStyle.ITALIC, Color.BLACK));
    t.checkExpect(fiw1.changeColorSizeText(),
        new TextImage("Click below to change number of colors and board size.",
            13, FontStyle.ITALIC, Color.BLACK));
    t.checkExpect(fiw5.changeColorSizeText(),
        new TextImage("Click below to change number of colors and board size.",
            13, FontStyle.ITALIC, Color.BLACK));
  }

  // test the method assignCells 
  void testAssignCells(Tester t) {
    this.initData();
    t.checkExpect(this.fiw2.board.size(), 100);
    fiw2.board = new ArrayList<Cell>();
    t.checkExpect(this.fiw2.board.size(), 0);
    fiw2.assignCells(fiw2.board, fiw2.rand);
    t.checkExpect(this.fiw2.board.size(), 100);  
    t.checkExpect(fiw2.board.get(6).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(72).color, Color.ORANGE);
    t.checkExpect(fiw2.board.get(6).x, 480);
    t.checkExpect(fiw2.board.get(72).x, 400);
    t.checkExpect(fiw2.board.get(6).y, 360);
    t.checkExpect(fiw2.board.get(72).y, 500); 
    t.checkExpect(fiw2.board.get(6).flooded, false);
    t.checkExpect(fiw2.board.get(72).flooded, false);  
    t.checkExpect(fiw2.numClicks, 0);
    t.checkExpect(fiw2.goalClicks, 26);
    t.checkExpect(fiw2.timer, 0.0);
  }

  // test the method assignCellsX
  void testAssignCellsX(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignCellsX(0), 440);
    t.checkExpect(fiw3.assignCellsX(2), 440);
    t.checkExpect(fiw4.assignCellsX(5), 460);
  }

  // test the method assignCellsY
  void testAssignCellsY(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignCellsY(0), 440);
    t.checkExpect(fiw3.assignCellsY(5), 480);
    t.checkExpect(fiw3.assignCellsY(15), 580);
  }

  // test the method touchingCells 
  void testTouchingCells(Tester t) {
    this.initData();
    t.checkExpect(this.fiw3, this.fiw3);

    // testing initial conditions 
    t.checkExpect(this.fiw3.board.get(0).left, null);
    t.checkExpect(this.fiw3.board.get(0).top, null);
    t.checkExpect(this.fiw3.board.get(0).right, null);
    t.checkExpect(this.fiw3.board.get(0).bottom, null);

    t.checkExpect(this.fiw3.board.get(1).left, null);
    t.checkExpect(this.fiw3.board.get(1).top, null);
    t.checkExpect(this.fiw3.board.get(1).right, null);
    t.checkExpect(this.fiw3.board.get(1).bottom, null);

    t.checkExpect(this.fiw3.board.get(2).left, null);
    t.checkExpect(this.fiw3.board.get(2).top, null);
    t.checkExpect(this.fiw3.board.get(2).right, null);
    t.checkExpect(this.fiw3.board.get(2).bottom, null);

    t.checkExpect(this.fiw3.board.get(3).left, null);
    t.checkExpect(this.fiw3.board.get(3).top, null);
    t.checkExpect(this.fiw3.board.get(3).right, null);
    t.checkExpect(this.fiw3.board.get(3).bottom, null);

    // calling the function (updates the cells adjacent cells)  
    fiw3.touchingCells(this.l1);

    // testing the updated cells adjacent cells 
    t.checkExpect(this.fiw3.board.get(0).left, null);
    t.checkExpect(this.fiw3.board.get(0).top, null);
    t.checkExpect(this.fiw3.board.get(0).right, this.c2);
    t.checkExpect(this.fiw3.board.get(0).bottom, this.c3);

    t.checkExpect(this.fiw3.board.get(1).left, this.c1);
    t.checkExpect(this.fiw3.board.get(1).top, null);
    t.checkExpect(this.fiw3.board.get(1).right, null);
    t.checkExpect(this.fiw3.board.get(1).bottom, this.c4);

    t.checkExpect(this.fiw3.board.get(2).left, null);
    t.checkExpect(this.fiw3.board.get(2).top, this.c1);
    t.checkExpect(this.fiw3.board.get(2).right, this.c4);
    t.checkExpect(this.fiw3.board.get(2).bottom, null);

    t.checkExpect(this.fiw3.board.get(3).left, this.c3);
    t.checkExpect(this.fiw3.board.get(3).top, this.c2);
    t.checkExpect(this.fiw3.board.get(3).right, null);
    t.checkExpect(this.fiw3.board.get(3).bottom, null);
  }

  // test the method assignLeft 
  void testAssignLeft(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignLeft(c1), null);
    t.checkExpect(fiw3.assignLeft(c2), c1);
    t.checkExpect(fiw3.assignLeft(c3), null);
    t.checkExpect(fiw4.assignLeft(c5), null);
  }

  // test the method assignTop
  void testAssignTop(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignTop(c1), null);
    t.checkExpect(fiw3.assignTop(c2), null);
    t.checkExpect(fiw3.assignTop(c3), c1);
    t.checkExpect(fiw4.assignTop(c7), c5);
  }

  // test the method assignRight
  void testAssignRight(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignRight(c1), c2);
    t.checkExpect(fiw3.assignRight(c2), null);
    t.checkExpect(fiw3.assignRight(c3), c4);
    t.checkExpect(fiw3.assignRight(c4), null);
  }

  // test the method assignBottom
  void testAssignBottom(Tester t) {
    this.initData();
    t.checkExpect(fiw3.assignBottom(c1), c3);
    t.checkExpect(fiw3.assignBottom(c2), c4);
    t.checkExpect(fiw3.assignBottom(c3), null);
    t.checkExpect(fiw3.assignBottom(c4), null);
  }

  // test the method onKeyEvent
  void testOnKeyEvent(Tester t) {
    this.initData();
    t.checkExpect(fiw2.board.get(0).equals(fiw2.board.get(0)), true);
    t.checkExpect(fiw2.board.get(17).equals(fiw2.board.get(17)), true);
    t.checkExpect(fiw2.board.get(52).equals(fiw2.board.get(52)), true);
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.boardSize = 6;
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.onKeyEvent("l");
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.onKeyEvent("r");
    t.checkExpect(fiw2.board.size(), 36);  
  }

  // test the method onMouseClicked
  void testOnMouseClicked(Tester t) {
    this.initData();
    t.checkExpect(fiw4.colorNum, 4);
    fiw4.onMouseClicked(new Posn(450, 90));
    t.checkExpect(fiw4.colorNum, 5);

    t.checkExpect(fiw5.colorNum, 8);
    fiw5.onMouseClicked(new Posn(450, 90));
    t.checkExpect(fiw5.colorNum, 3);

    this.initData();
    t.checkExpect(fiw4.boardSize, 2);
    fiw4.onMouseClicked(new Posn(450, 115));
    t.checkExpect(fiw4.boardSize, 3);

    fiw1.boardSize = 26;
    t.checkExpect(fiw1.boardSize, 26);
    fiw1.onMouseClicked(new Posn(450, 115));
    t.checkExpect(fiw1.boardSize, 2);

    this.initData();
    t.checkExpect(fiw2.board.get(0).equals(fiw2.board.get(0)), true);
    t.checkExpect(fiw2.board.get(17).equals(fiw2.board.get(17)), true);
    t.checkExpect(fiw2.board.get(52).equals(fiw2.board.get(52)), true);
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.boardSize = 6;
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.onMouseClicked(new Posn(450, 450));
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.onMouseClicked(new Posn(450, 150));
    t.checkExpect(fiw2.board.size(), 36);

    this.initData();
    t.checkExpect(fiw2.board.get(0).color, Color.PINK);
    t.checkExpect(fiw2.numClicks, 0);
    t.checkExpect(fiw2.board.get(16).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(16).x, 480);
    t.checkExpect(fiw2.board.get(16).y, 380);
    fiw2.onMouseClicked(new Posn(480, 380));
    t.checkExpect(fiw2.board.get(0).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(16).color, Color.MAGENTA);
    t.checkExpect(fiw2.numClicks, 1);
    t.checkExpect(fiw2.board.get(72).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(72).x, 400);
    t.checkExpect(fiw2.board.get(72).y, 500);
    fiw2.onMouseClicked(new Posn(400, 500));
    t.checkExpect(fiw2.board.get(0).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(72).color, Color.MAGENTA);
    t.checkExpect(fiw2.numClicks, 1);
  }

  // test the method mouseClickColors 
  void testMouseClickColors(Tester t) {
    this.initData();
    t.checkExpect(fiw4.colorNum, 4);
    fiw4.mouseClickColors(new Posn(450, 90));
    t.checkExpect(fiw4.colorNum, 5);

    t.checkExpect(fiw5.colorNum, 8);
    fiw5.mouseClickColors(new Posn(450, 90));
    t.checkExpect(fiw5.colorNum, 3);
  }

  // test the method mouseClickSize
  void testMouseClickSize(Tester t) {
    this.initData();
    t.checkExpect(fiw4.boardSize, 2);
    fiw4.mouseClickSize(new Posn(450, 115));
    t.checkExpect(fiw4.boardSize, 3);

    fiw1.boardSize = 26;
    t.checkExpect(fiw1.boardSize, 26);
    fiw1.mouseClickSize(new Posn(450, 115));
    t.checkExpect(fiw1.boardSize, 2);
  }


  // test the method mouseClickNewGame
  void testMouseClickNewGame(Tester t) {
    this.initData();
    t.checkExpect(fiw2.board.get(0).equals(fiw2.board.get(0)), true);
    t.checkExpect(fiw2.board.get(17).equals(fiw2.board.get(17)), true);
    t.checkExpect(fiw2.board.get(52).equals(fiw2.board.get(52)), true);
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.boardSize = 6;
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.mouseClickNewGame(new Posn(450, 450));
    t.checkExpect(fiw2.board.size(), 100);
    fiw2.mouseClickNewGame(new Posn(450, 150));
    t.checkExpect(fiw2.board.size(), 36);
  }


  // test the method mouseClickColorChange
  void testMouseClickColorChange(Tester t) {
    this.initData();
    t.checkExpect(fiw2.board.get(0).color, Color.PINK);
    t.checkExpect(fiw2.numClicks, 0);
    t.checkExpect(fiw2.board.get(16).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(16).x, 480);
    t.checkExpect(fiw2.board.get(16).y, 380);
    fiw2.mouseClickColorChange(new Posn(480, 380));
    t.checkExpect(fiw2.board.get(0).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(16).color, Color.MAGENTA);
    t.checkExpect(fiw2.numClicks, 1);
    t.checkExpect(fiw2.board.get(72).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(72).x, 400);
    t.checkExpect(fiw2.board.get(72).y, 500);
    fiw2.mouseClickColorChange(new Posn(400, 500));
    t.checkExpect(fiw2.board.get(0).color, Color.MAGENTA);
    t.checkExpect(fiw2.board.get(72).color, Color.MAGENTA);
    t.checkExpect(fiw2.numClicks, 1);
  }

  // test the method onTick
  void testOnTick(Tester t) {
    this.initData();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, false);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(1).x, fiw2.board.get(1).y));
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, false);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.updateFlooded();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    t.checkExpect(fiw2.board.get(10).flooded, true);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(2).x, fiw2.board.get(2).y));
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.updateFlooded();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, true);
    t.checkExpect(fiw2.board.get(3).flooded, true);

    initData();
    t.checkExpect(c4.color, Color.PINK);
    fiw1.updateColors();
    t.checkExpect(c4.color, Color.RED);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(1).x, fiw2.board.get(1).y));
    fiw2.onTick();
    fiw2.onTick();
    fiw2.onMouseClicked(new Posn(fiw2.board.get(2).x, fiw2.board.get(2).y));
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.RED);
    t.checkExpect(fiw2.board.get(10).color, Color.RED);
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(10).color, Color.RED);
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(10).color, Color.GRAY);

    initData();
    t.checkExpect(fiw3.timer, 0.1);
    fiw3.updateTimer();
    t.checkExpect(fiw3.timer,  0.11);
    fiw3.updateTimer();
    t.checkExpect(fiw3.timer, 0.12);
  }

  // test the method updateFlooded
  void testUpdateFlooded(Tester t) {
    this.initData();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, false);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(1).x, fiw2.board.get(1).y));
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, false);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.updateFlooded();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    t.checkExpect(fiw2.board.get(10).flooded, true);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(2).x, fiw2.board.get(2).y));
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, false);
    t.checkExpect(fiw2.board.get(3).flooded, false);
    fiw2.updateFlooded();
    t.checkExpect(fiw2.board.get(0).flooded, true);
    t.checkExpect(fiw2.board.get(1).flooded, true);
    t.checkExpect(fiw2.board.get(2).flooded, true);
    t.checkExpect(fiw2.board.get(3).flooded, true);
  }

  // test the method updateColors
  void testUpdateColors(Tester t) {
    this.initData();
    t.checkExpect(c4.color, Color.PINK);
    fiw1.updateColors();
    t.checkExpect(c4.color, Color.RED);
    fiw2.onMouseClicked(new Posn(fiw2.board.get(1).x, fiw2.board.get(1).y));
    fiw2.onTick();
    fiw2.onTick();
    fiw2.onMouseClicked(new Posn(fiw2.board.get(2).x, fiw2.board.get(2).y));
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.RED);
    t.checkExpect(fiw2.board.get(10).color, Color.RED);
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(10).color, Color.RED);
    fiw2.updateColors();
    t.checkExpect(fiw2.board.get(0).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(1).color, Color.GRAY);
    t.checkExpect(fiw2.board.get(10).color, Color.GRAY);
  }

  // test the method updateTimer
  void testUpdateTimer(Tester t) {
    this.initData();
    t.checkExpect(fiw3.timer, 0.1);
    fiw3.updateTimer();
    t.checkExpect(fiw3.timer,  0.11);
    fiw3.updateTimer();
    t.checkExpect(fiw3.timer, 0.12);
  }

  // test the method listOfFlooded 
  void testListOfFlooded(Tester t) {
    this.initData();
    t.checkExpect(fiw1.listOfFlooded(), new ArrayList<Cell>(Arrays.asList(c4)));
    t.checkExpect(fiw2.listOfFlooded(), new ArrayList<Cell>());
    fiw2.onMouseClicked(new Posn(fiw2.board.get(1).x, fiw2.board.get(1).y));
    fiw2.onTick();
    fiw2.board.get(0).color = Color.GRAY;
    t.checkExpect(fiw2.listOfFlooded(), new ArrayList<Cell>(Arrays.asList(fiw2.board.get(1),
        fiw2.board.get(10))));
    fiw2.onTick();
    t.checkExpect(fiw2.listOfFlooded(), new ArrayList<Cell>(Arrays.asList(fiw2.board.get(10))));
    t.checkExpect(fiw4.listOfFlooded(), new ArrayList<Cell>(Arrays.asList(c6, c7, c8)));
  }

  // test the method sortFloodedList
  void testSortFloodedList(Tester t) {
    this.initData();
    t.checkExpect(l3.get(0), c2);
    t.checkExpect(l3.get(1), c4);
    t.checkExpect(l3.get(2), c3);
    t.checkExpect(l3.get(3), c1);
    fiw1.sortFloodedList(l3);
    t.checkExpect(l3.get(0), c1);
    t.checkExpect(l3.get(1), c4);
    t.checkExpect(l3.get(2), c2);
    t.checkExpect(l3.get(3), c3);
  }

  // test the comparator DistFrom0
  void testDistFrom0(Tester t) {
    this.initData();
    t.checkExpect(dist.compare(c1, c2), -28000);
    t.checkExpect(dist.compare(c2, c1), 28000);
    t.checkExpect(dist.compare(c1, c1), 0);
    t.checkExpect(dist.compare(c4, c1), 2400);
    t.checkExpect(dist.compare(c1, c4), -2400);
  }

  // runs the game 
  void testGame(Tester t) {
    initData();
    FloodItWorld fiw = new FloodItWorld();
    fiw.bigBang(FloodItWorld.BOARD_SIZE, FloodItWorld.BOARD_SIZE, FloodItWorld.TICK_RATE);
  }
}