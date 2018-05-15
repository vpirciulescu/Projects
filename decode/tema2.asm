;%include "io.inc"

extern puts
extern printf
extern strlen
extern strstr

section .data
filename: db "./input.dat",0
inputlen: dd 2263
fmtstr: db "Key: %d",0xa,0
fmtstr2: db "Key: %c",0xa,0
hint: db "force", 0

section .bss
key: resb 1
tabela:     resb    30

section .text
global main

; TODO: define functions and helper functions
xor_strings:
            push ebp
            mov ebp, esp
            
            mov eax, [ebp + 8]
            mov ebx, [ebp + 12]
            
            sub ebx, eax
           
            xor ecx, ecx
            mov ecx, eax
            xor edi, edi
            
            make_xor:
                    mov eax, ecx
                    mov edx, [ecx + ebx]
                    xor byte [eax], dl
                    inc ecx
                    inc edi;
                    cmp edi, ebx
                    jnz make_xor
        
            leave
            ret

rolling_xor:
            push ebp
            mov ebp, esp
            
            mov ebx, [ebp + 8]
            
            push ebx
            
            push ebx
            call strlen
            add esp, 4
           
            mov ecx, eax
            pop ebx
            dec ecx
            add ebx, ecx
            
            make_ror:
                    mov eax, ebx
                    mov edx, [eax - 1]
                    xor byte [eax], dl
                    dec ebx
                    dec ecx
                    cmp ecx, 0
                    jnz make_ror
            
            leave
            ret

xor_hex_strings:
                push ebp
                mov ebp, esp
                
                mov ebx, [ebp + 12] ;str 5

                push ebx
                call strlen
                add esp, 4
                                             
                mov esi, [ebp + 8] ;str 4
                mov ebx, [ebp + 12] ;str 5
                                                    
                xor ecx, ecx         
                xor_h_str:
                            push eax
                            mov al, byte [esi + ecx]
                            cmp al, 97
                            jge litera
                            jmp cifra
                      
                            continuare:      
                                     mov dl, 16
                                     mul dl
                                     
                                     mov dl, byte [esi + ecx + 1]
                                     cmp dl, 97
                                     jge lit                                     
                                     sub dl, 48
                                     jmp conti
                                      
                                     lit: 
                                         sub dl, 87

                                     conti:
                                          add al, dl
                                          
                            push eax
                            xor eax, eax
                                       
                            mov al, byte [ebx + ecx]
                            cmp al, 97
                            jge litera2
                            jmp cifra2
                             
                            cont2:
                                    mov dl, 16
                                    mul dl
                                    
                                    mov dl, byte [ebx + ecx + 1]                    
                                    cmp dl, 97
                                    jge li
                                    sub dl, 48
                                    jmp continu
                                    
                                    li:
                                        sub dl, 87
                                    continu:
                                        add al, dl
                            
                            mov dl, al
                            pop eax
                            xor al, dl
                            push eax
                            
                            mov ax, cx
                            mov dl, 2
                            div dl
                            
                            mov edx, eax
                            pop eax
                            mov byte [esi + edx], al
                         
                            add ecx, 2
                            pop eax
                            cmp ecx, eax
                            jge ext
                            jmp xor_h_str
                
                litera:
                        sub al, 87
                        jmp continuare
                        
                cifra:  
                        sub al, 48
                        jmp continuare
                  
                  
                  
                litera2:
                        sub al, 87
                        jmp cont2
                        
                cifra2:  
                        sub al, 48
                        jmp cont2
         ext:
         
                mov dl, 2
                div dl
                
                mov byte [esi + eax], 0x00
                leave
                ret

base32decode:
            push ebp
            mov ebp, esp
            
            mov ecx, [ebp + 8]
            
            push ecx
            call strlen
            add esp, 4
            
            mov ecx, eax
            mov eax, [ebp + 8]
           
            xor ebx, ebx
            
            replace_lit_code:
                    mov dl, byte [eax + ebx]
                    ; 61 este = in ASCII
                    cmp dl, 61
                    je complete
                    cmp dl, 65
                    jge alfa
                    ;24 + cifra = ascii
                    sub dl, 24 
                    
                    nexti:
                          mov byte [eax + ebx], dl
                    
                    inc ebx
                    cmp ebx, ecx
                    je make_shift
                    jmp replace_lit_code 
                                                                                                                     
                    alfa:
                        sub dl, 65
                        jmp nexti
                                         
                   xor ebx, ebx 
                   xor edx, edx
           
           complete:
                    mov byte [eax + ebx], 0x00
                    inc ebx
                    cmp ebx, ecx
                    jne complete
                   
                   xor edx, edx
                   xor ebx, ebx
                   xor edi, edi
                   
           make_shift:
                   push ecx
                   mov dl, byte [eax + ebx]
                   shl dl, 3
                   mov cl, byte [eax + ebx + 1]
                   shr cl, 2
                   or dl, cl
                   mov byte [eax + edi], dl
                   
                   mov dl, byte [eax + ebx + 1]
                   shl dl, 6
                   mov cl, byte [eax + ebx + 2]
                   shl cl, 1
                   or dl, cl
                   mov cl, byte [eax + ebx + 3]
                   shr cl, 4
                   or dl, cl
                   mov byte [eax + edi + 1], dl
                   
                   mov dl, byte [eax + ebx + 3]
                   shl dl, 4
                   mov cl, byte [eax + ebx + 4]
                   shr cl, 1
                   or dl, cl
                   mov byte [eax + edi + 2], dl
                   
                   mov dl, byte [eax + ebx + 4]
                   shl dl, 7
                   mov cl, byte [eax + ebx + 5]
                   shl cl, 2
                   or dl, cl
                   mov cl, byte [eax + ebx + 6]
                   shr cl, 3
                   or dl, cl
                   mov byte [eax + edi + 3], dl
                                      
                   mov dl, byte [eax + ebx + 6]
                   shl dl, 5
                   mov cl, byte [eax + ebx + 7]
                   or dl, cl
                   mov byte [eax + edi + 4], dl
                
                   pop ecx
                   add edi, 5
                   add ebx, 8
                   cmp ebx, ecx
                   jge outt
                   jmp make_shift                  
      outt: 
     
            leave
            ret

