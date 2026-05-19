# Maze Runner

Maze Runner is a Java project that solves randomly generated mazes using a Genetic Algorithm.  
The maze itself is generated using the Recursive Division Algorithm, which creates complex paths and dead ends for the algorithm to navigate through.

The objective of the Genetic Algorithm is to evolve a sequence of moves that can successfully travel from the start cell to the destination while avoiding walls and unnecessary movements.

---

## Genetic Algorithm Parameters

| Parameter | Value |
|---|---|
| Generations | 1000 |
| Population Size | 1000 |
| Elitism Size | 50 |
| Mutation Rate | 0.15 |

---

## Selection Strategy

- Tournament Selection is used to select 700 chromosomes from the current population.
- The top 50 chromosomes are directly carried forward to the next generation using elitism.
- The remaining 250 chromosomes are generated through crossover and mutation.

---

## Fitness Function

The fitness function evaluates how good a chromosome is at solving the maze.

Penalties are applied for:

- Colliding with walls
- Moving outside the maze boundaries
- Revisiting already visited cells
- Increasing the distance from the destination
- Late collisions during traversal

Rewards are provided for:

- Moving closer to the goal
- Successfully reaching the destination

The reward for reaching the goal is scaled according to the maze size so that larger mazes receive a higher reward.

---

## Maze Generation

The maze is generated using the **Recursive Division Algorithm**.  
This approach recursively divides the grid into smaller sections while leaving openings between walls, creating structured and challenging mazes.

---
