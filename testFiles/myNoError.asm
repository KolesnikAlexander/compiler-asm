DAT1 SEGMENT
	B1 DB 10011b
	W1 DW 256
	D1 DD 5A6FH
	D2 DD 2048
	B2 DB "Course work"
	W2 DW 300
	D3 DD 5A6FH
	B3 DB "1235"
	labf dd lab5
	labb dd lab1
DAT1 ENDS

COD1 SEGMENT

	LAHF

	SUB ECX, D1
	SUB CL, byte ptr D1
	SUB CL, B2

	MOV EDX, EBX
	MOV AL, AH

	SUB EAX,ES:D3
	SUB CH, byte ptr ES:D3

	SUB EAX,[EBP+ESI*2]
	SUB EAX,DS:[EBP+ESI*2]
	SUB EAX,SS:[EBP+edi*2]
	SUB AL,DS:[EBX+ESI*2]
	SUB AL,SS:[EBX+edi*2]
	SUB AL,SS:[edi+ebx*2]

	cmp ES:D1, EBX
	cmp DS:B2, BL

	lab0:
	JNG lab0
	JNG lab1
	lab1:

	ADC D2, 5AH
	ADC byte ptr D3, 5AH
	ADC dword ptr D2, 5AH

	imul cl
	imul ECX

	AND EAX,020F0B01H
	AND AH, 6H

	JMP far ptr lab2
	lab3:
	JMP labf

COD1 ENDS
COD2 SEGMENT
	lab5:
	MOV AL, AH
	lab2:
	JMP far ptr lab3
	JMP labb
COD2 ENDS
END