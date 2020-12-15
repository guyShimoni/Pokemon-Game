## readme

# The Pokemon Game :
![image](https://github.com/guyShimoni/Pokemon-Game/blob/main/area.jpg)
In this project (Ex2) we represent an implementation of the game
"Pokemons game".
This task is divided into 2 parts, first part, An implementation of a Weighted graph in java, And the second part is the realizating the "Pokemon game" and managing agents for their target Pokemon similar to the Pacman game the game based on Graphs with nodes and edges taht represent the route. 
on the graph(route), there are pokemons scattered randomly.
the gol in this game is the agants should take as many pokemons as they can so that he will get the largest score in the game.


#part 1:
node_data-implemented by Node,there are node's information key, tag, location weight and info.

edge_data- private class Edge, there are edge's information tag, src node key, dest node key, weight and info.

geo_location-implemented by GeoLocation, 3D point (point have x,y,z), in thes class we have distance(geo_location g) that return the distance between two points.

Directed weighted graph- - implemented by DWGraph_DS, This class constructs a graph defined by nodes and edges, that class holds two hashmaps.
NodeMap- a collection of a Hash Map of nodes in a graph.
EdgeMap- a collection of a Hash Map of edges in a graph.


Dw graph algorithms- implemented by DWGraph_Algo, that implements graph algorithms.
init()- function initializes graph field from a given graph.
copy()- make a deep copy of this weighted graph. 
isConnected()-Returns true if and only if (iff) there is a valid path from each node to each. This function implement by algorithm DFS.
shortestPathDist- returns the length of the shortest path between src to dest. This function implement by algorithm Dijkstra
save, load- Saves and load the graph to a file, in JSON format.

part 2:

Game Client Stracture:

CL_Pokemon-build single pokemon.
CL_Agent-build single agent.
Arena- This class represents a multi Agents Arena which move on a graph - grabs Pokemons.
JFrame- implements MyFrame , this class represents a very simple GUI class to present a
game on a graph.  

Ex2 class- represents a game management system. It enables automatic (efficient) management of the agents. The constructor receives a 0-23 scenario from the server, places the agents optimally, and automatically moves the agents along the graph to collect as much pokemons as possible.


## Sources:

* https://www.kaggle.com/ 

