{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE MultiParamTypeClasses,
             TypeSynonymInstances, FlexibleInstances #-}

module GAS where

import ProblemState

import qualified Data.Map.Strict as M
import Data.List

{-
    Pozițiile tablei de joc, în formă (linie, coloană), unde ambele coordonate
    pot fi negative.
-}
type Position = (Int, Int)

{-
    Culorile pătratelor și cercurilor.
-}
data Color = Red | Blue | Gray
    deriving (Eq, Ord, Show)

{-
    Orientările pătratelor și săgeților.
-}
data Heading = North | South | East | West
    deriving (Eq, Ord)

instance Show Heading where
    show North = "^"
    show South = "v"
    show East  = ">"
    show West  = "<"

{-
    *** TODO ***

    Un obiect de pe tabla de joc: pătrat/ cerc/ săgeată.
-}
type Square = (Color, Heading)
type Circle = Color
type Arrow = Heading

data Object = Square Color Heading | Circle Color | Arrow Heading 
    deriving (Eq, Ord)


{-
    *** TODO ***

    Reprezetarea textuală a unui obiect.
-}

instance Show Object where
	show (Square c h) = take 1 (show c) ++ (show h)
	show (Circle Red) = "r"
	show (Circle Blue) = "b"
	show (Circle Gray) = "g"
	show (Arrow h) = show h

{-
    *** TODO ***

    Un nivel al jocului.

    Recomandăm Data.Map.Strict.
-}

data Level = Level (M.Map Position [Object])
    deriving (Eq, Ord)

{-
    *** TODO ***

    Reprezetarea textuală a unui nivel.
-}
printObj :: (Num a, Eq a) => Object -> a -> [Char]

printObj (Circle c) sz 
	| sz == 2 = concat [show cerc]
	| otherwise = "  " ++ (show cerc)
		where cerc = Circle c
printObj (Square c h) sz
	| sz == 2 = concat [show s]
	| otherwise = (show s) ++ " "
		where s = Square c h 
printObj (Arrow h) sz 
	|sz == 1 = "  " ++ (show h)
	|otherwise = concat [show h]


printLineFinal :: Ord a => a -> [a] -> M.Map a [Object] -> [Char]
printLineFinal k keys lvl = if (elem k keys) then pFinal else "   " 
	where 
		objs = lvl M.! k 
		sz = length (objs)
		pFinal = concat [printObj obj sz| obj <- objs]

printEmptyLine :: Int -> [Char]	
printEmptyLine dim = intercalate "|" (take dim (repeat "   "))

printLine :: (Ord a, Ord t, Enum t) => a -> [a] -> M.Map (a, t) [Object] -> [Char]
printLine x kx lvl = if (elem x kx) then (init pLine) else printEmptyLine dim
			where 
				keys = M.keys (M.filterWithKey (\key _ -> fst key == x) lvl)
				ky = map snd (M.keys lvl)
				yy = [minimum ky..maximum ky]
				dim = length yy
				pLine = concat [printLineFinal (x, y) keys lvl ++ "|" | y <- yy] 
		
instance Show Level where
    show (Level lvl) = init printLevel 
	where 
		kx = nub (sort (map fst (M.keys lvl)))
		xx = [fst (fst (M.findMin lvl))..fst(fst (M.findMax lvl))]
		printLevel = concat [printLine x kx lvl ++ "\n" | x <- xx]
		

{-
    *** TODO ***

    Nivelul vid, fără obiecte.
-}

emptyLevel :: Level
emptyLevel = Level M.empty

{-
    *** TODO ***

    Adaugă un pătrat cu caracteristicile date la poziția precizată din nivel.
-}

addSquare :: Color -> Heading -> Position -> Level -> Level
addSquare c h poz (Level level) = Level (M.insertWith (++) poz [Square c h] level)

{-
    *** TODO ***

    Adaugă un cerc cu caracteristicile date la poziția precizată din nivel.
-}
addCircle :: Color -> Position -> Level -> Level
addCircle c poz (Level level) = Level (M.insertWith (++) poz [Circle c] level)

{-
    *** TODO ***

    Adaugă o săgeată cu caracteristicile date la poziția precizată din nivel.
-}
addArrow :: Heading -> Position -> Level -> Level
addArrow h poz (Level level) = Level (M.insertWith (++) poz [Arrow h] level)

{-
    *** TODO ***

    Mută pătratul de la poziția precizată din nivel. Dacă la poziția respectivă
    nu se găsește un pătrat, întoarce direct parametrul.
-}
isSquare :: [Char] -> Bool
isSquare c = (c == "[R") || (c == "[G") || (c == "[B") 

findSquare :: Show a => [a] -> [a]
findSquare objs 
	| ((length objs == 2) && (length (show (head objs)) == 2)) = [head objs]
	| ((length objs == 2) && (length (show (head objs)) == 1)) = tail objs
	| otherwise = objs

findObj :: Show t => [t] -> [t]
findObj objs
	|length (show (head objs)) == 1 = [head objs]
	|otherwise = tail objs

f :: Show t1 => t -> [t1] -> Maybe [t1]
f _ x = if (length x) == 2 then Just (findObj x) else Nothing

--in functie de ce element se afla la poz la care trebuie sa mut => element nou 
g :: t -> [Object] -> [Object] -> [Object]
g _ [(Square c _)] [(Arrow a)] = [(Square c a)] ++ [(Arrow a)]
g _ [(Square c h)] [(Circle co)] = [(Square c h)] ++ [(Circle co)]
g _ [(Square c h), (Arrow _)] [(Arrow a2)] =  [(Square c h)] ++ [(Arrow a2)]
g _ [(Square c h), (Circle _)] [(Arrow a)] =  [(Square c h)] ++ [(Arrow a)]
g _ [(Square c h), (Arrow _)] [(Circle c2)] =  [(Square c h)] ++ [(Circle c2)]
g _ [(Square c h), (Circle _)] [(Circle c2)] =  [(Square c h)] ++ [(Circle c2)]
g _ [(Square c h)] [] = [(Square c h)]
g _ [(Square c h), (Circle _)] [] =  [(Square c h)]
g _ [(Square c h), (Arrow _)] [] =  [(Square c h)] 
g _ _ _ = undefined

--mutare principala
updatePoz :: [Char] -> (Int, Int) -> Level -> Level	
updatePoz dir poz@(x, y) (Level level) 
	| dir == "^" = Level (M.insertWithKey g (x-1, y) obj new_level)
	| dir == "v" = Level (M.insertWithKey g (x+1, y) obj new_level)
	| dir == ">" = Level (M.insertWithKey g (x, y+1) obj new_level)
	| otherwise = Level (M.insertWithKey g (x, y-1) obj new_level)
		where 
			obj = findSquare (level M.! poz)
			new_level = M.updateWithKey f poz level

--nu se afla patrat la poz
isValidpos :: (Show a, Ord k) => k -> M.Map k [a] -> Bool			
isValidpos poz level
	| M.member poz level == False = True
	| isSquare (take 2 (show (findSquare (level M.! poz)))) == True = False
	| otherwise = True

isEmpty :: (Show a, Ord t1, Ord t, Num t1, Num t) =>
                 (t, t1) -> [Char] -> M.Map (t, t1) [a] -> Bool
isEmpty (x, y) dir level 
	| dir == "^" = isValidpos (x-1, y) level 
	| dir == "v" = isValidpos (x+1, y) level
	| dir == "<" = isValidpos (x, y-1) level
	| otherwise = isValidpos (x, y+1) level

--avansez cu o poz in functie de directie daca se afla patrat in fata patratului 
--pe care trebuie sa-l mut
newPoz :: (Num t1, Num t) => (t, t1) -> [Char] -> (t, t1)
newPoz (x, y) dir 
	| dir == "^" = (x-1, y)
	| dir == "v" = (x+1, y)
	| dir == "<" = (x, y-1)
	| otherwise = (x, y+1) 

--True = e liber (cerc, sageata sau nimic) => mutare
--False = e patrat in fata => mai multe mutari succesive	
moveFinal :: Bool -> Position -> [Char] -> Level -> Level	
moveFinal True poz dir (Level level) = updatePoz dir poz (Level level)
moveFinal False poz dir (Level level) = updatePoz dir poz new_level
	where		
		new_poz = newPoz poz dir
		new_level = moveFinal (isEmpty new_poz dir level) new_poz dir (Level level)

--caz1. nu exista element la poz data => level 
--caz2. e cerc sau sageata => level
--caz3. patrat => level nou
move :: Position  -- Poziția
     -> Level     -- Nivelul inițial
     -> Level     -- Nivelul final
move poz (Level level) 
	| M.member poz level == False = Level level
	| isSquare (take 2 (show (findSquare (level M.! poz)))) == False = Level level
	| otherwise = moveFinal (isEmpty poz dir level) poz dir (Level level)
		where
			obj = findSquare (level M.! poz)
			dir = drop 2 (take 3 (show obj))	
{-
    *** TODO ***

    Instanțiați clasa `ProblemState` pentru jocul nostru.
-}

--verifc daca la poz respectiva se afla  patrat + cerc de aceeasi culoare
verifyGoal :: [Object] -> Bool
verifyGoal [(Square c _), (Circle co)] = c == co
verifyGoal _ = False

--verific toate pozitiile posibile care contin patrat 
checkGoal :: Level -> [Bool]
checkGoal (Level level) = [verifyGoal obj |  obj <- objs]
	where 
		 objs = [level M.! poz | poz <- pos_moves]
		 b = [(x, y)  | x <- [xmin..xmax], y <- [ymin..ymax]]
		 pos_moves = filter (\(x, y) -> square (x,y) level == True) b
		 xmin = fst (fst (M.findMin level))
		 xmax = fst (fst (M.findMax level))	
		 ymin = minimum (map snd (M.keys level))
		 ymax = maximum (map snd (M.keys level))

--verific daca e patrat la poz data
square :: (Show a, Ord k) => k -> M.Map k [a] -> Bool
square poz level
	| M.member poz level == False = False
	| otherwise = isSquare (take 2 (show (findSquare (level M.! poz))))

--generez toate starile urmatoare posibile 
succ2 :: Level -> [((Int, Int), Level)]
succ2 (Level level) = [(a, s) | a <- pos_moves, let s = move a (Level level)]
		where 
			b = [(x, y)  | x <- [xmin..xmax], y <- [ymin..ymax]]
			pos_moves = filter (\(x, y) -> square (x,y) level == True) b
			xmin = fst (fst (M.findMin level))
			xmax = fst (fst (M.findMax level))	
			ymin = minimum (map snd (M.keys level))
			ymax = maximum (map snd (M.keys level))


instance ProblemState Level Position where

	successors level = succ2 level 
	isGoal level = not (elem False (checkGoal level))

    -- Doar petru BONUS
    -- heuristic =
