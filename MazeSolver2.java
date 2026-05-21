import java.util.*;
import java.io.*;
public class MazeSolver2 {
    private static int maxx=0;
    private  static int maxy=0;
    private static boolean [][]grid;
    public  MazeSolver2(int maxx,int maxy,boolean [][]grid)
    {
        MazeSolver2.maxx=maxx;
        MazeSolver2.maxy=maxy;
        MazeSolver2.grid=grid;
    }
    public static double penalize(int currx,int curry,int k,int maxx,int maxy,boolean [][]grid,int [][]visited)
    {
        if(k==0)
            currx--;
        else if(k==1)
            currx++;
        else if(k==2)
            curry--;
        else
            curry++;
        int penalty=0;
        if(currx<0 || currx>=maxx || curry<0 || curry>=maxy)
        {
            penalty+=10000;
            if(k==0)
                currx++;
            else if(k==1)
                currx--;
            else if(k==2)
                curry++;
            else
                curry--;
        }
        else if(!grid[currx][curry])
        {
            penalty+=(1000*visited[currx][curry]);
            penalty+=10000;
            if(k==0)
                currx++;
            else if(k==1)
                currx--;
            else if(k==2)
                curry++;
            else
                curry--;
        }
        penalty+=(1000*visited[currx][curry]);
        int goalx = maxx - 1;
        int goaly = maxy - 1;

        int dist = Math.abs(currx - goalx) + Math.abs(curry - goaly);
        return (double) 1 /(penalty+dist+1);
    }
    public static int fitness_calc(int []chrom,boolean[][]grid)
    {
        int maxx=grid.length;
        int maxy=grid[0].length;
        int currx=0;
        int curry=0;
        int goalx=maxx-1;
        int goaly=maxy-1;
        int penalty=0;
        int [][]visited=new int[maxx][maxy];
        for(int i=0;i<chrom.length;i++)
        {
            int oldDist=Math.abs(currx-goalx)+Math.abs(curry-goaly);
            if(chrom[i]==0)
                currx--;
            else if(chrom[i]==1)
                currx++;
            else if(chrom[i]==2)
                curry--;
            else
                curry++;
            if(currx<0 || currx>=maxx || curry<0 || curry>=maxy)
            {
                penalty+=10000;
                if(chrom[i]==0)
                    currx++;
                else if(chrom[i]==1)
                    currx--;
                else if(chrom[i]==2)
                    curry++;
                else
                    curry--;
            }
            else if(!grid[currx][curry])
            {
                penalty+=10000;
                if(chrom[i]==0)
                    currx++;
                else if(chrom[i]==1)
                    currx--;
                else if(chrom[i]==2)
                    curry++;
                else
                    curry--;
            }
            penalty+=(1000*visited[currx][curry]);
            visited[currx][curry]+=1;
            int newDist=Math.abs(currx-goalx)+Math.abs(curry-goaly);

            if(newDist<oldDist)
                penalty-=(200*(chrom.length-i));
            else
                penalty+=(200*(chrom.length-i));
            if(currx==goalx && curry==goaly)
            {
                penalty-=(100000*(maxx+maxy));
                break;
            }
        }
        penalty+=((Math.abs(currx-goalx)+Math.abs(curry-goaly))*10);
        return penalty;
    }
    public static void aco()
    {
        //This code uses Ant Colony Optimization to solve the problem of Maze Solving

        Random random=new Random();
        int n_ants=100;  //The number of ants searching for the solution
        int n_cycles=100; //The number of learning cycles
        double evap_rate=0.35;  //Forgetting factor
        int alpha=1;  //Importance given to pheromone
        int beta=2;   //Importance given to distance
        int q=2;
        double [][][]pheromone=new double[maxx][maxy][4];
        // 0-> Up
        // 1-> Down
        // 2-> Left
        // 3-> Right

        for(int i=0;i<maxx;i++)
        {
            for(int j=0;j<maxy;j++)
                Arrays.fill(pheromone[i][j],1); //All pheromone values are initially set to 1
        }
        int []best_path=new int[(maxx*maxy)];
        int best_penalty=Integer.MAX_VALUE;
        for(int i=0;i<n_cycles;i++)
        {
            for(int j=0;j<n_ants;j++)
            {
                int currx=0;
                int curry=0;
                int []curr=new int[maxx*maxy];
                int p1=0;
                int [][]visited=new int[maxx][maxy];
                while(p1<best_path.length)
                {
                    if(currx == maxx-1 && curry == maxy-1)
                        break;
                    double sum=0.0;
                    ArrayList<Double> probab=new ArrayList<>();
                    for(int k=0;k<4;k++)
                    {
                        double tau=Math.pow(pheromone[currx][curry][k],alpha);
                        double eta=Math.pow(penalize(currx,curry,k,maxx,maxy,grid,visited),beta);
                        sum+=(tau*eta);
                        probab.add(tau*eta);
                    }
                    for(int k=0;k<4;k++)
                        probab.set(k,probab.get(k)/sum);
                    double ran = random.nextDouble();

                    double cumulative = 0.0;
                    int val=0;
                    for(int k=0;k<4;k++)
                    {
                        cumulative+=probab.get(k);
                        if(ran<=cumulative)
                        {
                            val=k;
                            break;
                        }
                    }
                    curr[p1++]=(val);
                    int nx = currx;
                    int ny = curry;

                    if(val==0) nx--;
                    else if(val==1) nx++;
                    else if(val==2) ny--;
                    else ny++;

                    if(nx>=0 && nx<maxx && ny>=0 && ny<maxy && grid[nx][ny])
                    {
                        currx = nx;
                        curry = ny;
                    }
                    visited[currx][curry]+=1;
                }
                int fitness=fitness_calc(curr,grid);
                if (fitness<best_penalty)
                {
                    best_penalty=fitness;
                    for(int k=0;k<best_path.length;k++)
                        best_path[k]=curr[k];
                }
                int x = 0;
                int y = 0;
                double contribution = (double) q / (Math.abs(fitness) + 1);
                for(int step=0; step<p1; step++)
                {
                    int dir = curr[step];
                    if(x>=0 && x<maxx && y>=0 && y<maxy)
                        pheromone[x][y][dir] += contribution;

                    if(dir==0) x--;
                    else if(dir==1) x++;
                    else if(dir==2) y--;
                    else y++;
                }
            }
            for(int j=0;j<maxx;j++)
            {
                for(int k=0;k<maxy;k++)
                {
                    for(int l=0;l<4;l++)
                        pheromone[j][k][l]*=(1-evap_rate);
                }
            }
        }
        printsolution(best_path);
    }
    public static void printsolution(int []best_path)
    {
        for(int i=0;i<best_path.length;i++)
        {
            if(best_path[i]==0)
                System.out.print("U ");
            else if(best_path[i]==1)
                System.out.print("D ");
            else if(best_path[i]==2)
                System.out.print("L ");
            else
                System.out.print("R ");
        }
        System.out.println();
    }
}
