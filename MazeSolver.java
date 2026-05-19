import java.util.*;

public class MazeSolver {
    public static int maxx=0, maxy=0;
    static boolean [][]grid;
    public MazeSolver(int maxx,int maxy,boolean [][]grid){
        MazeSolver.maxx=maxx;
        MazeSolver.maxy=maxy;
        MazeSolver.grid=grid;
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
        return penalty*-1;
    }
    public static ArrayList<int []> Tourn_selec(ArrayList<int[]> curr_pop,int []fitness_score)
    {
        ArrayList<int []> new_pop=new ArrayList<>();
        //We will select 750 candidates through tournament selection
        int tourn_size=50;
        int selections=750;
        for(int i=50;i<selections;i++)
        {
            Integer[] selected = new Integer[tourn_size];

            for(int j = 0; j < tourn_size; j++)
            {
                int indx = (int)(Math.random() * curr_pop.size());
                selected[j] = indx;
            }

            Arrays.sort(selected, (x, y) -> Integer.compare(fitness_score[x], fitness_score[y]));
            int []best_chrom=curr_pop.get(selected[tourn_size-1]);
            new_pop.add(best_chrom);
        }
        return new_pop;
    }
    public static ArrayList<int []> Crossover(ArrayList<int[]> new_pop)
    {
        //Producing the next 250 left individuals of the population by crossover
        int crossover_selections=250;

        for(int i=0;i<crossover_selections;i++)
        {
            int indx1=(int)(Math.random()*(new_pop.size()));
            int indx2=(int)(Math.random()*(new_pop.size()));
            int crossover_point=(int)(Math.random()*(new_pop.getFirst().length));
            int []child=new int[new_pop.getFirst().length];
            int []parent1=new_pop.get(indx1);
            int []parent2=new_pop.get(indx2);
            for(int j=0;j<crossover_point;j++)
            {
                child[j]=parent1[j];
            }
            for(int j=crossover_point;j<parent2.length;j++)
            {
                child[j]=parent2[j];
            }
            new_pop.add(child);
        }
        return new_pop;
    }
    public static ArrayList<int []> Mutation(ArrayList<int []>new_pop)
    {
        int mutation_rate=15;
        int crossover_start=750;
        for(int j=crossover_start;j<new_pop.size();j++)
        {
            int []curr_indv=new_pop.get(j);
            int prob=(int)(Math.random()*100);
            if(mutation_rate>prob)
            {
              int indx=(int)(Math.random()*curr_indv.length);
              curr_indv[indx] = (int)(Math.random()*4);
            }
        }
        return new_pop;
    }
    public static ArrayList<int []> Elitism(ArrayList<int []>new_pop,ArrayList<int []>curr_pop,int []fitness_score)
    {
        Integer []order=new Integer[curr_pop.size()];

        for(int i=0;i<curr_pop.size();i++)
        {
            order[i]=i;
        }

        Arrays.sort(order,(a,b)->Integer.compare(fitness_score[a],fitness_score[b]));

        int elitism_size=50;
        int p1=curr_pop.size()-1;

        while(elitism_size>0)
        {
            new_pop.add(curr_pop.get(order[p1]).clone());
            p1-=1;
            elitism_size-=1;
        }

        return new_pop;
    }
    public static void ga_start()
    {
        int gen=1000;
        int pop_size = 1000;
        int chromLength = (maxx + maxy) * 2;
        ArrayList<int[]> population = new ArrayList<>();

        //Grid is required for defining the fitness function

        for (int i = 0; i < pop_size; i++) {
            int[] chrom = new int[chromLength];
            for (int j = 0; j < chromLength; j++) {
                chrom[j] = (int)(Math.random() * 4);
            }
            population.add(chrom);
        }
        //We need to run the loop for 100 generations
        for(int curr_gen=0;curr_gen<gen;curr_gen++) {

            int[] fitness_score = new int[pop_size]; //Stores the fitness_Score for the whole population
            for (int i = 0; i < population.size(); i++) {
                int[] chrom = population.get(i);
                fitness_score[i] = fitness_calc(chrom, grid);
            }
            //Applying Tournament Selection to select new candidates
            ArrayList<int[]> new_pop = Tourn_selec(population, fitness_score);
            new_pop=Elitism(new_pop,population,fitness_score);
            new_pop=Crossover(new_pop);
            new_pop=Mutation(new_pop);
            population=new_pop;
        }
        print_solution(population);
    }
    public static void print_solution(ArrayList<int []>population)
    {
        int pop_size=1000;
        int[] fitness_score = new int[pop_size]; //Stores the fitness_Score for the whole population
        for (int i = 0; i < pop_size; i++) {
            int[] chrom = population.get(i);
            fitness_score[i] = fitness_calc(chrom, grid);
        }
        int best_ans_indx=0;
        for(int i=0;i< population.size();i++)
        {
            if(fitness_score[i]>fitness_score[best_ans_indx])
            {
                best_ans_indx=i;
            }
        }
        int []best_ans=population.get(best_ans_indx);
        //Printing the best path for this maze
        for(int i=0;i<best_ans.length;i++)
        {
            if(best_ans[i]==0)
                System.out.print("U ");
            else if(best_ans[i]==1)
                System.out.print("D ");
            else if(best_ans[i]==2)
                System.out.print("L ");
            else
                System.out.print("R ");
        }
        System.out.println();
    }
}
