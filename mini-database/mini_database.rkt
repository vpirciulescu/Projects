
#lang racket
(define NULL 'null)

;====================================
;=            Cerința 1             =
;= Definirea elementelor de control =
;=          20 de puncte            =
;====================================

;= Funcțiile de acces
(define init-database
  (λ()
    '()
  ))

(define create-table
  (λ (table columns-name)
    (append (list table) (list columns-name) (map list columns-name))
 ))

(define get-name
  (λ (table)
    (if(null? table)
       '()
    (car table)))
 )

(define get-columns
  (λ (table)
   (if(null? table)
      '()
   (cadr table)))
  )

(define get-tables
  (λ (db)
    (if(null? db)
       '()
    (cdr db)))
 )

(define get-table
  (λ (db table-name)
    (if(empty? (cadr db))
       '()
     (if (equal? (car (cadr db)) table-name)
        (cadr db)
        (get-table (cdr db) table-name))))
)


(define add-table
  (λ (db table)
    (if(not(null? db))
       (append (list (append (car db) (list(car table)))) (cdr db) (list table))
             ;(append db (list table)))
       (append (list (list (car table))) (list table))))
)

(define remove-table
  (λ (db table-name)
    (cond ((null? db)
         '())
        ((string=? table-name (car (car db)))
         (cdr db))
        (else
         (cons (car db) 
               (remove-table (cdr db) table-name)))))
  )

;= Pentru testare, va trebui să definiți o bază de date (având identificatorul db) cu următoarele tabele

;============================================================================================
;=                         Tabela Studenți                                                   =
;= +----------------+-----------+---------+-------+-------+                                  =
;= | Număr matricol |   Nume    | Prenume | Grupă | Medie |                                  =
;= +----------------+-----------+---------+-------+-------+                                  =
;= |            123 | Ionescu   | Gigel   | 321CA |  9.82 |                                  =
;= |            124 | Popescu   | Maria   | 321CB |  9.91 |                                  =
;= |            125 | Popa      | Ionel   | 321CC |  9.99 |                                  =
;= |            126 | Georgescu | Ioana   | 321CD |  9.87 |                                  =
;= +----------------+-----------+---------+-------+-------+                                  =
;=                                                                                           =
;=                                         Tabela Cursuri                                    =
;= +------+----------+-----------------------------------+---------------+------------+      =
;= | Anul | Semestru |            Disciplină             | Număr credite | Număr teme |      =
;= +------+----------+-----------------------------------+---------------+------------+      =
;= | I    | I        | Programarea calculatoarelor       |             5 |          2 |      =
;= | II   | II       | Paradigme de programare           |             6 |          3 |      =
;= | III  | I        | Algoritmi paraleli și distribuiți |             5 |          3 |      =
;= | IV   | I        | Inteligență artificială           |             6 |          3 |      =
;= | I    | II       | Structuri de date                 |             5 |          3 |      =
;= | III  | II       | Baze de date                      |             5 |          0 |      =
;= +------+----------+-----------------------------------+---------------+------------+      =
;============================================================================================
(define db '(("Studenți" "Cursuri") ("Studenți" ("Număr matricol" "Nume" "Prenume" "Grupă" "Medie") ("Număr matricol" 123 124 125 126) ("Nume" "Ionescu" "Popescu" "Popa" "Georgescu")
                           ("Prenume" "Gigel" "Maria" "Ionel" "Ioana") ("Grupă" "321CA" "321CB" "321CC" "321CD") ("Medie" 9.82 9.91 9.99 9.87))
            ("Cursuri" ("Anul" "Semestru" "Disciplină" "Număr credite" "Număr teme") ("Anul" "I" "II" "III" "IV" "I" "III") ("Semestru" "I" "II" "I" "I" "II" "II")
                       ("Disciplină" "Programarea calculatoarelor" "Paradigme de programare" "Algoritmi paraleli și distribuiți"
                                      "Inteligență artificială" "Structuri de date" "Baze de date")
                       ("Număr credite" 5 6 5 6 5 5) ("Număr teme" 2 3 3 3 3 0)))
  )
                                                                                               

