import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Live {
    Cell[][] field = new Cell[50][100];
    Cell[][] field2 = new Cell[50][100];

    public void randomFill() {
        for (int i = 0; i < field.length; ++i){
            for (int j = 0; j < field[0].length; ++j){
                field[i][j] = Cell.createCell();
                field2[i][j] = Cell.createCell();
                field[i][j].random();
            }
        }
        for (int i = 0; i < field.length; ++i) {
            field[i][0].setZero();
            field[i][field[0].length - 1].setZero();
        }
        for (int i = 0; i < field[0].length; ++i) {
            field[0][i].setZero();
            field[field.length - 1][i].setZero();
        }
    }
    public void gameIteration() {
        for (int i = 1; i < field.length - 1; ++i){
            for (int j = 1; j < field[0].length - 1; ++j){
                field2[i][j] = field[i][j].cross(getNeights(i, j));
            }
        }
        Cell[][] field3 = field;
        field = field2;
        field2 = field3;
        draw();
    }
    private LinkedList<Cell>  getNeights(int x, int y) {
        LinkedList<Cell> nei = new LinkedList<Cell>();
        for (int i = -1; i < 2; ++i){
            for (int j = -1; j < 2; ++j){
                if((i != 0 || j != 0 ) && field[x+i][y+j].alive)
                   nei.add(field[x+i][y+j]);
            }
        }
        return nei;
    }
    public void draw() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 1; i < field.length - 1; ++i){
            for (int j = 1; j < field[0].length - 1; ++j){
                field[i][j].draw();
            }
            System.out.println();
        }
        System.out.println();
    }
    public void gameLoop() {
        while (true){
            gameIteration();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
