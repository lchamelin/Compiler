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