;====================================
;=            Cerința 2             =
;=         Operația insert          =
;=            10 puncte             =
;====================================

(define insert-final
  (λ (table one-rec)
    (if (empty? table)
        '()
    (begin (if(equal? (caar table) (car one-rec))
           (append (car table) (list (cdr one-rec)))
           (insert-final (cdr table) one-rec))))
       ))

(define find-record
  (λ (record col-name)
    (cons col-name (list-ref (map cdr record) (- (length (map car record)) (length (member col-name (map car record)))) ))
    ))
  
(define (insert-helper table record)
  (λ (col-name)
    (if (not (member col-name (map car record)))
                  (insert-final table (cons col-name NULL))  
                  (insert-final table (find-record record col-name)))
  ))

;;redefinire get-table
(define get-table-help
  (λ (db table-name)
     (if(null? db)
       '()
     (if (equal? (car (cadr db)) table-name)
        (cadr db)
        (get-table-help (cdr db) table-name)))))

(define (get-table-re db)
  (λ (table-name)
   (get-table-help db table-name))
)
;;;

(define insert
  (λ (db table-name record)
    
     (define tabel (get-table db table-name))
     (define cols (cadr tabel))
     (define coada (cdr (member table-name (car db))))
     (define cap (take (car db) (- (- (length (car db)) (length coada)) 1)))
    
    (if (empty? cols)
        '()
   (append (list (car db)) (map (get-table-re db) cap) (list (append (list table-name) (list cols) (map (insert-helper (cddr tabel) record) cols))) (map (get-table-re db) coada))
  )))


;====================================
;=            Cerința 3 a)          =
;=     Operația simple-select       =
;=             10 puncte            =
;====================================

(define simple-select-final
  (λ (ct col-name)
    (if(equal? (car (car ct)) col-name)
       (cdr(car ct))
       (simple-select-final (cdr ct) col-name))
    )
  )

(define simple-select-helper
  (λ(ct columns)
    (if(null? columns)
       '()
       (append (list (simple-select-final ct (car columns))) (simple-select-helper ct (cdr columns)))
       )
    ))

(define simple-select
  (λ (db table-name columns)
    (if (null? (cddr (get-table db table-name)))
        '()
    (simple-select-helper (cddr (get-table db table-name)) columns))
    ))


;====================================
;=            Cerința 3 b)          =
;=           Operația select        =
;=            30 de puncte          =
;====================================

(define only-cols
  (λ (columns)
    (if (null? columns)
        '()
        (begin (if (pair? (car columns))
                   (append (list (cdr (car columns))) (only-cols (cdr columns)))
                   (append (list (car columns)) (only-cols (cdr columns)))
                   ))
        )))

(define apply-cond
  (λ(conditions db table-name poz)
    (if (null? conditions)
        #t
        (if (not ((car (car conditions)) (list-ref (car (simple-select db table-name (list (cadr (car conditions)))))  poz) (caddr (car conditions))))
            #f
            (apply-cond (cdr conditions) db table-name poz)))))


  
(define (aplly-conds conditions db table-name)
  (λ (elem)
    (apply-cond conditions db table-name (car elem))))
   

(define select-first
  (λ (db table-name ssc conditions)
    (if (null? ssc)
        '()
        (append (list (filter (aplly-conds conditions db table-name) (map cons (range (length (car ssc))) (car ssc)))) (select-first db table-name (cdr ssc) conditions))
        )))

