import java.io.PrintWriter;
import java.io.IOException;
import java.util.Vector;

public class PrintMachineCodeVisitor implements ParserVisitor
{

  String m_outputFileName = null;
  PrintWriter m_writer = null;
  Vector<String> out_node = new Vector<String>();
  Vector<String> current_in_register = new Vector<String>();
  Vector<String> current_live_or_not = new Vector<String>();
  String registre0 = "";
  String registre1 = "";
  String registre2 = "";
  String registre3 = "";
  String registre4 = "";
  Integer node_out_rendu = 0;
  //CHANGER ICI POUR 5 AFIN DAVOIR 5 REGISTRE ET NE PAS OUBLIER LE FIHCIER .PY
  Integer nbRegistreDisponible = 5;
  Boolean isUse = false;
  Vector<Vector<String>> all_out_node = new Vector<Vector<String>>();

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

    //Append the lives var to the vector
    

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

    //Get the out node of LIVES
    out_node = node.getLive();

    //Get all the outnode in a datastructure
    all_out_node.add(out_node);

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

   
    //Usefull print for debug
    //System.out.println(assigned);
    //System.out.println(left);
    //System.out.println(right);

    // TODO:: Chaque variable a son emplacement en mémoire, mais si elle est déjà
    // dans un registre, ne la rechargez pas!

    //Theses are the lives var for the current instruction
    //System.out.println(all_out_node.elementAt(node_out_rendu));
    
    //Les registre disponible: R0 R1 R2 ET ALTERNATIVE R0 R1 R2 R3 R4
    String r0 = "R0";
    String r1 = "R1";
    String r2 = "R2";
    String r3 = "R3";
    String r4 = "R4";

    //Vars to affect the right register to write the assembly code!
    String destRegister = "";
    String loadRegister1 = "";
    String loadRegister2 = "";
    String storeRegister = "";

    //Car on a uti;liser "deux" pour representer 2... didnt compiled otherwise
    
    if(left.equals("deux")) {
      left = "#2";
    }
    if(right.equals("deux")) {
      right = "#2";
    }

    if(nbRegistreDisponible == 3) {
      m_writer.println("LD " + r0 + ", " + left);
      m_writer.println("LD " + r1 + ", " + right);
      
      //Write the OP to do 
      if(node.getOp().equals("+")) {
        m_writer.println("ADD " + r2 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("-")) {
        m_writer.println("SUB " + r2 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("*")) {
        m_writer.println("MUL " + r2 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("/")) {
        m_writer.println("DIV " + r2 + ", " + r0 + ", " + r1);
      }

      m_writer.println("ST " + assigned + ", " + r2);
    }


    if(nbRegistreDisponible == 5) {
      if(!isUse) {
        m_writer.println("LD " + r0 + ", " + left);
        m_writer.println("LD " + r1 + ", " + right);

        //Write the OP to do 
      if(node.getOp().equals("+")) {
        m_writer.println("ADD " + r4 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("-")) {
        m_writer.println("SUB " + r4 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("*")) {
        m_writer.println("MUL " + r4 + ", " + r0 + ", " + r1);
      }
      if(node.getOp().equals("/")) {
        m_writer.println("DIV " + r4 + ", " + r0 + ", " + r1);
      }

      m_writer.println("ST " + assigned + ", " + r4);
      isUse = true;
      }
      else {
        m_writer.println("LD " + r2 + ", " + left);
        m_writer.println("LD " + r3 + ", " + right);

        //Write the OP to do 
        if(node.getOp().equals("+")) {
          m_writer.println("ADD " + r4 + ", " + r2 + ", " + r3);
        }
        if(node.getOp().equals("-")) {
          m_writer.println("SUB " + r4 + ", " + r2 + ", " + r3);
        }
        if(node.getOp().equals("*")) {
          m_writer.println("MUL " + r4 + ", " + r2 + ", " + r3);
        }
        if(node.getOp().equals("/")) {
          m_writer.println("DIV " + r4 + ", " + r2 + ", " + r3);
        }

        m_writer.println("ST " + assigned + ", " + r4);
        isUse = false;        
      }
    }
   
    
    // TODO:: Si vos registres sont plein, déterminez quel variable que vous allez retirer
    // et si vous devez la sauvegarder!

    // TODO:: Écrivez la traduction en code machine, une instruction intermédiaire
    // peut générer plus qu'une instruction machine!

    //Increment for the lives var at the current instruction
    node_out_rendu += 1;

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

    String r0 = "R0";
    String r1 = "R1";
    String r2 = "R2";
    String r3 = "R3";
    String r4 = "R4";

    m_writer.println("LD " + r0 + ", " + left);
    m_writer.println("ST " + assigned + ", " + r0);

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
