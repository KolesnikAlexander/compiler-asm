.386
.186
DATA1 SEGMENT PARA
	B1 DB 10011b
	W1 DW 256
	D1 DD 5A6FH
	D2 DD 2048
	
	W2 DW 300
	D3 DD 5A6FH
	B3 DB '1235'
	dvalue db 30 dup ('0',0fh) ;зарезервовано 30 десяткових розрядів змінної dvalue
	
	len dw 5
	row dw 3
	col dw 7

	valadd db 1 ;значення, яке додається до dvalue

	cont db 0ffh ;ознака продовження роботи
DATA1 ENDS

STACK1 SEGMENT STACK
	DB 256 DUP ('?')
STACK1 ENDS

CODE1 SEGMENT
	ASSUME CS:CODE1, DS:DATA1

MAIN PROC FAR
	mov ax,Data1            
    mov ds,ax
	;Инициализация драйвера мыши
	MOV AX, 00H ;Использовать драйвер мыши
	INT 33H

	MOV AX, 0CH; Установить функцию-обработчик
	MOV CX, 1010b; событие - нажатие л.клав. или пр. клав.

	push ES ;зберегти вміст сегментного регістра
	push CS
	pop ES ; вважаєм, що процедура користувача
	lea dx, MOUSE_HANDLER; встановити зміщення процедури
	INT 33H

	mov ah, 00H  ;установка видеорнежима
	mov al, 03H  ;Цветной текстовый режим
	INT 10H	

	MOV AX, 01H ;Показать указатель
	INT 33H
@write: ; відображення dvalue
	MOV BX, 0b800h ;начальный адрес видеобуфера
	MOV ES, BX
	сli ; заборона переривань, щоб мишка "не мішала"
	mov si,offset dvalue
	mov al, 0; byte ptr row ; обчислення Nrow ?80?2 + Ncolumn ? 2
	mov ah,0
	MOV BX, 160
	MOV CX, 1
	start_loop:
		CMP CX, BX
		jg end_loop
		add ax, byte ptr row
		add CX, 1
		jmp start_loop
	end_loop:
	add ax,col
	add ax,col ; завершення обчислення
	mov di,ax
	mov cx,len
	cld
	rep movsw ;виведення на екран
	sti ;переривання дозволяються
@9: ; додавання чергового значення :
	mov si,len
	shl si,1
	sub si,2 ;визначення адреси молодшої цифри dvalue
	mov ah,0 ;початкове значення перенесення в старший
			;десятковий розряд
	mov al, dvalue[si]
	add al,valadd
	aaa ;корекція при додаванні розпакованих
			;двійково-десяткових чисел
	or al,30h ;формування коду ASCII десяткової цифри
	mov dvalue[si],al
@10:
	dec si
	dec si
	mov al,ah
	mov ah,0
	add al,dvalue[si] ;додавання перенесення
	aaa
	or al,30h
	mov dvalue[si],al
	cmp si,0
	jnz @10
	mov al,cont ;аналіз ознаки продовження роботи програми
	cmp al,0ffh
	je @write
	CALL EXIT
MAIN ENDP 

MOUSE_HANDLER PROC FAR
	MOV AX, 03H ; получить текущее положение указателя
	INT 33H
	MOV AX, BX ; регистры с информацией
	
	CHECK_LEFT: AND BX, 11 ; проверка нажатия левой клавиши
				CMP BX, 01
				jne CHECK_RIGHT
				CMP len, 2
				je R
				sub len, 1
				jmp R
	CHECK_RIGHT: ;нажата правая клавиша
	CALL EXIT
R:
mov ax, 03 ;очистка экрана
int 10h
RET
MOUSE_HANDLER ENDP

CLEAR PROC NEAR
	PUSHA
	MOV AX, 0600H
	MOV BH, 07
	MOV CX, 0000
	MOV DX, 184FH
	INT 10H
	POPA
	RET
CLEAR ENDP

EXIT PROC NEAR
	xor cx,cx ;cx=0
	mov ax,0ch
	int 33h ;процедура prmaus далі викликатись не буде

	mov ax, 03 ;очистка экрана
	int 10h

	POP ES

	MOV AX, 4C00H
	INT 21H
EXIT ENDP

CODE1 ENDS
END MAIN