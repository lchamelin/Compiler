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
