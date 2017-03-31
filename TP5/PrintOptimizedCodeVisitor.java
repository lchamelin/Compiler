import java.io.PrintWriter;
import java.io.IOException;
import java.util.Vector;

public class PrintOptimizedCodeVisitor implements ParserVisitor
{

  String m_outputFileName = null;
  PrintWriter m_writer = null;
  Vector<String> lives = new Vector<String>();
  Vector<String> enfants = new Vector<String>();

  Vector<Vector<String>> to_write = new Vector<Vector<String>>();



  public PrintOptimizedCodeVisitor(String outputFilename)  {
    m_outputFileName = outputFilename;
  }

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

    // Visiter les enfants
    node.childrenAccept(this, null);

    //System.out.println(to_write);

    for(Vector<String> i: to_write) {
      enfants.add(i.get(1));
      enfants.add(i.get(2));
    }
    System.out.println(enfants);
    for(Vector<String> i: to_write) {

      if(lives.contains(i.get(0)) || enfants.contains(i.get(0))) {
        m_writer.println(i.get(0) + " = " + i.get(1) + " " + i.get(3) + " " + i.get(2));
      }
    }

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
    lives = node.getLive();

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
    Vector<String> line_write = new Vector<String>();
    // TODO:: Présentement on imprime seulement le code non-optimizé, comment corriger la situation?

    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs

    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);
    String right = (String)node.jjtGetChild(2).jjtAccept(this, null);
    String operator = node.getOp();
    line_write.add(assigned);
    line_write.add(left);
    line_write.add(right);
    line_write.add(operator);

    to_write.add(line_write);
    //m_writer.println(assigned + " = " + left + " " + node.getOp() + " " + right);
    
    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignUnaryStmt node, Object data) {
    Vector<String> line_write = new Vector<String>();
    // TODO:: Présentement on imprime seulement le code non-optimizé, comment corriger la situation?

    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs

    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);

    line_write.add(assigned);
    line_write.add(" minus " + left);
    line_write.add("");
    line_write.add("");
    to_write.add(line_write);

    //m_writer.println(assigned + " = minus " + left);
    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignDirectStmt node, Object data) {
    Vector<String> line_write = new Vector<String>();
    // TODO:: Présentement on imprime seulement le code non-optimizé, comment corriger la situation?

    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs

    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);

    line_write.add(assigned);
    line_write.add(left);
    line_write.add("");
    line_write.add("");
    to_write.add(line_write);
    //m_writer.println(assigned + " = " + left);
    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: on retourne son identifiant, qui est celui de l'enfant
  public Object visit(ASTExpr node, Object data) {
  	return node.jjtGetChild(0).jjtAccept(this, null);
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On retourne l'indentifiant du noeud
  public Object visit(ASTIntValue node, Object data) {
    return String.valueOf(node.getValue());
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On retourne l'indentifiant du noeud
  public Object visit(ASTIdentifier node, Object data) {
    return node.getValue();
  }
}
