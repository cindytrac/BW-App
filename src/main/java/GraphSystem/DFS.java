package GraphSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

class DFS {

  private Stack<GraphNode> stack;
  private LinkedList<GraphNode> llVisited;

  // Constructor
  DFS() {
    this.stack = new Stack<>();
    llVisited = new LinkedList<GraphNode>();
  }

  // main method for depth-first search
  void dfs(List<GraphNode> nodeList) {
    for (GraphNode g : nodeList) {
      if (!g.isVisited()) {
        g.setVisited(true);
        llVisited.add(g);
        dfsStack(g);
      }
    }
  }

  // Helper method for dfs method
  // test comment
  void dfsStack(GraphNode rootNode) {
    this.stack.add(rootNode);
    rootNode.setVisited(true);

    while (!stack.isEmpty()) {

      GraphNode actualNode = this.stack.pop();
      System.out.print(actualNode.getNodeID() + " "); // changed to Node.nodeID

      for (GraphNode g : actualNode.getNeighbourList()) {
        if (!g.isVisited()) {
          g.setVisited(true);
          llVisited.add(g);
          this.stack.push(g);
        }
      }
    }
  }

  LinkedList<GraphNode> getLLVisited() {
    return llVisited;
  }
}
