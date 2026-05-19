import java.util.*;
import java.io.*;
import java.util.Random;
public class MazeCreation {
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
        int maxx=0,maxy=0;
        maxx=sc.nextInt();
        maxy=sc.nextInt();

        boolean [][]grid=new boolean[maxx][maxy];
        for(int i=0;i<maxx;i++)
        {
            Arrays.fill(grid[i],true);
        }
        randomwalls(maxx-1,maxy-1,0,0,maxx-1,maxy-1,grid);
        MazeSolver ms=new MazeSolver(maxx,maxy,grid);
        for(int i=0;i<maxx;i++)
        {
            for(int j=0;j<maxy;j++)
            {
                if(grid[i][j])
                    System.out.print("TRUE ");
                else
                    System.out.print("FALSE ");
            }
            System.out.println();
        }
        MazeSolver.ga_start();

    }
    public static void randomwalls(int limx,int limy,
                                   int startx,int starty,
                                   int maxx,int maxy,
                                   boolean [][]grid)
    {
        if(startx >= limx || starty >= limy)
            return;

        Random rand=new Random();

        int type=rand.nextInt(2);

        if(type==0)
        {
            if(limx-startx < 2) return;

            int wall=rand.nextInt(limx-startx-1)+startx+1;
            int hole=rand.nextInt(limy-starty+1)+starty;

            for(int i=starty;i<=limy;i++)
            {
                if(i==hole) continue;
                grid[wall][i]=false;
            }

            randomwalls(wall-1,limy,startx,starty,maxx,maxy,grid);
            randomwalls(limx,limy,wall+1,starty,maxx,maxy,grid);
        }
        else
        {
            if(limy-starty < 2) return;

            int wall=rand.nextInt(limy-starty-1)+starty+1;
            int hole=rand.nextInt(limx-startx+1)+startx;

            for(int i=startx;i<=limx;i++)
            {
                if(i==hole) continue;
                grid[i][wall]=false;
            }

            randomwalls(limx,wall-1,startx,starty,maxx,maxy,grid);
            randomwalls(limx,limy,startx,wall+1,maxx,maxy,grid);
        }
    }
}
