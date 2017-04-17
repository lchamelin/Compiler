// unsigned int fib(unsigned int n){
//    unsigned int i = n - 1, a = 1, b = 0, c = 0, d = 1, t;
//    if (n <= 0)
//      return 0;
//    while (i > 0){
//      if (i % 2 == 1){
//        t = d*(b + a) + c*b;
//        a = d*b + c*a;
//        b = t;
//      }
//      t = d*(2*c + d);
//      c = c*c + d*d;
//      d = t;
//      i = i / 2;
//    }
//    return a + b;
//  }

PRINT "Please enter the number of the fibonacci suite to compute:"
INPUT n

//    if (n <= 0)
//      return 0;
LD R0, n
BGTZ R0, validInput
PRINT #0
BR end

validInput:
//    unsigned int i = n - 1, a = 1, b = 0, c = 0, d = 1, t;
DEC R0
ST i, R0
ST a, #1
ST b, #0
ST c, #0
ST d, #1

//    while (i > 0){
beginWhile:
LD R0, i
BLETZ R0, printResult

//      if (i % 2 == 1){
MOD R0, R0, #2
DEC R0
BNETZ R0, afterIf

CLEAR

//        t = d*(b + a) + c*b;
//        a = d*b + c*a;
//        b = t;

// TODO:: PUT THE BLOCK 1 THERE!
LD R0, b
LD R1, a
ADD R4, R0, R1
ST i1, R4
LD R2, d
LD R3, i1
MUL R4, R2, R3
ST i2, R4
LD R0, c
LD R1, b
MUL R4, R0, R1
ST i3, R4
LD R2, i2
LD R3, i3
ADD R4, R2, R3
ST t, R4
LD R0, d
LD R1, b
MUL R4, R0, R1
ST i4, R4
LD R2, c
LD R3, a
MUL R4, R2, R3
ST i5, R4
LD R0, i4
LD R1, i5
ADD R4, R0, R1
ST a, R4
LD R0, t
ST b, R0

CLEAR

afterIf:
CLEAR

//      t = d*(2*c + d);
//      c = c*c + d*d;
//      d = t;
//      i = i / 2;

// TODO:: PUT THE BLOCK 2 THERE!
LD R0, #2
LD R1, c
MUL R4, R0, R1
ST j1, R4
LD R2, j1
LD R3, d
ADD R4, R2, R3
ST j2, R4
LD R0, d
LD R1, j2
MUL R4, R0, R1
ST t, R4
LD R2, d
LD R3, d
MUL R4, R2, R3
ST j3, R4
LD R0, c
LD R1, c
MUL R4, R0, R1
ST j4, R4
LD R2, j3
LD R3, j4
ADD R4, R2, R3
ST c, R4
LD R0, t
ST d, R0
LD R0, i
LD R1, #2
DIV R4, R0, R1
ST i, R4


// TODO:: This instruction is just a placeholder to let the code end, remove the code below!
//LD R0, i
//DEC R0
//ST i, R0
// TODO:: Remove the placeholder above of this line!

CLEAR
BR beginWhile

//    return a + b;
printResult:
LD R0, a
LD R1, b
ADD R0, R0, R1
PRINT R0

end:
PRINT "END"