import java.io.PrintWriter;
import java.io.IOException;

public class PrintMachineCodeVisitor implements ParserVisitor
{

  String m_outputFileName = null;
  PrintWriter m_writer = null;

  public PrintMachineCodeVisitor(String outputFilename)  {
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

    System.out.println("HEY");
    // Visiter les enfants
    node.childrenAccept(this, null);

    m_writer.close();
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
  public Object visit(ASTLiveNode node, Object data) {

    // TODO:: Vous voulez probablement sauvegarder les lives pour utiliser dans les instructions...

  	node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTInNode node, Object data) {
  	node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTOutNode node, Object data) {
  	node.childrenAccept(this, null);
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
  public Object visit(ASTStmt node, Object data) {
    node.childrenAccept(this, null);
  	return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignStmt node, Object data) {
    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);
    String right = (String)node.jjtGetChild(2).jjtAccept(this, null);


    System.out.println(assigned);
    System.out.println(left);
    System.out.println(right);
    // TODO:: Chaque variable a son emplacement en mémoire, mais si elle est déjà
    // dans un registre, ne la rechargez pas!

    // TODO:: Si vos registres sont plein, déterminez quel variable que vous allez retirer
    // et si vous devez la sauvegarder!

    // TODO:: Écrivez la traduction en code machine, une instruction intermédiaire
    // peut générer plus qu'une instruction machine!

    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignUnaryStmt node, Object data) {
    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);

    // TODO:: Chaque variable a son emplacement en mémoire, mais si elle est déjà
    // dans un registre, ne la rechargez pas!

    // TODO:: Si vos registres sont plein, déterminez quel variable que vous allez retirer
    // et si vous devez la sauvegarder!

    // TODO:: Écrivez la traduction en code machine, une instruction intermédiaire
    // peut générer plus qu'une instruction machine!

    m_writer.println(assigned + " = minus " + left);
    return null;
  }

  // Paramètre data: On ne transmet rien aux enfants
  // Valeur de retour: On ne retourne rien aux parents
  public Object visit(ASTAssignDirectStmt node, Object data) {
    // On ne visite pas les enfants puisque l'on va manuellement chercher leurs valeurs
    // On n'a rien a transférer aux enfants
    String assigned = (String)node.jjtGetChild(0).jjtAccept(this, null);
    String left = (String)node.jjtGetChild(1).jjtAccept(this, null);


    // TODO:: Chaque variable a son emplacement en mémoire, mais si elle est déjà
    // dans un registre, ne la rechargez pas!

    // TODO:: Si vos registres sont plein, déterminez quel variable que vous allez retirer
    // et si vous devez la sauvegarder!

    // TODO:: Écrivez la traduction en code machine, une instruction intermédiaire
    // peut générer plus qu'une instruction machine!
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
