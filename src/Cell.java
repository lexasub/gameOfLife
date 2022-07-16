import java.util.LinkedList;

public class Cell {
    boolean alive;
    Genome genome = Genome.createGenome();
    private Cell() {

    }
    public Cell(Genome _genome){
        alive = true;
        genome = _genome;
    }
    public static Cell createCell() {
        return new Cell();
    }
    public static Cell createCell(Genome _genome) {
        return new Cell(_genome);
    }

    public void random() {
        alive = Math.random() > 0.4;
        genome.random();
    }

    public void setZero() {
        alive = false;
    }

    public void draw() {
        if(alive) genome.draw();
        System.out.print(" ");
    }

    public Cell cross(LinkedList<Cell> neights) {
        if (alive) {
            if (neights.size() > 1 && neights.size() < 5) {
                return this;
            }
            else return createCell();
        }
        else {
            if (neights.size() == 3) {
                Cell newCell = Cell.createCell(genome.cross(neights.stream().map(i -> i.genome)));
                return newCell;
            }
            return this;
        }
    }
}
