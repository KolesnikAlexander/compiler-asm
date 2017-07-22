# compiler-asm
Compiler for x86 Assembly that creates a listing file.<br>
This is done as a coursework on a System Programming subject at the KPI university.
The task is to create a compiler that produces a listing file (.lst).<br>
Listing is a file that presents a machine code generated for assembly commands in a human-readable format.
Compiler supports a small set of the x86 assembly commands.

## Example
#### Source code (.asm)
![alt source](/materials/img/asm.png)
#### Result (.lst)
![alt result](/materials/img/listing.png)
## Task
The orginal task is to implement the following assembly code (next is in Ukrainian):

###### Ідентифікатори
Містять великі і малі букви латинского алфавіту та цифри. Починаються з букви. 
Великі та малі букви не відрізняються. Довжина ідентифікаторів не більше 4 символів

###### Константи
Шістнадцяткові, десяткові, двійкові та текстові константи

###### Директиви
END, SEGMENT - без операндів, ENDS, ASSUME
DB,DW,DD з одним операндом - константою (рядкові константи тільки для DB)

###### Розрядність даних та адрес
32 - розрядні дані та зміщення в сегменті, 16 -розрядні дані та зміщення не використовуються

###### Адресація операндів пам'яті
Пряма адресація.
Базова індексна адресація із множником ([edx+esi*4],[ebx+ecx*2] і т.п.) з оператором визначення типу (ptr) при необхідності.

###### Заміна сегментів
Префікси заміни сегментів можуть задаватись тільки явно

###### Машинні команди
Lahf<br>
Sub reg, mem<br>
Mov reg, reg<br>
Cmp mem,reg<br>
Adc mem,imm<br>
Imul reg<br>
And reg,imm<br>
Jng<br>
jmp (міжсегментна пряма та посередня адресація)<br>
Де reg – 8 або 32-розрядні РЗП<br>
mem – адреса операнда в пам’яті<br>
imm - 8 або 32-розрядні безпосередні дані (константи)<br>

