import javax.imageio.IIOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author wjx
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class Tools {
    int score = 0; //游戏得分,在flashScore()修改.
    int[][] temp; //传入待操作的数组
    int x; //表示二维数组的长度
    int y; //表示每个一维数组的长度
    ArrayList<int[]> arrList = new ArrayList<>();//定义一个数组集合,方便进行游戏向上和向下下滑动的逻辑实现
    String lastMove;
    int movePace = 0;
    String undo_lastMove;
    int undo_movePace;
    int [][] undo_arr;

    boolean pd_merge = true;
    boolean pd_move = true;

    public void initialize(int[][] arr) {
        temp = arr;
        undo_arr = new int[arr.length][arr[0].length];
        x = temp.length;
        y = temp[0].length;
        for (int i = 0; i < 2; i++) {
            int randomNum1 = new Random().nextInt(temp.length);
            int randomNum2 = new Random().nextInt(temp[0].length);
            while (temp[randomNum1][randomNum2] == 0) {
                temp[randomNum1][randomNum2] = 2;
            }
        }
        paintGame(arr);
    }//初始化数组并显示画面

    public void paintGame(int[][] arr) {
        System.out.println();
        for (int m = 0; m < y; m++) {
            System.out.print("|");
            for (int n = 0; n < 7; n++) {
                System.out.print("-");
            }
            System.out.print("|");
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println();
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] == 0) {
                    System.out.print("|" + "   " + " " + "   " + "|");
                } else {
                    if (arr[i][j] < 10) {
                        System.out.print("|" + "   " + arr[i][j] + "   " + "|");
                    } else if (arr[i][j] < 100) {
                        System.out.print("|" + "  " + arr[i][j] / 10 + " " + arr[i][j] % 10 + "  " + "|");
                    } else if (arr[i][j] < 1000) {
                        System.out.print("|" + " " + arr[i][j] / 100 + " " + arr[i][j] / 10 % 10 + " " + arr[i][j] % 10 + " " + "|");
                    } else {
                        System.out.print("|" + arr[i][j] / 1000 + " " + arr[i][j] / 100 % 10 + " " + arr[i][j] / 10 % 10 + " " + arr[i][j] % 10 + "|");
                    }
                }
            }
            System.out.println();
            for (int m = 0; m < y; m++) {
                System.out.print("|");
                for (int n = 0; n < 7; n++) {
                    System.out.print("-");
                }
                System.out.print("|");
            }
        }
        System.out.println();
        System.out.println("           得分: " + score);
    } //显示画面

    public void random(String judge, int x, int showMovePace) {
        if(mange(temp)) {
            if (((!(judge.equals(lastMove)) || pd_merge) || !(pd_move)) && showMovePace == 1) {
                //for (int i = 0; i < x; i++) {
                int randomNum1 = new Random().nextInt(temp.length);
                int randomNum2 = new Random().nextInt(temp[0].length);
                if (temp[randomNum1][randomNum2] == 0) {
                    temp[randomNum1][randomNum2] = (new Random().nextDouble() < 0.85) ? 2 : 4;
                } else {
                    random(judge, 1, showMovePace);
                }
                // }
                movePace = 0;
            } else {
                movePace = 1;
            }
        }
        else{}
        pd_merge = true;
        pd_move = true;
    } //随机在两个位置生成两个数，小概率是4,大概率是2

    public void up(int[][] arr) {
        copy(arr);

        if (movePace < 1) {
            movePace++;
        }
        for (int i = 0; i < arr[0].length; i++) {
            arrList.add(new int[arr.length]);
        }//创造n个数组,把二维数组的每一列取出来放在这些创造的数组里面
        for (int i = 0; i < arrList.size(); i++) { //暂存数组的索引 同时是原数组的列
            for (int j = 0; j < arr.length; j++) { //原数组的行
                arrList.get(i)[j] = arr[j][i];
            }
        }

        sort(arrList);

        merge(arrList, 0);

        sort(arrList);

        for (int i = 0; i < arrList.size(); i++) { //暂存数组的索引 同时是原数组的列
            for (int j = 0; j < arr.length; j++) { //原数组的行
                arr[j][i] = arrList.get(i)[j];
            }
        } // 再将每一列取出来的放回去

        arrList.clear(); //最后清空集合,方便下一次操作

        random("up", 2, movePace);
        lastMove = "up";
    } //模拟玩家做出向上滑动的操作 0

    public void down(int[][] arr) {
        copy(arr);

        if (movePace < 1) {
            movePace++;
        }
        for (int i = 0; i < arr[0].length; i++) {
            arrList.add(new int[arr.length]);
        }//创造n个数组,把二维数组的每一列取出来放在这些创造的数组里面(这里和up差不多,
        for (int i = 0; i < arrList.size(); i++) { //暂存数组的索引 同时是原数组的列
            for (int j = arr.length - 1; j >= 0; j--) { //原数组的行
                arrList.get(i)[arr.length - j - 1] = arr[j][i];
            }
        }//不过得反着取,反着放

        sort(arrList);

        merge(arrList, 1); //消除相同数字

        sort(arrList);

        for (int i = 0; i < arrList.size(); i++) { //暂存数组的索引 同时是原数组的列
            for (int j = arr.length - 1; j >= 0; j--) { //原数组的行
                arr[j][i] = arrList.get(i)[arr.length - j - 1];
            }
        } // 再讲每一列取出来的放回去(反着放)

        arrList.clear(); //最后清空集合,方便下一次操作

        random("down", 2, movePace);
        lastMove = "down";
    } //模拟玩家做出向下滑动的操作 1

    public void left(int[][] arr) {
        copy(arr);

        if (movePace < 1) {
            movePace++;
        }
        //这里还是要初始化数组集合,因为sort方法形参是集合.
        arrList.addAll(Arrays.asList(arr)); //此处将集合里面的数组指向了游戏界面的二维数组中的元素
        sort(arrList);
        merge(arrList, 0);
        sort(arrList);
        arrList.clear();

        random("left", 2, movePace);
        lastMove = "left";
    }//模拟玩家做出向左滑动的动作 0

    public void right(int[][] arr) {
        copy(arr);

        if (movePace < 1) {
            movePace++;
        }
        //方法和left一样,不过要在sort后反序一遍,然后merge,如果有需要合并的数字,则合并后再右移修正一下
        arrList.addAll(Arrays.asList(arr)); //此处将集合里面的数组指向了游戏界面的二维数组中的元素
        for (int[] temp : arr) {
            for (int i = 0; i < temp.length / 2; i++) {
                int tempNum = temp[i];
                temp[i] = temp[temp.length - 1 - i];
                temp[temp.length - 1 - i] = tempNum;
            }
        }//对其反序
        sort(arrList);
        for (int[] temp : arr) {
            for (int i = 0; i < temp.length / 2; i++) {
                int tempNum = temp[i];
                temp[i] = temp[temp.length - 1 - i];
                temp[temp.length - 1 - i] = tempNum;
            }
        }//对其反序

        merge(arrList, 1);

        for (int[] temp : arr) {
            for (int i = 0; i < temp.length / 2; i++) {
                int tempNum = temp[i];
                temp[i] = temp[temp.length - 1 - i];
                temp[temp.length - 1 - i] = tempNum;
            }
        }//对其反序
        sort(arrList);
        for (int[] temp : arr) {
            for (int i = 0; i < temp.length / 2; i++) {
                int tempNum = temp[i];
                temp[i] = temp[temp.length - 1 - i];
                temp[temp.length - 1 - i] = tempNum;
            }
        }//对其反序


        arrList.clear();

        random("right", 2, movePace);
        lastMove = "right";
    }//模拟玩家做出向右滑动的动作 1

    public void sort(ArrayList<int[]> arrList) {
        for (int[] temp : arrList) {
            for (int i = arrList.get(0).length - 1; i > 0; i--) {
                for (int j = 0; j < temp.length - 1; j++) {
                    if (temp[j] == 0) {
                        if (temp[j] + temp[j + 1] != 0) {
                            int tempNum = temp[j];
                            temp[j] = temp[j + 1];
                            temp[j + 1] = tempNum;
                            pd_move = false;

                        }
                    }
                }
            }
        }
    } //将自己创造的数组排序

    public void merge(ArrayList<int[]> arrList, int x) {
        int ch = 0;
        switch (x) {
            case 1: //从右往左判断
                for (int[] temp : arrList) {
                    for (int i = temp.length - 1; i > 0; i--) {
                        if (temp[i] != 0) {
                            if (temp[i] == temp[i - 1]) {
                                ch++;
                                if (temp[i] != 1024) {
                                    flashScore(temp[i]);
                                    temp[i] *= 2;
                                    temp[i - 1] = 0;
                                } else {
                                    flashScore(temp[i]);
                                    temp[i] = 0;
                                    temp[i - 1] = 0;
                                }
                            }
                        } //嵌套if避免不必要的判断
                    }
                }
                break;
            case 0: //从左往右判断
                for (int[] temp : arrList) {
                    for (int i = 0; i < temp.length - 1; i++) {
                        if (temp[i] != 0) {
                            if (temp[i] == temp[i + 1]) {
                                ch++;
                                if (temp[i] != 1024) {
                                    flashScore(temp[i]);
                                    temp[i] *= 2;
                                    temp[i + 1] = 0;
                                } else {
                                    flashScore(temp[i]);
                                    temp[i] = 0;
                                    temp[i + 1] = 0;
                                }
                            }
                        } //嵌套if避免不必要的判断
                    }
                }
                break;
        }
        if(ch==0)
        {
            pd_merge = false;
        }
    }//合并自己构造的数组中的两个相同的数字，同时计算分数的逻辑也写在此方法内,便于统一管理

    public void flashScore(int x) {
        score +=2*x;
    } //现实得分的方法 x为相消的数,比如相消:2+2,x传入的就是2

    public boolean gameIsLive(int[][] arr) {
        boolean panduan = false;
        for(int i = 0;i<4;i++)
        {
            for(int j = 0;j<3;j++)
            {
                if(arr[i][j]==arr[i][j+1])
                {
                    panduan = true;
                    return panduan;
                }
            }
        }

        for(int j = 0;j<4;j++)
        {
            for(int i=0;i<3;i++)
            {
                if(arr[i][j]==arr[i+1][j])
                {
                    panduan = true;
                    return panduan;
                }
            }
        }

        for(int i =0;i<4;i++)
        {
            for(int j = 0;j<4;j++)
            {
                if(arr[i][j]==0)
                {
                    panduan = true;
                    return panduan;
                }
            }
        }

        return panduan;

    } // 判断游戏是否结束的方法

    public void copy(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                undo_arr[i][j] = arr[i][j];
            }
        }
        undo_movePace = movePace;
        undo_lastMove = lastMove;
    } //暂存游戏状态

    public void undo(){
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = undo_arr[i][j];
            }
        }
        movePace = undo_movePace;
        lastMove = undo_lastMove;
        paintGame(temp);
    }//输出暂存的游戏状态

    public boolean mange(int[][] arr)//判断用户进行操作后页面是否满格
    {
        boolean panduan = false;
        for(int i = 0;i<4;i++)
        {
            for(int j =0;j<4;j++)
            {
                if(arr[i][j]==0)
                {
                    panduan = true;
                }
            }
        }
        return panduan;
    }

    public void FileWriter()
    {
        try(FileWriter fileWriter = new FileWriter("2048Data.txt")){
            int[] data = new int[4];
            for(int i =0;i<4;i++) {
                for(int j =0;j<4;j++)
                {
                    data[j] = temp[i][j];
                }
                String numData = Arrays.toString(data);
                fileWriter.write(numData);


            }
            System.out.println("游戏已保存");
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void FileReader()
    {
        try {
            FileReader fileReader = new FileReader("2048Data.txt");
            int ch;
            int j = 0;
            int num=0;
            char[] buf = new char[1024];
            int[] NumData = new int[16];
            String[] strings = new String[16];

            while ((ch = fileReader.read(buf)) != -1) {
                String data = new String(buf,0,ch);
                String[] numData = data.split("[ \\,]");
                for(int i = 0;i<25;i+=2)
                {
                    if(i==0)
                    {
                        String[] str = numData[i].split("\\[");
                        strings[j] = str[1];
                        j++;
                        continue;
                    }
                    if(i==6||i==12||i==18)
                    {
                        String[] str = numData[i].split("\\]");
                        strings[j] = str[0];
                        j++;
                        String[] str02 = str[1].split("\\[");
                        strings[j] = str02[1];
                        j++;
                        //System.out.println(str02[1]);
                        continue;
                    }
                    if(i==24)
                    {
                        String[] str = numData[i].split("\\]");
                        strings[j]=str[0];
                        break;
                    }
                    strings[j] = numData[i];
                    j++;
                    //System.out.println(numData[i]);
                }
                //System.out.println(numData[1]);
                for(int i =0;i<4;i++)
                {
                    for(int l =0;j<4;j++)
                    {
                        undo_arr[i][j] = Integer.parseInt(strings[num]);
                        num++;
                    }
                }

            }
            fileReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}


