/* Generated By:JJTree: Do not edit this line. ASTAssignDirectStmt.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTAssignDirectStmt extends SimpleNode {
  public ASTAssignDirectStmt(int id) {
    super(id);
  }

  public ASTAssignDirectStmt(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cf794cfb6f1ce3b7f4f4e0249504a8b8 (do not edit this line) */
