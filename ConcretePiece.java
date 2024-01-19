import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class ConcretePiece implements Piece{
    protected Player owner;
    protected String type;
    protected int id, distance=0;
    protected List<Position> positionsHistory = new ArrayList<>();
    public ConcretePiece(Player firstPlayer) {
        this.owner = firstPlayer;
    }
    public ConcretePiece(ConcretePiece cp) {
        this.owner = cp.getOwner();
        this.type = cp.getType();
        this.id = cp.getId();
        this.distance = cp.getDistance();
        this.positionsHistory = cp.getPositionsHistory();
    }
    @Override
    public Player getOwner(){
        return this.owner;
    }
    public int getId(){
        return this.id;
    }
    public int getDistance(){
        return this.distance;
    }
    public List<Position> getPositionsHistory(){
        return this.positionsHistory;
    }
    public void setId(int newId){
        this.id = newId;
    }
    public void addDistance(int walkedDistance){
        this.distance += walkedDistance;
    }
    public void addPosition(Position p){
        this.positionsHistory.add(p);
    }
    @Override
    public String getType(){
        return this.type;
    }
    public void copy(ConcretePiece cp){
        this.distance = cp.getDistance();
        this.owner = cp.getOwner();
        this.type = cp.getType();
        this.id = cp.getId();
        this.positionsHistory = cp.getPositionsHistory();
    }
}
class movesComparator implements Comparator<ConcretePiece> {
    private boolean playerOneWon;
    public movesComparator(boolean playerOneWon){
        this.playerOneWon = playerOneWon;
    }

    public int compare(ConcretePiece piece1, ConcretePiece piece2){
        if(piece1.getOwner().isPlayerOne() == piece2.getOwner().isPlayerOne()) {
            if (piece2.getPositionsHistory().size() - piece1.getPositionsHistory().size() == 0) {
                return piece1.getId() - piece2.getId();
            }
            return piece1.getPositionsHistory().size() - piece2.getPositionsHistory().size();
        }
        if(this.playerOneWon == piece1.getOwner().isPlayerOne()){return -1;}
        return 1;
    }
}
class distanceComparator implements Comparator<ConcretePiece> {
    private boolean playerOneWon;
    public distanceComparator(boolean playerOneWon){
        this.playerOneWon = playerOneWon;
    }

    public int compare(ConcretePiece piece1, ConcretePiece piece2){
        if(piece2.getDistance() - piece1.getDistance() == 0){
            if (piece1.getId() == piece2.getId()) {
                if(this.playerOneWon == piece1.getOwner().isPlayerOne()){return -1;}
                return 1;
            }
            return piece1.getId() - piece2.getId();
        }
        return piece2.getDistance() - piece1.getDistance();
    }
}
