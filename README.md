# Maze Runner

Maze Runner is a Java project that solves randomly generated mazes using both **Genetic Algorithms (GA)** and **Ant Colony Optimization (ACO)**.

The maze is generated using the **Recursive Division Algorithm**, which creates complex paths, structured corridors, and dead ends for the optimization algorithms to navigate through.

The objective of the algorithms is to discover an optimal sequence of moves that successfully travels from the start cell to the destination while avoiding walls, unnecessary movements, and repeated exploration.

---

# Features

- Recursive Division based maze generation
- Maze solving using Genetic Algorithm
- Maze solving using Ant Colony Optimization
- Collision and revisit penalty system
- Distance-based fitness evaluation
- Elitism and Tournament Selection
- Pheromone-based path reinforcement

---

# Maze Generation

The maze is generated using the **Recursive Division Algorithm**.

This approach recursively divides the grid into smaller sections while leaving openings between walls, creating structured and challenging mazes with multiple dead ends and alternative paths.

---

# Genetic Algorithm

The Genetic Algorithm evolves a population of chromosomes where each chromosome represents a sequence of moves inside the maze.

The algorithm continuously improves solutions over multiple generations using:

- Selection
- Crossover
- Mutation
- Elitism

---

# Genetic Algorithm Parameters

| Parameter | Value |
|---|---|
| Generations | 1000 |
| Population Size | 1000 |
| Elitism Size | 50 |
| Mutation Rate | 0.15 |

---

# Selection Strategy

Tournament Selection is used to select **700 chromosomes** from the current population.

- The top **50 chromosomes** are directly carried forward to the next generation using **elitism**.
- The remaining **250 chromosomes** are generated through **crossover** and **mutation** operations.

---

# Fitness Function

The fitness function evaluates how effectively a chromosome solves the maze.

## Penalties

Penalties are applied for:

- Colliding with walls
- Moving outside maze boundaries
- Revisiting previously visited cells
- Increasing distance from the destination
- Late collisions during traversal

## Rewards

Rewards are provided for:

- Moving closer to the destination
- Successfully reaching the goal

The reward for reaching the destination is scaled according to the maze size so that larger mazes receive proportionally larger rewards.

---

# Ant Colony Optimization

The project also implements **Ant Colony Optimization (ACO)** on the same generated maze.

In this approach:

- Multiple ants explore the maze simultaneously
- Ants probabilistically choose paths based on pheromone concentration and heuristic information
- Good paths receive higher pheromone reinforcement
- Poor paths gradually lose influence due to pheromone evaporation

The algorithm gradually converges toward efficient paths through collective learning.

---

# Ant Colony Optimization Parameters

```java
int n_ants = 100;
int n_cycles = 100;
double evap_rate = 0.35;
int alpha = 1;
int beta = 2;
int q = 2;
```

| Parameter | Description |
|---|---|
| `n_ants` | Number of ants exploring the maze |
| `n_cycles` | Number of learning iterations |
| `evap_rate` | Pheromone evaporation rate |
| `alpha` | Importance of pheromone trails |
| `beta` | Importance of heuristic information |
| `q` | Pheromone contribution factor |

---

# ACO Heuristic Strategy

The heuristic function considers:

- Distance from the destination
- Invalid moves
- Wall collisions
- Boundary collisions

Ants are encouraged to move toward cells that reduce the Manhattan distance to the goal while avoiding invalid states.

---