bruteforce_singlebyte_xor:
        push ebp
        mov ebp, esp
         
        mov edi, [ebp + 8]
        
        push edi
        call strlen
        add esp, 4
        
        mov ebx, eax
        
        xor ecx, ecx
        mov cl, 1
        
        caut:
            inc cl
            xor edi, edi
            mov edi, [ebp + 8]
            xor eax, eax
            
            xor_str:
                    xor byte [edi], cl
                    inc edi
                    inc eax
                    cmp eax, ebx
                    jne xor_str                         
       
            sub edi, ebx
                       
            push ecx
            
            push hint
            push edi
            call strstr
            add esp, 8
                   
            pop ecx
            
            cmp ecx, 255
            je exit
          
            cmp eax, 0
            je caut
             
     exit:
        xor eax, eax
        mov eax, ecx
          
        leave 
        ret 

break_substitution:
        
            push ebp
            mov ebp, esp
            
            ;[ebp + 12] tabela
            ;[ebp + 8] string 
            
           mov esi, [ebp + 8]
           
           push esi
           call strlen 
           add esp, 4
           
           mov edi, [ebp + 12]
           xor ebx, ebx
          
          replace:
               
              mov dl, byte [esi + ebx]
              
              push ebx
              xor ebx, ebx
              
              search:
                    add ebx, 2
                    cmp dl, byte [edi + ebx - 1]
                    jne search
                    
               mov edx, ebx
               sub edx, 2
               pop ebx
               mov cl, byte [edi + edx]
               mov byte [esi + ebx], cl
                                 
               inc ebx
               cmp ebx, eax 
               jne replace
                
            leave 
            ret
            
