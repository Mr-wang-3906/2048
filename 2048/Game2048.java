import java.util.Scanner;

/**
 * @author wjx
 * @version 1.0
 */
public class Game2048 {
    public static void main(String[] args) {
        int[][] game = new int[4][4];
        System.out.println("按v键开始游戏.");
        Scanner ms = new Scanner(System.in);
        Tools myTool = new Tools();
        char start = ms.next().charAt(0);
        if (start == 'v') {
            myTool.initialize(game);
            System.out.println("分别用w/W,a/A,s/S,d/D控制上,左,下,右滑动. 按r/R可撤回上一步");
            System.out.println("注意事项: 请勿不断重复同一操作,否则数字不会刷新");
        }
        while (myTool.gameIsLive(game)) {
            char nextMove = ms.next().charAt(0);
            switch (nextMove) {
                case 119:
                case 87:
                    myTool.up(game);
                    myTool.paintGame(game);
                    break;
                case 115:
                case 83:
                    myTool.down(game);
                    myTool.paintGame(game);
                    break;
                case 97:
                case 65:
                    myTool.left(game);
                    myTool.paintGame(game);
                    break;
                case 100:
                case 68:
                    myTool.right(game);
                    myTool.paintGame(game);
                    break;
                case 82:
                case 114:
                    myTool.undo();
            }
        }
    }
}
