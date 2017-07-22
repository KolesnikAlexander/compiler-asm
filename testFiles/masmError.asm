.386
DAT1 SEGMENT
	B1 DB 10011b
	W1 DW 256
	D1 DD -a5A6FH
	D2 DD 2048
	B2 DB "Course work"
	W2 DW 300
	D3 DD 5A6FH
	B3 DB "1235"
	labf dd lab5
	labb dd lab1
DAT1 ENDS

COD1 SEGMENT

	ASSUME CS:COD1, DS:DAT1
	LAHF eax

	SUB B2, byte ptr D1
	SUB CL, B2

	MOV 1 , EBX
	MOV AL, byte ptr ES:D3

	SUB EAX,ES:D3
	SUB CH, byte ptr ES:D3


	SUB BX, EBP+ESI*2
	SUB EAX,DS:[EBP*ESI]

	cmp ES:D1, EBX
	cmp DS:B2, BL

	lab0
		JNG B1
		JNG D3
	lab1:

	ADC [edi+ebx*2], -3

	imul cl, ebp
	imul cs

	AND D1,020F0B01H
	AND AH, cl

	JMP far ptr W2
	cs:

COD1 ENDS
	mov al, cl
COD2 SEGMENT
ASSUME CS:COD2
	lab5: ebx
	JMP -labb
COD2 ENDS
END