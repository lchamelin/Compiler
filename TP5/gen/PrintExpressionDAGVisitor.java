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
  Vector<Vector<String>> v_print = new Vector<Vector<String>>();
  Vector<Vector<String>> compare_print = new Vector<Vector<String>>();
  HashMap<Integer, String> myMap = new HashMap<Integer, String>();

  Integer counter = 0;
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

    Integer indexLoop = 0;
    Integer indexLoop2 = 0;
    Boolean dejaTrouve = false;
    Integer indexToSkip = -1;

    for(Vector<String> i: compare_print) {
      String operator = i.get(1);
      String enf1 = i.get(2);
      String enf2 = i.get(3);
      String identiqueAdd = "";
      Boolean isIdentique = false;
      

      indexLoop2 = 0;
      for(Vector<String> j: compare_print) {
        if(j.get(1).equals(operator) && indexLoop != indexLoop2){
          if(j.get(2).equals(enf1) && j.get(3).equals(enf2)) {
            indexToSkip = indexLoop;

            if(!dejaTrouve) {
              isIdentique = true;
              dejaTrouve = true;
            }
            
            identiqueAdd = j.get(0);
          }
        }
        indexLoop2++;
      }

      if(isIdentique) {
        m_writer.println("  " + i.get(4) + " [label=\"" + i.get(1) + "\", xlabel=\"" + i.get(0) + ", " + identiqueAdd  + "\", shape=\"circle\"]");
      }
      else if(i.get(4).equals(String.valueOf(indexToSkip))){
        //System.out.println(i.get(4));
        //System.out.println(i);
      }
      else {
        m_writer.println("  " + i.get(4) + " [label=\"" + i.get(1) + "\", xlabel=\"" + i.get(0) + "\", shape=\"circle\"" + ", color=\"red\"]");
      }
      indexLoop++;
    }

    // La visite mémorise les liens que l'on imprime ensuite
    for (String link : m_links) {
      if(!link.contains(String.valueOf(indexToSkip))) {
        m_writer.println("  " + link);
      }
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
    ASTIdentifier assigned = (ASTIdentifier)node.jjtGetChild(0);
    
    String enfant1 = node.jjtGetChild(1).jjtAccept(this, data).toString();
    String enfant2 = node.jjtGetChild(2).jjtAccept(this, data).toString();

    Boolean enfant1Pres = false;
    Boolean enfant2Pres = false;
    
    for (String parentNode : v_nodes) {         
      if(parentNode.equals(enfant1)) {
        addLink(String.valueOf(m_nodes.size()), myMap.get(v_nodes.indexOf(enfant1)));
        enfant1Pres = true;
      }

      if(parentNode.equals(enfant2)) {

        addLink(String.valueOf(m_nodes.size()), myMap.get(v_nodes.indexOf(enfant2)));
        enfant2Pres = true;
      }
    }

    if(!enfant1Pres) {
      addLink(String.valueOf(m_nodes.size()), enfant1);
    }

    if(!enfant2Pres) {
      addLink(String.valueOf(m_nodes.size()), enfant2);
    }

    storeData(assigned.getValue(), node.getOp(), enfant1, enfant2, String.valueOf(m_nodes.size()));
    addNode(String.valueOf(m_nodes.size()), node.getOp(), assigned.getValue());

    return null;
  }

  public void storeData(String parent, String op, String enfant1, String enfant2, String uniqueId) {
    Vector<String> all_nodes = new Vector<String>();
    
    all_nodes.add(parent);
    all_nodes.add(op);
    all_nodes.add(enfant1);
    all_nodes.add(enfant2);
    all_nodes.add(uniqueId);

    compare_print.add(all_nodes);
  }


  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignUnaryStmt node, Object data) {
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
    ASTIdentifier assigned = (ASTIdentifier)node.jjtGetChild(0);
    // TODO:: Quoi faire lorsque le noeud est une assignation directe?
    
    Boolean enfant1Pres = false;

    // TODO:: Comment lier le noeud à son enfant?
    String enfant1 = node.jjtGetChild(1).jjtAccept(this, data).toString();
    // TODO:: Comment fusionner ce noeud a un autre si si l'expression est identique?
    for (String parentNode : v_nodes) {         
      if(parentNode.equals(enfant1)) {
        addLink(String.valueOf(m_nodes.size()), myMap.get(v_nodes.indexOf(enfant1)));
        enfant1Pres = true;
      }
    }

    if(!enfant1Pres) {
      addLink(String.valueOf(m_nodes.size()), enfant1);
    }

    storeData(assigned.getValue(), "=", enfant1, "", String.valueOf(m_nodes.size()));
    addNode(String.valueOf(m_nodes.size()), "=", assigned.getValue());

    return null;
  }


  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTExpr node, Object data) {
  	return node.jjtGetChild(0).jjtAccept(this, data);
  }


  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTIntValue node, Object data) {
    addLeaf("num" + String.valueOf(node.getValue()), String.valueOf(node.getValue()));
    return "num" + String.valueOf(node.getValue());
  }


  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTIdentifier node, Object data) {
    // TODO:: Présentement on imprime l'identifiant sans index, "a" au lieu de "a0"
    // Est-ce que ca peut causer des problèmes?
    if(v_nodes.contains(node.getValue())) {
      return node.getValue();
    }
    else {
      addLeaf(node.getValue() + "0",node.getValue() + "0");
  
      return node.getValue() + "0";
    }
  }


  public void addLink(String id1, String id2) {
    m_links.add(id1 + " -- " + id2);
  }


  public void addLeaf(String uniqueLabel, String val) {
    if(m_leafs.contains(uniqueLabel))
      return;

    m_leafs.add(uniqueLabel);
    m_writer.println("  " + uniqueLabel +  " [label=\"" + val + "\", shape=\"none\"]");
  }


  public void addNode(String uniqueId, String label, String notation) {
    if(m_nodes.contains(uniqueId))
      return;
    Vector<String> n_nodes = new Vector<String>();
    n_nodes.add(uniqueId);
    n_nodes.add(label);
    n_nodes.add(notation);
    v_nodes.add(notation);
    m_nodes.add(uniqueId);
    myMap.put(counter, uniqueId);
    counter++;
    v_print.add(n_nodes);
    //m_writer.println("  " + uniqueId + " [label=\"" + label + "\", xlabel=\"" + notation + "\", shape=\"circle\"]");
  }
}