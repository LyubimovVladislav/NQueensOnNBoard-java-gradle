package task.chess;

public class ChessPiece{
    int[][] underAttackByPiece;
    int dim,x,y;
    public ChessPiece(int dim, int x, int y){
        this.dim = dim;
        this.x = x;
        this.y = y;
        createBoard();
        markUnderAttackBoard();
    }
    
    private void markUnderAttackBoard(){
        for (int i = 0; i<dim; i++){
            underAttackByPiece[x][i] = 1;
            underAttackByPiece[i][y] = 1;
        }
        for (int i = 0; i<dim;i++){
            if (x+i<dim && y+i<dim)
                underAttackByPiece[x+i][y+i]=1;
            if (x+i<dim && y-i>=0)
                underAttackByPiece[x+i][y-i]=1;
            if (x-i>=0 && y+i<dim)
            underAttackByPiece[x-i][y+i]=1;
            if (x-i>=0 && y-i>=0)
            underAttackByPiece[x-i][y-i]=1;
        }
    }
    
    public int[][] getUnderAttackByPiece() {
        return underAttackByPiece;
    }
    
    private void createBoard(){
        underAttackByPiece = new int[dim][dim];
        for (int i=0; i<dim; i++)
            for(int j=0; j<dim;j++ )
                underAttackByPiece[i][j] = 0;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for (int i=0; i<dim; i++){
            for(int j=0; j<dim;j++ ) {
                str.append(underAttackByPiece[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }
    
}
