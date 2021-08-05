# Computer-challenge
Endava has a small RC car with a speaker to pass around news around the office and wants to automate it to do a lap around the office in the most optimal way possible and additionally wants to be able to carry stuff from one room to the other. Implement a system that meets the following criteria:
* Calculates a good route that passes through all rooms once
* Calculate a good route between two rooms
* Reads the room data only once and persists that information
* All files stored on disk must be compressed
* Optional: Be able to calculate routes between two rooms concurrently and somehow show it does so

## Solution
### Fist point
Dijkstra Approach
<b>O(n * 2^n * log(n*2^n)</b>
1. Put all nodes in a queue
2. Make an array with a mask (n*2^n)
3. Go over every child take the decision to take or not the path and save it
4. Get the paths that go through all the nodes
5. Select the minimum cost path

### Second point
Floyd–Warshall algorithm
Save minimum distances to every node and reconstruct the path

## Used libraries
* java.util.zip
* java.util.*