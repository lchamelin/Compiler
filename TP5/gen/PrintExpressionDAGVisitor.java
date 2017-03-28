import java.util.HashSet;
import java.util.HashMap;
import java.util.Vector;
import java.io.PrintWriter;
import java.io.IOException;




public class PrintExpressionDAGVisitor implements ParserVisitor
{

  // TODO:: Est-ce que l'on aurait besoin d'un objet pour encapsuler plus d'informations ou seulement une string est suffisante?
  HashSet<String> m_nodes = new HashSet<String>();
  HashSet<String> m_leafs = new HashSet<String>();
  HashSet<String> m_links = new HashSet<String>();
  Vector<String> v_nodes = new Vector<String>();
  HashMap<Integer, String> myMap = new HashMap<Integer, String>();

  Integer counter = 0;
  String enfant = "";
  String nodeId = "";
  String m_outputFileName = null;
  PrintWriter m_writer = null;

  public PrintExpressionDAGVisitor(String outputFilename)  {
    m_outputFileName = outputFilename;
  }

  // TODO:: Présentement data est inutilisée, pouvez-vous vous en servir pour autre chose?

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(SimpleNode node, Object data) {
    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTProgram node, Object data) {

    try{
      m_writer = new PrintWriter(m_outputFileName, "UTF-8");
    } catch (IOException e) {
      System.out.println("Failed to create ouput file.");
      return null;
    }

    m_writer.println("graph g {");

    // Visite les noeuds pour construire l'arbre et imprimer les noeuds
    // TODO:: Est-ce que ce serait mieux de ne pas imprimer les noeuds durant la visite, mais par la suite?
    node.childrenAccept(this, null);

    // La visite mémorise les liens que l'on imprime ensuite
    for (String link : m_links) {
      m_writer.println("  " + link);
    }

    // rank = sink force les noeuds suivant d'être au plus bas du graphe
    // TODO:: Est-ce que c'est suffisant pour avoir une structure d'arbre?
    m_writer.print("  {rank=sink ");
    for (String leaf : m_leafs) {
      m_writer.print(leaf + " ");
    }
    m_writer.println("}");

    m_writer.println("}");

    m_writer.close();
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTBlock node, Object data) {
  	node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTLive node, Object data) {
  	node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTStmt node, Object data) {
  	node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignStmt node, Object data) {
    node.childrenAccept(this, null);

    ASTIdentifier assigned = (ASTIdentifier)node.jjtGetChild(0);
    //System.out.print(assigned.getValue());
    addNode(String.valueOf(m_nodes.size()), node.getOp(), assigned.getValue());
    
    String parent = node.jjtGetChild(0).jjtAccept(this, data).toString();
    String enfant1 = node.jjtGetChild(1).jjtAccept(this, data).toString();
    String enfant2 = node.jjtGetChild(2).jjtAccept(this, data).toString();
    String operation = node.getOp().toString();

    
    for (String parentNode : v_nodes) {         
      //System.out.println("Node = " + parentNode);
      if(parentNode.equals(enfant1)) {
        System.out.println("Parent: " + parentNode);
        System.out.println("Enfant1: " + enfant1);
        System.out.println("Node vers node");
        addLink(nodeId, myMap.get(v_nodes.indexOf(enfant1)));
      }
      else {
        addLink(nodeId, enfant1);
      }

      if(parentNode.equals(enfant2)) {
        System.out.println("Parent: " + parentNode);
        System.out.println("Enfant2: " + enfant2);
        System.out.println("Node vers node");
        addLink(nodeId, myMap.get(v_nodes.indexOf(enfant2)));
      }
      else {
        addLink(nodeId, enfant2); 
      }
    }
    //System.out.println("Enfants: " + enfant1 + "   " + enfant2);
    /*
    if(v_nodes.contains(enfant1)) {
      String test = myMap.get(v_nodes.indexOf(enfant1));
      //System.out.println(test);
      addLink(test, enfant1);
    }
    else if(v_nodes.contains(enfant2)) {
      String test2 = myMap.get(v_nodes.indexOf(enfant2));
      //System.out.println(test2);
      addLink(test2, enfant1);
    }
    else {
      addLink(nodeId, enfant1);
      addLink(nodeId, enfant2); 
    }
    */
    System.out.println(enfant1);
    System.out.println(enfant2);
    System.out.println("BREAK");
    //addLink(nodeId, enfant1);
    //addLink(nodeId, enfant2); 
    
    // TODO:: Comment lier le noeud à ces enfants?

    // TODO:: Comment fusionner ce noeud a un autre si si l'expression est identique?

    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignUnaryStmt node, Object data) {
    node.childrenAccept(this, null);

    ASTIdentifier assigned = (ASTIdentifier)node.jjtGetChild(0);

    addNode(String.valueOf(m_nodes.size()), "minus", assigned.getValue());

    // TODO:: Quoi faire lorsque le noeud est un unaire?

    // TODO:: Comment lier le noeud à son enfant?
    // TODO:: Comment fusionner ce noeud a un autre si si l'expression est identique?

    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignDirectStmt node, Object data) {
    node.childrenAccept(this, null);

    ASTIdentifier assigned = (ASTIdentifier)node.jjtGetChild(0);

    // TODO:: Quoi faire lorsque le noeud est une assignation directe?

    // TODO:: Comment lier le noeud à son enfant?
    // TODO:: Comment fusionner ce noeud a un autre si si l'expression est identique?

    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTExpr node, Object data) {
  	node.childrenAccept(this, null);

    enfant = node.jjtGetChild(0).jjtAccept(this, data).toString();

    //System.out.print(enfant);

  	return enfant;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTIntValue node, Object data) {
    addLeaf(String.valueOf(node.getValue()));


    //System.out.print(node.getValue());
    return node.getValue();
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTIdentifier node, Object data) {
    // TODO:: Présentement on imprime l'identifiant sans index, "a" au lieu de "a0"
    // Est-ce que ca peut causer des problèmes?

    if(v_nodes.contains(node.getValue())) {
      //System.out.println(node.getValue());
      //addLeaf(node.getValue());
      return node.getValue();
    }
    else {
      addLeaf(node.getValue() + "0");
      return node.getValue() + "0";
    }
  
    //System.out.print(node.getValue());  
  }

  public void addLink(String id1, String id2)
  {
    if(id1.compareTo(id2) <= 0)
    {
      m_links.add(id1 + " -- " + id2);
    }
    else
    {
      m_links.add(id2 + " -- " + id1);
    }
  }

  public void addLeaf(String uniqueLabel) {
    if(m_leafs.contains(uniqueLabel))
      return;
    //System.out.print(uniqueLabel);
    m_leafs.add(uniqueLabel);
    m_writer.println("  " + uniqueLabel + " [label=\"" + uniqueLabel + "\", shape=\"none\"]");
  }

  public void addNode(String uniqueId, String label, String notation) {
    nodeId = uniqueId;
    if(m_nodes.contains(uniqueId))
      return;
    v_nodes.add(notation);
    m_nodes.add(uniqueId);
    myMap.put(counter, uniqueId);
    counter++;
    m_writer.println("  " + uniqueId + " [label=\"" + label + "\", xlabel=\"" + notation + "\", shape=\"circle\"]");
  }
}