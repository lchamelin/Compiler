LD R0, deux
LD R1, c
MUL R0, R0, R1
ST j1, R0
LD R0, j1
LD R1, d
ADD R0, R0, R1
ST j2, R0
LD R0, d
LD R1, j2
MUL R0, R0, R1
ST t, R0
LD R0, d
LD R1, d
MUL R0, R0, R1
ST j3, R0
LD R0, c
LD R1, c
MUL R0, R0, R1
ST j4, R0
LD R0, j3
LD R1, j4
ADD R0, R0, R1
ST c, R0
LD R0, i
LD R1, deux
DIV R0, R0, R1
ST i, R0