(define select-final
  (λ (col col-cond)
    (if (pair? col)
        (cond
          ((equal? (car col) 'min) (apply min col-cond))
          ((equal? (car col) 'max) (apply max col-cond))
          ((equal? (car col) 'count) (length (remove-duplicates col-cond)))
          ((equal? (car col) 'sum) (apply + col-cond))
          ((equal? (car col) 'avg) (/ (apply + col-cond) (length col-cond)))
          ((equal? (car col) 'sort-asc) (sort col-cond <))
          (else  (sort col-cond >)))          
        col-cond
    )))

(define refac-cols
  (λ (cols-p)
    (if (null? cols-p)
        '()
        (append (list (map cdr (car cols-p))) (refac-cols (cdr cols-p))))
    ))

;;ssc = simple-selected cols
(define select
  (λ (db table-name columns conditions)
    (define cols (only-cols columns))
    (define ssc (simple-select db table-name cols))
    (define cols-p (select-first db table-name ssc conditions))
    (define cols-cond (refac-cols cols-p))
    (map select-final columns cols-cond))
      )
    

;====================================
;=             Cerința 4            =
;=           Operația update        =
;=            20 de puncte          =
;====================================



(define modify-col
  ( λ (table val idx)
     (define cap (take table (+ (- (length (cadr table)) (length (member (car val) (cadr table)))) 2)))
     (define coada (cdr (take-right table (length (member (car val) (cadr table))))))
     (if (empty? idx)
         table
         (modify-col (append cap (list (list-set (car (take-right table (length (member (car val) (cadr table))))) (+ (car idx) 1) (cdr val))) coada)
                     val (cdr idx))
     )))


(define update-prim
  (λ (db table-name values idx)
    (if (empty? values)
        db
        (begin (if (member (car (car values)) (cadr (get-table db table-name)))
                   (update-prim (list-set db  (- (length (car db)) (length (cdr (member table-name (car db))))) (modify-col (get-table db table-name) (car values) idx))
                    table-name (cdr values) idx)
                   (update-prim db table-name (cdr values) idx)))
        )))


(define elem-modf
  (λ (db table-name conditions poz)
    (if (= poz (- (length (caddr (get-table db table-name))) 1))
        '()
        (begin (if (equal? (apply-cond conditions db table-name poz) #t)
                   (append (list poz) (elem-modf db table-name conditions (+ poz 1)))
                   (elem-modf db table-name conditions (+ poz 1)))))
    ))

  
(define update
  (λ (db table-name values conditions)

    (define idx (elem-modf db table-name conditions 0)) ;lista cu indici care respecta conditiile
    
    (if (empty? values)
       db
     (update-prim db table-name values idx))))
    

;====================================
;=             Cerința 5            =
;=           Operația remove        =
;=              10 puncte           =
;====================================

(define update-index
  (λ (idx)
    
   ; (map - (range l2 1 (+ (length idx) 1)) (range l1 0 (length idx))) =  (1 1 1 ... 1)
   (if (empty? idx)
       '()
       (map - idx (map - (range 1 (+ (length idx) 1)) (range 0 (length idx))))
       )))
    

(define delete-line
  (λ (table idx)
    (define cols (cddr table))
    (if (empty? idx)
        table
        (let  ((st (map - (range (+ (car idx) 1) (+ (+ (length (cadr table)) (car idx)) 1)) (range 0 (length (cadr table)))))
              ;(idx2 (- (- (length (car cols)) (car idx)) 1))
              (dr (map - (range (- (- (length (car cols)) (car idx)) 1) (+ (length (cadr table)) (- (- (length (car cols)) (car idx)) 1))) (range 1 (+ (length (cadr table)) 1)))))
            (delete-line (append (take table 2) (map append (map take cols st) (map take-right cols dr))) (update-index (cdr idx))))
    )))

(define delete
  (λ (db table-name conditions)

    (define tabel (get-table db table-name))
    (define cols (cadr tabel))
    (define coada (cdr (member table-name (car db))))
    (define cap (take (car db) (- (- (length (car db)) (length coada)) 1)))

    ;idx = lista cu indici care respecta conditiile
    
    (if (empty? conditions)
        (append (list (car db)) (map (get-table-re db) cap) (list (append (list table-name) (list cols))) (map (get-table-re db) coada))
        (let ((idx (elem-modf db table-name conditions 0)))
          (if (= (length idx) (- (length (caddr tabel)) 1))
              (append (list (car db)) (map (get-table-re db) cap) (list (append (list table-name) (list cols))) (map (get-table-re db) coada))
          (append (list (car db)) (map (get-table-re db) cap) (list (delete-line tabel idx)) (map (get-table-re db) coada)))
        )
    )))

;====================================
;=               Bonus              =
;=            Natural Join          =
;=            20 de puncte          =
;====================================


(define natural-join
  (λ (db tables columns conditions)

    'your-code-here
    
    
    ))

