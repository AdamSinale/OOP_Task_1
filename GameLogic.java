import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    final int boardSize = 11;
    private ConcretePiece[][] board = new ConcretePiece[boardSize][boardSize];
    private Position[][] boardPositions = new Position[boardSize][boardSize];
    private boolean secondPlayerTurn = true;
    private Stack<Move> moves;
    private List<ConcretePiece> allPieces = new ArrayList<>();
    final ConcretePlayer firstPlayer = new ConcretePlayer(true);
    final ConcretePlayer secondPlayer = new ConcretePlayer(false);

    // if possible to move from a to b return true, else false
    public GameLogic(){
        reset();
    }
    @Override
    public boolean move(Position a, Position b) {
        int ax = a.getX(), ay = a.getY(), bx = b.getX(), by = b.getY();
        //If a and b are the same spot- impossible
        if(ax == bx && ay == by){
            return false;
        }
        //If a and b arent horizontal/vertical- impossible
        if(ax != bx && ay != by){
            return false;
        }
        //If no piece of the turn's player is selected- impossible
        if (board[ax][ay] == null || (board[ax][ay].getOwner().isPlayerOne() == secondPlayerTurn)){
            return false;
        }
        //If a pawn is going to the corners- impossible
        if(board[ax][ay] instanceof Pawn){
            if(bx==0 && (by==0 || by==10)){ return false; }
            if(bx==10 && (by==0 || by==10)){ return false; }
        }
        //If there is a piece in the way
        int distanceMoved; //distance walked
        if (ay == by){
            for(int i=Math.min(ax+1,bx); i<=Math.max(ax-1,bx);i++){
                if(board[i][ay] != null){
                    return false;
                }
            }
            distanceMoved = Math.abs(ax-bx); //set distance walked
        }
        else{
            for(int i=Math.min(ay+1,by); i<=Math.max(ay-1,by);i++){
                if(board[ax][i] != null){
                    return false;
                }
            }
            distanceMoved = Math.abs(ay-by); //set distance walked
        }

        //save the last stats of pieces that will be affected
        ConcretePiece removedLeft=null, removedRight=null, removedTop=null, removedBottom=null, mainPiece;
        if(board[ax][ay] instanceof King){mainPiece = new King((King)board[ax][ay]); }
        else{mainPiece = new Pawn((Pawn)board[ax][ay]); }

        //move piece from a to b
        board[bx][by] = board[ax][ay];
        if(boardPositions[bx][by]==null){boardPositions[bx][by] = new Position(bx,by);}
        board[bx][by].addPosition(boardPositions[bx][by]);
        board[ax][ay].addDistance(distanceMoved);
        board[ax][ay] = null;

        //remove when enemy pawn between pawn and corner/edge/other pawn
        if (bx > 0) {
            removedLeft = board[bx -1][by];
            if (board[bx][by] instanceof Pawn && removedLeft instanceof Pawn && removedLeft.getOwner().isPlayerOne() == secondPlayerTurn) {
                if (bx == 1 || (by%10==0 && bx==2) || (board[bx - 2][by] instanceof Pawn && board[bx - 2][by].getOwner().isPlayerOne() != secondPlayerTurn)) {
                    ((Pawn)board[bx][by]).killed();
                    board[bx - 1][by] = null;
                }
            }
        }
        if (bx < 10) {
            removedRight = board[bx + 1][by];
            if (board[bx][by] instanceof Pawn && removedRight instanceof Pawn && removedRight.getOwner().isPlayerOne() == secondPlayerTurn) {
                if (bx == 9 || (by%10==0 && bx==8) || (board[bx + 2][by] instanceof Pawn && board[bx + 2][by].getOwner().isPlayerOne() != secondPlayerTurn)) {
                    ((Pawn)board[bx][by]).killed();
                    board[bx + 1][by] = null;
                }
            }
        }
        if (by > 0) {
            removedTop = board[bx][by - 1];
            if (board[bx][by] instanceof Pawn && removedTop instanceof Pawn && removedTop.getOwner().isPlayerOne() == secondPlayerTurn) {
                if (by == 1 || (by==2 && bx%10==0) || (board[bx][by - 2] instanceof Pawn && board[bx][by - 2].getOwner().isPlayerOne() != secondPlayerTurn)) {
                    ((Pawn)board[bx][by]).killed();
                    board[bx][by - 1] = null;
                }
            }
        }
        if (by < 10) {
            removedBottom = board[bx][by + 1];
            if (board[bx][by] instanceof Pawn && removedBottom instanceof Pawn && removedBottom.getOwner().isPlayerOne() == secondPlayerTurn) {
                if (by == 9 || (by==8 && bx%10==0) || (board[bx][by + 2] instanceof Pawn && board[bx][by + 2].getOwner().isPlayerOne() != secondPlayerTurn)) {
                    ((Pawn)board[bx][by]).killed();
                    board[bx][by + 1] = null;
                }
            }
        }
        // keep track for every move- from where, where to, and who are the neighbors
        moves.push(new Move(a,b,removedLeft,removedRight,removedTop,removedBottom,mainPiece));
        if(isGameFinished()){
            if(!isSecondPlayerTurn()){ firstPlayer.playerWon(); }
            else{ secondPlayer.playerWon(); }
            gameData(isSecondPlayerTurn());
        }
        secondPlayerTurn = !secondPlayerTurn;  //sets the other player's turn
        return true; //if we got here then horizontal/vertical, different positions and free to go!
    }
    @Override
    public Piece getPieceAtPosition(Position position){ return board[position.getX()][position.getY()]; }
    @Override
    public Player getFirstPlayer(){ return firstPlayer; }
    @Override
    public Player getSecondPlayer(){ return secondPlayer; }
    @Override
    public boolean isGameFinished(){
        // If first player won by king getting to the corners
        if(Pawn.getDefendersKills()>21 || board[0][0] != null || board[10][10] != null || board[10][0] != null || board[0][10] != null){
            return true;
        }
        // If second player won by surrounding the king
        int kingY=-1, kingX=-1;
        int movedToY = moves.peek().getTo().getY(), movedToX = moves.peek().getTo().getX();
        if (moves.peek().getPieceLeft() instanceof King){ kingX = movedToX-1; kingY = movedToY; }
        if (moves.peek().getPieceRight() instanceof King){ kingX = movedToX+1; kingY = movedToY; }
        if (moves.peek().getPieceTop() instanceof King){ kingX = movedToX; kingY = movedToY-1; }
        if (moves.peek().getPieceBottom() instanceof King){ kingX = movedToX; kingY = movedToY+1; }
        if(kingY != -1 && kingX != -1){
            if((kingY == 10 || board[kingX][kingY+1] instanceof Pawn && !board[kingX][kingY+1].getOwner().isPlayerOne()) &&
               (kingY == 0 || board[kingX][kingY-1] instanceof Pawn && !board[kingX][kingY-1].getOwner().isPlayerOne()) &&
               (kingX == 10 || board[kingX+1][kingY] instanceof Pawn && !board[kingX+1][kingY].getOwner().isPlayerOne()) &&
               (kingX == 0 || board[kingX-1][kingY] instanceof Pawn && !board[kingX-1][kingY].getOwner().isPlayerOne())){
                return true;
            }
        }
        //If the second player has no pieces left he lost(but the given jar doesnt cover that)

        return false;
    }
    public void gameData(boolean didPlayer2Win) {
        allPieces.sort(new movesComparator(didPlayer2Win));
        String STR_Q1 = "";
        for (ConcretePiece value : allPieces) {
            List<Position> piecePH = new ArrayList<>(value.getPositionsHistory());

            if (piecePH.size() > 1) {
                String add = value.toString() + ": [" + piecePH.removeFirst().toString();
                STR_Q1 += add;
                while (!piecePH.isEmpty()) {
                    add = ", " + piecePH.removeFirst().toString();
                    STR_Q1 += add;
                }
                STR_Q1 += "]\n";
            }
        }

        List<Pawn> allPawns = new ArrayList<>();
        for (ConcretePiece piece : allPieces) {
            if (piece instanceof Pawn) {
                allPawns.add((Pawn)piece);
            }
        }
        allPawns.sort(new killsComparator(didPlayer2Win));
        String STR_Q2 = "";
        for (Pawn allPawn : allPawns) {
            if (allPawn.getKills() > 0) {
                String add = allPawn.toString() + ": " + ((Pawn)allPawn).getKills() + " kills\n";
                STR_Q2 += add;
            }
        }

        String STR_Q3 = "";
        allPieces.sort(new distanceComparator(didPlayer2Win));
        for (ConcretePiece piece : allPieces) {
            if (piece.getDistance() > 0) {
                String add = piece.toString() + ": " + piece.getDistance() + " squares\n";
                STR_Q3 += add;
            }
        }

        List<Position> positionsWalked= new ArrayList<>();
        String positionsSTR_Q4 = "";
        for (ConcretePiece allPiece : allPieces) {
            List<Position> piecePositions = new ArrayList<>();
            for (int j = 0; j < allPiece.getPositionsHistory().size(); j++) {
                if (!piecePositions.contains(allPiece.getPositionsHistory().get(j))) {
                    allPiece.getPositionsHistory().get(j).addPieceStepped();
                    piecePositions.add(allPiece.getPositionsHistory().get(j));
                }
                if (!positionsWalked.contains(allPiece.getPositionsHistory().get(j))) {
                    positionsWalked.add(allPiece.getPositionsHistory().get(j));
                }
            }
        }
        positionsWalked.sort(new steppedComparator());
        for (Position position : positionsWalked) {
            if (position.getPiecesStepped() > 1) {
                String add = position.toString() + position.getPiecesStepped() + " pieces\n";
                positionsSTR_Q4 += add;
            }
        }

        System.out.print(STR_Q1);
        System.out.println("***************************************************************************");
        System.out.print(STR_Q2);
        System.out.println("***************************************************************************");
        System.out.print(STR_Q3);
        System.out.println("***************************************************************************");
        System.out.print(positionsSTR_Q4);
        System.out.println("***************************************************************************");
    }
    @Override
    public boolean isSecondPlayerTurn(){ return secondPlayerTurn; }
    @Override
    public void reset(){
        moves = new Stack<>();
        allPieces = new ArrayList<>();
        board = new ConcretePiece[boardSize][boardSize];
        boardPositions = new Position[boardSize][boardSize];
        secondPlayerTurn = true;
        for(int i=3;i<=7;i++){
            board[5][i] = new Pawn(firstPlayer);
            board[i][5] = new Pawn(firstPlayer);
            board[0][i] = new Pawn(secondPlayer);
            board[10][i] = new Pawn(secondPlayer);
            board[i][0] = new Pawn(secondPlayer);
            board[i][10] = new Pawn(secondPlayer);
        }
        board[5][5] = new King(firstPlayer);
        board[4][4] = new Pawn(firstPlayer);
        board[4][6] = new Pawn(firstPlayer);
        board[6][4] = new Pawn(firstPlayer);
        board[6][6] = new Pawn(firstPlayer);
        board[1][5] = new Pawn(secondPlayer);
        board[9][5] = new Pawn(secondPlayer);
        board[5][1] = new Pawn(secondPlayer);
        board[5][9] = new Pawn(secondPlayer);
        int idPlayerOne=1,idPlayerTwo=1;
        for(int y=0;y<boardSize;y++){
            for(int x=0;x<boardSize;x++){
                if(board[x][y] != null) {
                    if (board[x][y].getOwner().isPlayerOne()){
                        board[x][y].setId(idPlayerOne);
                        idPlayerOne++;
                    } else{
                        board[x][y].setId(idPlayerTwo);
                        idPlayerTwo++;
                    }
                    boardPositions[x][y] = new Position(x,y);
                    board[x][y].addPosition(boardPositions[x][y]);
                    allPieces.add(board[x][y]);
                }
            }
        }
    }
    @Override
    public void undoLastMove(){
        if(!moves.isEmpty()){
            Move removedMove = moves.pop();
            int toY = removedMove.getTo().getY();
            int toX = removedMove.getTo().getX();
            if(toY<10){ board[toX][toY + 1] = removedMove.getPieceBottom(); }
            if(toY>0){ board[toX][toY - 1] = removedMove.getPieceTop(); }
            if(toX<10){ board[toX + 1][toY] = removedMove.getPieceRight(); }
            if(toX>0){ board[toX - 1][toY] = removedMove.getPieceLeft(); }
            if(removedMove.getMainPiece() instanceof Pawn && secondPlayerTurn){
                Pawn.removeTotalkills(((Pawn)board[toX][toY]).getKills() - ((Pawn)removedMove.getMainPiece()).getKills());
            }
            board[toX][toY].copy(removedMove.getMainPiece());
            board[removedMove.getFrom().getX()][removedMove.getFrom().getY()] = board[toX][toY];
            board[toX][toY] = null;
            secondPlayerTurn = !secondPlayerTurn;
        }
    }
    @Override
    public int getBoardSize(){ return boardSize; }
}
