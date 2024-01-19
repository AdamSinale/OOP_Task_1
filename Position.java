import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Position {
    final int x;
    final int y;
    private int piecesStepped = 0;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getPiecesStepped(){
        return this.piecesStepped;
    }
    public void addPieceStepped(){
        this.piecesStepped++;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }

}
class steppedComparator implements Comparator<Position> {
    public int compare(Position position1, Position position2){
        if(position1.getPiecesStepped() - position2.getPiecesStepped() == 0){
            if (position1.getX() - position2.getX() == 0){
                return position1.getY() - position2.getY();
            }
            return position1.getX() - position2.getX();
        }
        return position2.getPiecesStepped() - position1.getPiecesStepped();
    }
}
