{-# OPTIONS_GHC -Wall #-}

module Search where

import ProblemState

{-
    *** TODO ***

    Tipul unei nod utilizat în procesul de căutare. Recomandăm reținerea unor
    informații legate de:

    * stare;
    * acțiunea care a condus la această stare;
    * nodul părinte, prin explorarea căruia a fost obținut nodul curent;
    * adâncime.
-}


data Node s a  = Node s a (Node s a) Int
    deriving (Eq, Show)

{-
    *** TODO ***

    Întoarce starea stocată într-un nod.
-}

nodeState :: Node s a -> s
nodeState (Node s _ _ _) = s

nodeAction :: Node s a -> a
nodeAction (Node _ a _ _) = a

findParinte :: Node s a -> Node s a
findParinte (Node _ _  parinte _) = parinte

calcAd :: Node s a -> Int
calcAd (Node _ _ _ adancime) = adancime

{-
    *** TODO ***

    Întoarce lista nodurilor rezultate prin parcurgerea limitată în adâncime
    a spațiului stărilor, pornind de la starea dată ca parametru.

    Pentru reținerea stărilor vizitate, recomandăm Data.Set. Constrângerea
    `Ord s` permite utilizarea tipului `Set`.

    În afara BONUS-ului, puteți ignora parametrul boolean. Pentru BONUS, puteți
    sorta lista succesorilor folosind `sortBy` din Data.List.
-}
	 
startDfs :: (ProblemState s a, Ord s) => Int -> Int -> [Node s a] -> [Node s a] -> [Node s a]
startDfs adanc maxH stack vizitat 
	| (length stack) == 0 = vizitat
	| adanc > maxH = startDfs (calcAd (head (tail stack))) maxH (tail stack) vizitat
	| or (map f vizitat) == True =  startDfs (calcAd (head (tail stack))) maxH (tail stack) vizitat
	| otherwise = startDfs (adanc + 1) maxH ((foldr ac [] succs) ++ (tail stack)) (vizitat ++ [head stack])
		where 
			succs = successors (nodeState (head stack))
			ac (a, s) acc = (Node s a (head stack) (adanc + 1)):acc
			f nod = (nodeState (head stack)) == (nodeState nod)


limitedDfs :: (ProblemState s a, Ord s)
           => s           -- Starea inițială
           -> Bool        -- Pentru BONUS, `True` dacă utilizăm euristica
           -> Int         -- Adâncimea maximă de explorare
           -> [Node s a]  -- Lista de noduri
limitedDfs si False maxHeight = startDfs 0 maxHeight [(Node si undefined undefined undefined)] []
limitedDfs _ True _ = undefined

{-
    *** TODO ***

    Explorează în adâncime spațiul stărilor, utilizând adâncire iterativă,
    pentru determinarea primei stări finale întâlnite.

    Întoarce o perche între nodul cu prima stare finală întâlnită și numărul
    de stări nefinale vizitate până în acel moment.

    În afara BONUS-ului, puteți ignora parametrul boolean.
-}

--parcurg pana cand e rezolvat levelul -> (isGoal level) == True 
iterativ :: (ProblemState s a, Num t) => [Node s a1] -> t -> (Node s a1, t)
iterativ sa nrs 
	| (length sa == 1) && (isGoal (nodeState (head sa)) == True) = ((head sa), nrs)
	| length sa == 1 = ((head sa), nrs+1)
	| isGoal (nodeState (head sa)) == True = ((head sa), nrs)
	| otherwise = iterativ (tail sa) (nrs + 1)

iterDoi :: (ProblemState s a, Ord s, Num t) => s -> Bool -> (Node s a, t) -> Int -> t -> (Node s a, t)
iterDoi s bool parinte ad nrs 
	| isGoal (nodeState (fst parinte)) == True = ((fst parinte), (nrs + (snd parinte)))
	| otherwise = iterDoi s bool new_p (ad+1) (nrs + (snd parinte)) 
		where
			new_p = iterativ (limitedDfs s bool (ad+1)) 0

iterativeDeepening :: (ProblemState s a, Ord s)
    => s                -- Starea inițială
    -> Bool             -- Pentru BONUS, `True` dacă utilizăm euristica
    -> (Node s a, Int)  -- (Nod cu prima stare finală,
                        --  număr de stări nefinale vizitate)
iterativeDeepening si bool = iterDoi si bool (iterativ (limitedDfs si bool 0) 0) 0 0

{-
    *** TODO ***

    Pornind de la un nod, reface calea către nodul inițial, urmând legăturile
    către părinți.

    Întoarce o listă de perechi (acțiune, stare), care se încheie în starea
    finală, dar care EXCLUDE starea inițială.
-}

findPath :: Node s a -> [(a, s)] -> [(a, s)]
findPath n path 
	| calcAd n == 1 = (nodeAction n, nodeState n) : path
	| otherwise = findPath (findParinte n) ((nodeAction n, nodeState n) : path)

extractPath :: Node s a -> [(a, s)]
extractPath n = findPath n []

{-
    Poate fi utilizată pentru afișarea fiecărui element al unei liste
    pe o linie separată.
-}
printSpacedList :: Show a => [a] -> IO ()
printSpacedList = mapM_ (\a -> print a >> putStrLn (replicate 20 '*'))