main:
    mov ebp, esp; for correct debugging
    push ebp
    mov ebp, esp
    sub esp, 2300
    
    ; fd = open("./input.dat", O_RDONLY);
    mov eax, 5
    mov ebx, filename
    xor ecx, ecx
    xor edx, edx
    int 0x80
    
	; read(fd, ebp-2300, inputlen);
	mov ebx, eax
	mov eax, 3
	lea ecx, [ebp-2300]
	mov edx, [inputlen]
	int 0x80

	; close(fd);
	mov eax, 6
	int 0x80

	; all input.dat contents are now in ecx (address on stack)

	; TASK 1: Simple XOR between two byte streams
	; TODO: compute addresses on stack for str1 and str2
	; TODO: XOR them byte by byte


        ;size of the first string
        mov edx, ecx        
         
        push ecx
         
        push edx
        call strlen
        add esp, 4
         
        pop ecx
        
        mov edx, ecx
        add edx, eax
        add edx, 1
        
        push eax    ;save the length of str1
        push ecx    ;save 'input.dat'

        push edx    ;push addr_str2
        push ecx    ;push addr_str1
        call xor_strings
        add esp, 8

        pop ecx
        push ecx
        
	;Print the first resulting string
	push ecx
	call puts
	add esp, 4

        pop ecx
        pop eax
        
	; TASK 2: Rolling XOR
	; TODO: compute address on stack for str3
	; TODO: implement and apply rolling_xor function

        ;find addr_str3
         add eax, eax
         add eax, 2
         add ecx, eax 
         
        push ecx
         
        push ecx
        call rolling_xor
        add esp, 4
        
        pop ecx
        
        push ecx
	;Print the second resulting string
	push ecx
	call puts
	add esp, 4

        
        ; TASK 3: XORing strings represented as hex strings
	; TODO: compute addresses on stack for strings 4 and 5
	; TODO: implement and apply xor_hex_strings
        pop ecx
        push ecx
        
        push ecx
        call strlen
        add esp, 4
        
        pop ecx
        add ecx, eax
        add ecx, 1
        
        push ecx
        
        push ecx
        call strlen
        add esp, 4
        
        pop ecx
        mov edx, ecx
        
        add ecx, eax
        add ecx, 1
        
        push edx
        
        push ecx
        push edx
        call xor_hex_strings
        add esp, 8
        
        pop ecx        
        push ecx
        ; Print the third string
        push ecx
        call puts 
        add esp, 4
       
	; TASK 4: decoding a base32-encoded string
	; TODO: compute address on stack for string 6
	; TODO: implement and apply base32decode
    
        pop ecx
        
        push ecx
        push ecx
        call strlen
        add esp, 4
        
        pop ecx
        add eax, eax
        add eax, eax
        add ecx, eax
        add ecx, 2
                
        push ecx
        
        push ecx 
        call strlen
        add esp, 4
                
        pop ecx
        push eax
        push ecx
        
	push ecx       ;push addr_str6
	call base32decode
	add esp, 4

        pop ecx
        push ecx
	; Print the fourth string
        
        push ecx ;push addr_str6
        call puts
        add esp, 4

	; TASK 5: Find the single-byte key used in a XOR encoding
	; TODO: determine address on stack for string 7
	; TODO: implement and apply bruteforce_singlebyte_xor
             
        pop ecx
        pop eax
        add ecx, eax
        add ecx, 1
           
        push ecx    
        push ecx
        call bruteforce_singlebyte_xor
        add esp, 4
            
        mov [key], eax
           
        pop ecx  
        push ecx     
        push ecx
        call puts
        add esp, 4
     
        mov eax, [key]
        push eax
        push fmtstr
        call printf
        add esp, 8
        
	; TASK 6: Break substitution cipher
	; TODO: determine address on stack for string 8
	; TODO: implement break_substitution

        pop ecx
        
        push ecx
        push ecx
        call strlen
        add esp, 4
        
        pop ecx
        add ecx, eax
        add ecx, 1
            
        ;aqbrcwdee fugthyiijokplfmhn.ogpdqarssltkumvjwnxbyzzv c.x
        mov byte [tabela], 'a'
        mov byte [tabela + 1], 'q'
        mov byte [tabela + 2], 'b'
        mov byte [tabela + 3], 'r'
        mov byte [tabela + 4], 'c'
        mov byte [tabela + 5], 'w'
        mov byte [tabela + 6], 'd'
        mov byte [tabela + 7], 'e'
        mov byte [tabela + 8], 'e'
        mov byte [tabela + 9], ' '
        mov byte [tabela + 10], 'f'
        mov byte [tabela + 11], 'u'
        mov byte [tabela + 12], 'g'
        mov byte [tabela + 13], 't'
        mov byte [tabela + 14], 'h'
        mov byte [tabela + 15], 'y'
        mov byte [tabela + 16], 'i'
        mov byte [tabela + 17], 'i'
        mov byte [tabela + 18], 'j'
        mov byte [tabela + 19], 'o'
        mov byte [tabela + 20], 'k'
        mov byte [tabela + 21], 'p'
        mov byte [tabela + 22], 'l'
        mov byte [tabela + 23], 'f'
        mov byte [tabela + 24], 'm'
        mov byte [tabela + 25], 'h'
        mov byte [tabela + 26], 'n'
        mov byte [tabela + 27], '.'
        mov byte [tabela + 28], 'o'
        mov byte [tabela + 29], 'g'
        mov byte [tabela + 30], 'p'
        mov byte [tabela + 31], 'd'
        mov byte [tabela + 32], 'q'
        mov byte [tabela + 33], 'a'
        mov byte [tabela + 34], 'r'
        mov byte [tabela + 35], 's'
        mov byte [tabela + 36], 's'
        mov byte [tabela + 37], 'l'
        mov byte [tabela + 38], 't'
        mov byte [tabela + 39], 'k'
        mov byte [tabela + 40], 'u'
        mov byte [tabela + 41], 'm'
        mov byte [tabela + 42], 'v'
        mov byte [tabela + 43], 'j'
        mov byte [tabela + 44], 'w'
        mov byte [tabela + 45], 'n'
        mov byte [tabela + 46], 'x'
        mov byte [tabela + 47], 'b'
        mov byte [tabela + 48], 'y'
        mov byte [tabela + 49], 'z'
        mov byte [tabela + 50], 'z'
        mov byte [tabela + 51], 'v'
        mov byte [tabela + 52], ' '
        mov byte [tabela + 53], 'c'
        mov byte [tabela + 54], '.'
        mov byte [tabela + 55], 'x'
        
        push ecx
        
        lea edx, [tabela]
              
	push edx   ;push substitution_table_addr
	push ecx   ;push addr_str8
	call break_substitution
	add esp, 8

        pop ecx
	; Print final solution (after some trial and error)
	push ecx
	call puts
	add esp, 4

	; Print substitution table

        lea edx, [tabela]
        
	push edx   ;push substitution_table_addr
	call puts
	add esp, 4

    ; Phew, finally done
     xor eax, eax
     leave
     ret